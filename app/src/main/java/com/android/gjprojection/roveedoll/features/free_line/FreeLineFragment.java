package com.android.gjprojection.roveedoll.features.free_line;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.MainThread;
import android.support.annotation.NonNull;
import android.support.annotation.UiThread;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.android.gjprojection.roveedoll.R;
import com.android.gjprojection.roveedoll.UIComponent;
import com.android.gjprojection.roveedoll.features.free_line.views.FreeLineView;
import com.android.gjprojection.roveedoll.services.bluetooth.BleReceiveMessage;
import com.android.gjprojection.roveedoll.services.bluetooth.BleSendMessageV;
import com.android.gjprojection.roveedoll.services.bluetooth.BluetoothManager;
import com.android.gjprojection.roveedoll.utils.AnimatorsUtils;
import com.android.gjprojection.roveedoll.utils.CommonUtils;

import java.util.ArrayList;

import javax.annotation.Nullable;

public class FreeLineFragment extends Fragment implements UIComponent {
    private static final int MENU_SLIDE_MILLIS = 300;
    private static final int MENU_COLOR_CHANGE_MILLIS = 400;
    private static final int CLOSE_MENU_DELAY_MILLIS = 1500;

    @NonNull
    FreeLineViewModel viewModel;
    @NonNull
    Handler mainHandler = new Handler(Looper.getMainLooper());

    @NonNull
    FreeLineView view;
    @NonNull
    LinearLayout loadingLayout;

    @NonNull
    TextView linesCount;

    @NonNull
    RecyclerView console;
    @NonNull
    LinearLayoutManager consoleManager;
    @NonNull
    ConsoleRecyclerViewAdapter consoleAdapter;
    @NonNull
    LinearLayout consoleContainer;
    @NonNull
    LinearLayout consoleTitleLayout;

    @NonNull
    LinearLayout actionsLayout;
    @NonNull
    LinearLayout actionsContent;
    @NonNull
    LinearLayout speedLayout;
    @NonNull
    LinearLayout speedContent;
    @NonNull
    SeekBar speedSeekbar;
    @NonNull
    TextView speedTextView;

    @NonNull
    ImageView uploadAction;
    @NonNull
    ImageView deleteAction;
    @NonNull
    ImageView undoAction;

    @Nullable
    CommonUtils.GenericAsyncTask lastUploadTask = null;

    public FreeLineFragment() {
        // Required empty public constructor
    }

    public static FreeLineFragment newInstance() {
        return new FreeLineFragment();
    }

