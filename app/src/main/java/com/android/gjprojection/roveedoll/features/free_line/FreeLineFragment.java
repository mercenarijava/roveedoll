package com.android.gjprojection.roveedoll.features.free_line;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.android.gjprojection.roveedoll.R;
import com.android.gjprojection.roveedoll.UIComponent;

public class FreeLineFragment extends Fragment implements UIComponent, View.OnTouchListener {

    @NonNull
    LinearLayout actionsLayout;
    @NonNull
    LinearLayout actionsContent;

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
        final View v = inflater.inflate(R.layout.fragment_free_line, container, false);
        connectViews(v);
        connectListeners();
        return v;
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

    @Override
    public void connectViews(View... views) {
        if (views.length == 0) return;
        this.actionsLayout = views[0].findViewById(R.id.actions_layout);
        this.actionsContent = views[0].findViewById(R.id.actions_content);

    }

    @Override
    public void connectListeners() {
        setRightMenu();
    }

    private void setRightMenu() {
        this.actionsLayout.animate().translationZ(15);
        this.actionsLayout.setOnTouchListener(this);

    }


    private float startX;
    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        switch (motionEvent.getAction()){
            case MotionEvent.ACTION_DOWN:{
                startX = motionEvent.getX();
            }break;
            case MotionEvent.ACTION_MOVE:
            case MotionEvent.ACTION_UP:{
            }break;
        }
        return false;
    }
}
