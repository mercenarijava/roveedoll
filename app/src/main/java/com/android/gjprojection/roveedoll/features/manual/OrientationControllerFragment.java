package com.android.gjprojection.roveedoll.features.manual;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.android.gjprojection.roveedoll.R;
import com.android.gjprojection.roveedoll.UIComponent;
import com.android.gjprojection.roveedoll.features.manual.Direction.Orientation;
import com.android.gjprojection.roveedoll.features.manual.MarceDir.Marce;
import com.android.gjprojection.roveedoll.features.manual.Widget.AngolationWidget;
import com.android.gjprojection.roveedoll.features.manual.Widget.ProssimityControl;
import com.android.gjprojection.roveedoll.services.bluetooth.BleSendMessageL;
import com.android.gjprojection.roveedoll.services.bluetooth.BluetoothManager;

import static java.lang.Thread.sleep;

public class OrientationControllerFragment extends Fragment implements UIComponent, Orientation.Listener {
    static final private int MAX_DEGREE = 71;

    @NonNull
    private Orientation mOrientation;
    @NonNull
    private ImageView directionView;
    @NonNull
    private Marce marce;
    @NonNull
    private ConstraintLayout lay;
    @NonNull
    private AngolationWidget degree;
    @NonNull
    private ProssimityControl shoot;
    @NonNull
    private Thread movementThread;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.activity_orientation_controller,
                container,
                false
        );
        connectViews(v);
        connectListeners();

        return v;
    }

    @Override
    public void connectViews(View... views) {
        this.directionView = views[0].findViewById(R.id.square);
        this.marce = views[0].findViewById(R.id.marce);
        this.lay = views[0].findViewById(R.id.lay);
        this.mOrientation = new Orientation(getActivity());
        this.shoot = new ProssimityControl(getActivity());
        this.degree = new AngolationWidget(getActivity());
        this.lay.addView(shoot);
        this.lay.addView(degree);
        this.movementThread = new Thread(new MovementThread(marce, degree));
    }

    @Override
    public void connectListeners() {
        if (getContext() != null) {
            BluetoothManager.init(getContext().getApplicationContext()).getMessageReceiver().observe(
                    this, bleReceiveMessage -> {
                        if (bleReceiveMessage == null) return;
                        int distance = bleReceiveMessage.getObstacleDistanceCM(); //MAX_DISTANCE - distanza attuale IMPORTANTE
                        shoot.onIdentifiedObjectProssimity(distance); // parte di ricezione della distanza
                    }
            );
        }
        this.movementThread.start();
    }


    @Override
    public void onDetach() {
        this.movementThread.interrupt();
        BluetoothManager.writeData(new BleSendMessageL(0, 9));
        super.onDetach();
    }

    /**
     * Start listening orientation sensor
     */
    @Override
    public void onStart() {
        super.onStart();
        marce.setOnDiscreteSliderChangeListener(position -> {
            degree.setVelocityTextSize(position);
            shoot.setRotation(position == 0 ? 180 : 0);
        });
        mOrientation.startListening(this);
    }

    /**
     * Stop listening orientation sensor
     */
    @Override
    public void onStop() {
        super.onStop();
        mOrientation.stopListening();
    }

    /**
     * max rotation 180°
     *
     * @param roll
     */
    @Override
    public void onOrientationChanged(float roll) {
        if ((roll * 2 > -MAX_DEGREE) && (roll * 2 < MAX_DEGREE)) { //direzione dritta
            directionView.setRotation((roll * -(2)));
            shoot.setRotation((roll * -(2)));
            degree.setRotation((roll * -(2)));
        }

    }

    /**
     * This subClass is used for send info to the robot bluetooth reciver
     * every 500ms
     */
    private class MovementThread implements Runnable {

        private Marce marce;
        private AngolationWidget rotation;

        private MovementThread(Marce marce, AngolationWidget rotation) {
            this.marce = marce;
            this.rotation = rotation;
        }

        @Override
        public void run() {

            int speed_sx = 0;
            int speed_dx = 0;
            int lastSpeedSx = speed_sx;
            int lastSpeedDx = speed_dx;

            while (true) {
                try {
                    sleep(100); //frequenza di invio dati al robottino
                    int current_marcia = marce.getPosition() - 1; //se si va in retro la velocità è negativa
                    int current_angolation = (int) rotation.getRotation();
                    int speed_generic = 250 * current_marcia;

                    speed_dx = current_angolation > 0 ? (speed_generic - ((speed_generic * current_angolation) / 100)) : speed_generic;
                    speed_sx = current_angolation >= 0 ? speed_generic : (speed_generic - ((speed_generic * (-current_angolation)) / 100));

                    if (writeSpeed(speed_sx, speed_dx, lastSpeedSx, lastSpeedDx)) {
                        lastSpeedSx = speed_sx;
                        lastSpeedDx = speed_dx;
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        private boolean writeSpeed(
                int speedSx,
                int speedDx,
                int lSpeedSx,
                int lSpeedDx) {
            final boolean execute = speedDx != lSpeedDx || speedSx != lSpeedSx;
            if (execute) {
                BluetoothManager.writeData(new BleSendMessageL(speedSx, speedDx));
            }
            return execute;
        }
    }

}