    @Override
    public void onCreate(
            Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(
            LayoutInflater inflater,
            ViewGroup container,
            Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View v = inflater.inflate(R.layout.fragment_free_line, container, false);
        connectViews(v);
        connectListeners();
        return v;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        this.mainHandler.removeCallbacksAndMessages(null);
    }

    @Override
    public void connectViews(View... views) {
        this.viewModel = ViewModelProviders.of(FreeLineFragment.this).get(FreeLineViewModel.class);
        if (views.length == 0) return;
        this.view = views[0].findViewById(R.id.data_background);
        this.loadingLayout = views[0].findViewById(R.id.loading_layout);
        this.linesCount = views[0].findViewById(R.id.lines_count_textview);
        this.console = views[0].findViewById(R.id.console_recyclerview);
        this.consoleManager = new LinearLayoutManager(getContext());
        this.console.setLayoutManager(consoleManager);
        this.consoleAdapter = new ConsoleRecyclerViewAdapter();
        this.console.setAdapter(consoleAdapter);
        this.consoleContainer = views[0].findViewById(R.id.console_container);
        this.consoleTitleLayout = views[0].findViewById(R.id.console_title_layout);
        this.actionsLayout = views[0].findViewById(R.id.actions_layout);
        this.actionsContent = views[0].findViewById(R.id.actions_content);
        this.deleteAction = views[0].findViewById(R.id.delete_action);
        this.uploadAction = views[0].findViewById(R.id.upload_action);
        this.undoAction = views[0].findViewById(R.id.undo_action);
        this.speedLayout = views[0].findViewById(R.id.velocity_layout);
        this.speedContent = views[0].findViewById(R.id.velocity_content);
        this.speedSeekbar = views[0].findViewById(R.id.velocity_seekbar);
        this.speedTextView = views[0].findViewById(R.id.velocity_textview);
    }

    @Override
    public void connectListeners() {
        this.manageStateUpload(false);
        this.view.setCommunicationViewModel(viewModel);

        this.viewModel.getPointAdd().observe(
                FreeLineFragment.this,
                pointScaled -> {
                    if (pointScaled == null) return;
                    consoleAdapter.add("action add-point-id " + pointScaled.getId());
                }
        );

        this.viewModel.getPointDeleted().observe(
                FreeLineFragment.this,
                pointScaled -> {
                    if (pointScaled == null) return;
                    consoleAdapter.add("action delete-point-id " + pointScaled.getId());
                }
        );

        this.viewModel.getLinesCount().observe(
                FreeLineFragment.this,
                lines -> {
                    if (lines == null) return;
                    final String s = String.valueOf(FreeLineView.MAX_LINES_ALLOWED - lines);
                    linesCount.setText(s);
                }
        );

        this.consoleAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                consoleManager.smoothScrollToPosition(
                        console,
                        null,
                        consoleAdapter.getItemCount()
                );
            }
        });

        this.consoleContainer.setOnClickListener(new View.OnClickListener() {
            boolean open = true;

            @Override
            public void onClick(View v) {
                open = !open;
                openConsole(open);
            }
        });

        this.deleteAction.setOnClickListener(view -> {
            FreeLineFragment.this.view.clear();
            consoleAdapter.add("action background-clear");
        });

        this.undoAction.setOnClickListener(view -> {
            FreeLineFragment.this.view.undo();
            consoleAdapter.add("action undo");
        });

        this.speedSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                updateSpeed(i);
                view.setSpeed(i);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                consoleAdapter.add("action set-speed " + seekBar.getProgress());
            }
        });
        setRightMenu();

        this.console.setLayoutFrozen(true);
        this.speedSeekbar.setProgress(50);
        this.consoleAdapter.add("--- Hi, my name is Dollconsole !");
        this.consoleAdapter.add("--- i am here to let you control all the actions performed...");
        this.consoleAdapter.add("--- Good Game :)");

        if (getContext() == null) return; // non può succedere
        BluetoothManager.init(getContext())
                .getActiveDeviceConnection()
                .observe(this, connected -> {
                    if (connected == null) return;
                    if (!connected) removeInputMessagesObserver();

                    notifyUploadState(false);
                    setWaitingConnection(connected);

                });

    }

    private void setWaitingConnection(
            final boolean connected) {

        this.loadingLayout.animate()
                .alpha(connected ? 0 : 1);

        manageStateUpload(!connected);

    }

    private void removeInputMessagesObserver() {
        if (getActivity() != null)
            BluetoothManager.init(getContext())
                    .getMessageReceiver()
                    .removeObservers(getActivity());
    }

    @UiThread
    private void notifyUploadState(
            final boolean success) {
        this.consoleAdapter.upload(
                success ? "upload completed successfully!" : "upload failed :(",
                success
        );
    }

    @UiThread
    private void notifyCollision() {
        this.consoleAdapter.upload(
                "upload failed, the EV3 robot has collided",
                false
        );
    }

    @UiThread
    private void handleBleMessage(
            @Nullable BleReceiveMessage msg) {
        if (msg == null) return;
        if (msg.hasCollided()) {
            removeInputMessagesObserver();
            notifyCollision();
            manageStateUpload(false);
        } else if (
                msg.isUploadProcessSuccess(
                        view.getPoints().get(
                                view.getPoints().size() - 1
                        ).id
                )) {
            removeInputMessagesObserver();
            notifyUploadState(true);
            manageStateUpload(false);
        } else {
            this.consoleAdapter.add("reached point " + msg.getLineId());
        }
    }

    private void startUpdateProcess() {
        final boolean canStartUpdate = this.viewModel.getLinesCount() != null &&
                this.viewModel.getLinesCount().getValue() != null &&
                this.viewModel.getLinesCount().getValue() > 0;


        if (canStartUpdate) {
            this.consoleAdapter.add("free line upload started", true, true);

            if (getActivity() != null && getContext() != null) {
                BluetoothManager.init(getContext())
                        .getMessageReceiver()
                        .observe(getActivity(), this::handleBleMessage);
            }

            manageStateUpload(true);

            if (lastUploadTask != null) {
                lastUploadTask.cancel(true);
            }

            this.lastUploadTask = getUploadAsyncTask();
            this.lastUploadTask.execute();
        } else {
            this.consoleAdapter.add("exception: nothing to update", false, false);
        }
    }

    private CommonUtils.GenericAsyncTask getUploadAsyncTask() {
        return new CommonUtils.GenericAsyncTask(() -> {
            @NonNull final ArrayList<FreeLineView.PointScaled> points = view.getPoints();

            Float lastDirection = null;
            for (int i = 1; i < points.size(); i++) {
                float direction = CommonUtils.getAngleInOrigins(
                        points.get(i).x - points.get(i - 1).x,
                        points.get(i - 1).y - points.get(i).y,
                        points.get(i).y <= points.get(i - 1).y
                );

                float saveNewDirection = direction;
                direction = calibrateRobotRotation(lastDirection, direction);
                lastDirection = saveNewDirection;

                final BleSendMessageV bleMessage = new BleSendMessageV(
                        points.get(i).id,
                        points.get(i).speed,
                        (int) direction,
                        (int) ((
                                Math.sqrt(
                                        Math.pow(points.get(i).x - points.get(i - 1).x, 2) +
                                                Math.pow(points.get(i - 1).y - points.get(i).y, 2)
                                ) / getResources()
                                        .getInteger(
                                                R.integer.free_line_view_grid_square_width
                                        )
                        ) * 100)
                );

                final boolean writeSegmentResult = BluetoothManager.writeData(bleMessage);
                if (!writeSegmentResult) {
                    this.mainHandler.post(() -> handleLineWriteBroke(bleMessage.lineId));
                }
            }

        });
    }

    private float calibrateRobotRotation(
            @Nullable final Float lastDirection,
            final float newDirection) {
        float baseDirection = -90;
        float direction = newDirection;

        if (lastDirection != null) {
            direction -= lastDirection;
            if (direction > 180) return direction - 360;
            if (direction < -180) return direction + 360;
            return direction;
        }

        if (direction >= 270 && direction <= 360) {
            return baseDirection + (direction - 360);
        } else {
            return direction + baseDirection;
        }
    }

    @MainThread
    private void handleLineWriteBroke(
            final long lineId) {
        this.consoleAdapter.add("Error occurred in line with id: " + lineId);
        // TODO
    }

    private void manageStateUpload(
            final boolean uploadStateOn) {
        this.view.setEnabled(!uploadStateOn);
        this.undoAction.setEnabled(!uploadStateOn);
        this.deleteAction.setEnabled(!uploadStateOn);
        this.uploadAction.setEnabled(!uploadStateOn);
        this.speedSeekbar.setEnabled(!uploadStateOn);
        this.actionsLayout.setEnabled(!uploadStateOn);
        this.speedLayout.setEnabled(!uploadStateOn);
        if (uploadStateOn) {
            this.openConsole(true);
        }
        this.consoleContainer.setEnabled(!uploadStateOn);
        this.actionsLayout.animate().alpha(uploadStateOn ? 0.7f : 1);
        this.speedLayout.animate().alpha(uploadStateOn ? 0.7f : 1);

        AnimatorsUtils.animateBackgroundTint(
                consoleContainer,
                getResources()
                        .getColor(uploadStateOn ? R.color.white_transparent50 : R.color.default_white),
                getResources()
                        .getColor(uploadStateOn ? R.color.default_white : R.color.white_transparent50),
                MENU_COLOR_CHANGE_MILLIS
        );

        this.consoleContainer.animate()
                .scaleY(uploadStateOn ? 1.1f : 1f);
    }

    private void updateSpeed(final int speed) {
        final String s = speed + "%";
        speedTextView.setText(s);
    }

    private void openConsole(boolean open) {
        float offset = open ? 0 : -1 * console.getWidth();
        this.consoleContainer.animate().translationZ(open ? 20 : 0);
        AnimatorsUtils.animateX(consoleContainer, offset, MENU_SLIDE_MILLIS,
                open ? new AccelerateInterpolator() : new OvershootInterpolator());
    }

    private void setRightMenu() {
        this.mainHandler.postDelayed(() -> {
            animateActionLayout(actionsLayout, actionsContent, false);
            animateBackgroundTint(actionsLayout, false);
            animateActionLayout(speedLayout, speedContent, false);
            animateBackgroundTint(speedLayout, false);
        }, CLOSE_MENU_DELAY_MILLIS);

        this.actionsLayout.setOnClickListener(new View.OnClickListener() {
            boolean open = false;

            @Override
            public void onClick(View view) {
                open = !open;
                animateActionLayout(actionsLayout, actionsContent, open);
                animateBackgroundTint(actionsLayout, open);
            }
        });

        this.speedLayout.setOnClickListener(new View.OnClickListener() {
            boolean open = false;

            @Override
            public void onClick(View view) {
                open = !open;
                animateActionLayout(speedLayout, speedContent, open);
                animateBackgroundTint(speedLayout, open);
            }
        });

        this.uploadAction.setOnClickListener(v -> startUpdateProcess());
    }

    private void animateActionLayout(
            @NonNull View fullLayout,
            @NonNull View contentLayout,
            final boolean open) {
        if (getView() == null) return;
        float offset = 0;
        if (!open)
            offset += contentLayout.getWidth();
        AnimatorsUtils.animateX(fullLayout, offset, MENU_SLIDE_MILLIS,
                open ? new AccelerateInterpolator() : new OvershootInterpolator());
    }

    private void animateBackgroundTint(
            @NonNull View fullLayout,
            final boolean open) {
        int colorTo = getResources()
                .getColor(open ? R.color.white_transparent50 : R.color.colorPrimaryDark);
        int colorFrom = getResources()
                .getColor(open ? R.color.colorPrimaryDark : R.color.white_transparent50);
        AnimatorsUtils.animateBackgroundTint(fullLayout, colorFrom, colorTo, MENU_COLOR_CHANGE_MILLIS);
    }
}
