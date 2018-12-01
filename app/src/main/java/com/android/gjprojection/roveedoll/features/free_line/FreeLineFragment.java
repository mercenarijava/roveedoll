package com.android.gjprojection.roveedoll.features.free_line;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
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
import com.android.gjprojection.roveedoll.utils.AnimatorsUtils;

public class FreeLineFragment extends Fragment implements UIComponent {
    private static final int MENU_SLIDE_MILLIS = 300;
    private static final int MENU_COLOR_CHANGE_MILLIS = 400;

    @NonNull
    FreeLineView view;
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

    public FreeLineFragment() {
        // Required empty public constructor
    }

    public static FreeLineFragment newInstance() {
        FreeLineFragment fragment = new FreeLineFragment();
        return fragment;
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
        this.view = (FreeLineView) inflater.inflate(R.layout.fragment_free_line, container, false);
        connectViews(view);
        connectListeners();
        return view;
    }

    @Override
    public void connectViews(View... views) {
        if (views.length == 0) return;
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
        this.deleteAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FreeLineFragment.this.view.clear();
            }
        });

        this.undoAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FreeLineFragment.this.view.undo();
            }
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

            }
        });
        setRightMenu();
    }

    @Override
    public void onAttach(
            Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    private void updateSpeed(final int speed) {
        final String s = speed + "%";
        speedTextView.setText(s);
    }

    private void setRightMenu() {
        this.actionsLayout.setOnClickListener(new View.OnClickListener() {
            boolean open = true;

            @Override
            public void onClick(View view) {
                open = !open;
                animateActionLayout(actionsLayout, actionsContent, open);
                animateBackgroundTint(actionsLayout, open);
            }
        });

        this.speedLayout.setOnClickListener(new View.OnClickListener() {
            boolean open = true;

            @Override
            public void onClick(View view) {
                open = !open;
                animateActionLayout(speedLayout, speedContent, open);
                animateBackgroundTint(speedLayout, open);
            }
        });
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
                .getColor(open ? R.color.default_white : R.color.colorPrimaryDark);
        int colorFrom = getResources()
                .getColor(open ? R.color.colorPrimaryDark : R.color.default_white);
        AnimatorsUtils.animateBackgroundTint(fullLayout, colorFrom, colorTo, MENU_COLOR_CHANGE_MILLIS);
    }
}
