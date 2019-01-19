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

    public static float getAngleInOrigins(
            final float xA,
            final float yA,
            final boolean directionUp) {
        final float xB = 10, yB = 0;

        final float aVectorXbVector = (xA * xB) + (yA * yB);
        final float aVectorSize = (float) Math.sqrt((xA * xA) + (yA * yA));
        final float bVectorSize = (float) Math.sqrt((xB * xB) + (yB * yB));
        final float cosAlpha = aVectorXbVector / (aVectorSize * bVectorSize);
        final float degrees = (float) ((((float) Math.acos(cosAlpha)) * 180) / Math.PI);
        return directionUp ? degrees : 180 + (180 - degrees);
    }

}
