package com.android.gjprojection.roveedoll;

import android.view.View;


public interface UIComponent {
    public void connectViews(View... views);

    public void connectListeners();
}
