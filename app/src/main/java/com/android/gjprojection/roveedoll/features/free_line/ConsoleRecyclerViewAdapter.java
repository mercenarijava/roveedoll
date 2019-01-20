package com.android.gjprojection.roveedoll.features.free_line;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.gjprojection.roveedoll.R;

import java.util.ArrayList;


public class ConsoleRecyclerViewAdapter extends RecyclerView.Adapter<ConsoleRecyclerViewAdapter.ViewHolder> {
    private static final String CONSOLE_RAW_PREFIX = "C:\\System> ";
    private final ArrayList<ConsoleElement> data;

    static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        TextView mTextView;
        ProgressBar mProgress;

        ViewHolder(View v) {
            super(v);
            this.mTextView = v.findViewById(R.id.text);
            this.mProgress = v.findViewById(R.id.progress);
        }
    }

    ConsoleRecyclerViewAdapter() {
        this.data = new ArrayList<>();
    }

    void add(@NonNull String raw) {
        this.data.add(new ConsoleElement(raw, null, false));
        notifyItemInserted(data.size() - 1);
    }

    void add(@NonNull String raw,
             @Nullable Boolean success,
             final boolean progress) {
        this.data.add(new ConsoleElement(raw, success, progress));
        notifyItemInserted(data.size() - 1);
    }

    synchronized void upload(@NonNull String raw,
                             @Nullable Boolean success) {
        final boolean removed = removeLoadingData();
        if (removed || (success != null && success))
            add(raw, success, false);
    }

    private boolean removeLoadingData() {
        int i = -1;
        for (int j = 0; j < data.size() && i == -1; j++) {
            if (data.get(j).loadingOn) {
                i = j;
            }
        }
        if (i != -1) {
            data.remove(i);
            notifyItemRemoved(i);
        }
        return i != -1;
    }

    @Override
    public ConsoleRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                                    int viewType) {
        // create a new view
        ConstraintLayout v = (ConstraintLayout) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.element_console, parent, false);
        ConsoleRecyclerViewAdapter.ViewHolder vh = new ConsoleRecyclerViewAdapter.ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ConsoleRecyclerViewAdapter.ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        final ConsoleElement e = data.get(position);
        holder.mProgress.setVisibility(e.loadingOn ? View.VISIBLE : View.GONE);
        holder.mTextView.setTextColor(
                ContextCompat.getColor(
                        holder.itemView.getContext(),
                        e.success == null ? R.color.default_black :
                                e.success ? R.color.default_green : R.color.default_red
                )
        );
        final String s = CONSOLE_RAW_PREFIX + e.text;
        holder.mTextView.setText(s);

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return data.size();
    }

    private static class ConsoleElement {
        @NonNull
        String text;
        @Nullable
        Boolean success;
        boolean loadingOn;

        ConsoleElement(@NonNull String text,
                       @Nullable Boolean success,
                       boolean loadingOn) {
            this.text = text;
            this.success = success;
            this.loadingOn = loadingOn;
        }
    }
}