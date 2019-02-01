package com.android.gjprojection.roveedoll.features.tutorial;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.gjprojection.roveedoll.R;

import java.util.List;

public class Adapter extends RecyclerView.Adapter<Adapter.myViewHolder> {

    private Context mContext;
    private List<Tutorial> mData;

    public Adapter(Context mContext, List<Tutorial> mData) {
        this.mContext = mContext;
        this.mData = mData;
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View v = inflater.inflate(R.layout.card_item, viewGroup, false);
        return new myViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull myViewHolder myViewHolder, int i) {
        myViewHolder.background.setImageResource(mData.get(i).getBackground());
        myViewHolder.title.setText(mData.get(i).getCard_title());
        final int positon = i;
        myViewHolder.background.setOnClickListener(v -> {
            Intent playVideo = new Intent(mContext, VideoPlayer.class);
            playVideo.putExtra("path", mData.get(positon).getVideo());
            playVideo.putExtra("title", mData.get(positon).getCard_title());
            mContext.startActivity(playVideo);
        });
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class myViewHolder extends RecyclerView.ViewHolder {
        ImageView background;
        TextView title;

        public myViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.card_title);
            background = itemView.findViewById(R.id.card_background);
        }
    }
}
