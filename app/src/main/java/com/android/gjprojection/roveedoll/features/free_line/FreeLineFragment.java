package com.android.gjprojection.roveedoll.features.free_line;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
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
import com.android.gjprojection.roveedoll.utils.AnimatorsUtils;

public class FreeLineFragment extends Fragment implements UIComponent {
    private static final int MENU_SLIDE_MILLIS = 300;
    private static final int MENU_COLOR_CHANGE_MILLIS = 400;

    @NonNull
    FreeLineView view;
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
        updateSpeed(50);

        consoleAdapter.add("--- Hi, my name is Dollconsole !");
        consoleAdapter.add("--- i am here to let you control all the actions performed...");
        consoleAdapter.add("--- Good Game :)");

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

    private void openConsole(boolean open) {
        float offset = open ? 0 : -1 * console.getWidth();
        this.consoleContainer.animate().translationZ(open ? 20 : 0);
        AnimatorsUtils.animateX(consoleContainer, offset, MENU_SLIDE_MILLIS,
                open ? new AccelerateInterpolator() : new OvershootInterpolator());
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
                .getColor(open ? R.color.white_transparent50 : R.color.colorPrimaryDark);
        int colorFrom = getResources()
                .getColor(open ? R.color.colorPrimaryDark : R.color.white_transparent50);
        AnimatorsUtils.animateBackgroundTint(fullLayout, colorFrom, colorTo, MENU_COLOR_CHANGE_MILLIS);
    }
}
