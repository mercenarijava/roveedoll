package com.android.gjprojection.roveedoll.utils;

import android.os.AsyncTask;
import android.support.annotation.Nullable;

public class CommonUtils {

    public static class GenericAsyncTask extends AsyncTask<Void, Void, Void> {
        @Nullable
        Runnable backgroundRunnable;
        @Nullable
        Runnable postExecuteRunnable;

        public GenericAsyncTask(
                @Nullable Runnable backgroundRunnable) {
            this.backgroundRunnable = backgroundRunnable;
        }

        public GenericAsyncTask(
                @Nullable Runnable backgroundRunnable,
                @Nullable Runnable postExecuteRunnable) {
            this.backgroundRunnable = backgroundRunnable;
            this.postExecuteRunnable = postExecuteRunnable;
        }

        @Override
        protected Void doInBackground(
                Void... voids) {
            if (backgroundRunnable != null)
                backgroundRunnable.run();
            return null;
        }

        @Override
        protected void onPostExecute(
                Void aVoid) {
            super.onPostExecute(aVoid);
            if (postExecuteRunnable != null)
                postExecuteRunnable.run();
        }
    }

}
