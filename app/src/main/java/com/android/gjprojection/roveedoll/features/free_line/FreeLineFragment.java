package com.android.gjprojection.roveedoll.features.free_line;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
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
import com.android.gjprojection.roveedoll.utils.AnimatorsUtils;
import com.android.gjprojection.roveedoll.utils.CommonUtils;

import java.util.ArrayList;

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

    @NonNull
    CommonUtils.GenericAsyncTask uploadTask = new CommonUtils.GenericAsyncTask(() -> {
        @NonNull final ArrayList<FreeLineView.PointScaled> points = view.getPoints();

        // TODO

    });

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
    public void onDetach() {
        super.onDetach();
        this.mainHandler.removeCallbacksAndMessages(null);
    }

    @Override
    public void connectViews(View... views) {
        this.viewModel = ViewModelProviders.of(FreeLineFragment.this).get(FreeLineViewModel.class);
        if (views.length == 0) return;
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

    }

    private void startUpdateProcess() {
        final boolean canStartUpdate = this.viewModel.getLinesCount() != null &&
                this.viewModel.getLinesCount().getValue() != null &&
                this.viewModel.getLinesCount().getValue() > 0;

        this.consoleAdapter.add(canStartUpdate ? "action update start" : "exception: nothing to update");

        if (canStartUpdate) {
            this.uploadTask.cancel(true);
            this.uploadTask.execute();
        }

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
