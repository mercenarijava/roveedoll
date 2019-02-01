package com.android.gjprojection.roveedoll.features.tutorial;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.gjprojection.roveedoll.R;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class TutorialFragment extends Fragment {
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private List<Tutorial> tutorialList;
    private Context mContext;

    public TutorialFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_tutorial, container, false);

        mContext = getActivity();

        mRecyclerView = (RecyclerView) v.findViewById(R.id.tutorial_list);
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(mContext);
        mRecyclerView.setLayoutManager(mLayoutManager);

        // Add item on list
        tutorialList = new ArrayList<>();
        tutorialList.add(new Tutorial(R.drawable.progamed_mode, "Guida modalità Programmata", R.raw.vid_1));
        tutorialList.add(new Tutorial(R.drawable.manual_mode, "Guida modalità Manuale", R.raw.vid_2));

        // specify an adapter
        mAdapter = new Adapter(mContext, tutorialList);
        mRecyclerView.setAdapter(mAdapter);

        return v;
    }

}
