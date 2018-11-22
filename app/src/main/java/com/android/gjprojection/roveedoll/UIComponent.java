package com.android.gjprojection.roveedoll;

import android.support.annotation.Nullable;
import android.view.View;

/**
 * Created by recosta32 on 22/11/2018.
 */

public interface UIComponent {
    public void connectViews(@Nullable View... views);
    public void connectListeners();
    public void setActionBar();
}
