package com.android.gjprojection.roveedoll.features.free_line;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.gjprojection.roveedoll.R;

import java.util.ArrayList;


public class ConsoleRecyclerViewAdapter extends RecyclerView.Adapter<ConsoleRecyclerViewAdapter.ViewHolder> {
    private static final String CONSOLE_RAW_PREFIX = "C:\\System> ";
    private final ArrayList<String> data;

    static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        TextView mTextView;

        ViewHolder(TextView v) {
            super(v);
            mTextView = v;
        }
    }

    ConsoleRecyclerViewAdapter() {
        this.data = new ArrayList<>();
    }

    void add(@NonNull String raw) {
        this.data.add(raw);
        notifyItemInserted(data.size() - 1);
    }

    @Override
    public ConsoleRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                                    int viewType) {
        // create a new view
        TextView v = (TextView) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.element_console, parent, false);
        ConsoleRecyclerViewAdapter.ViewHolder vh = new ConsoleRecyclerViewAdapter.ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ConsoleRecyclerViewAdapter.ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        final String s = CONSOLE_RAW_PREFIX + data.get(position);
        holder.mTextView.setText(s);

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return data.size();
    }
}