package com.android.gjprojection.roveedoll.services.bluetooth;

import android.support.annotation.Nullable;

import com.android.gjprojection.roveedoll.utils.JacksonUtils;

public class BleSendMessageL implements WritableJSON{
    private Integer speedMotorSx;
    private Integer speedMotorDx;

    public BleSendMessageL(
            final int speedMotorSx,
            final int speedMotorDx) {
        this.speedMotorSx = speedMotorSx;
        this.speedMotorDx = speedMotorDx;
    }

    /**
     * Gets he same object's string conversion
     *
     * @return JSON string
     */
    @Override
    @Nullable
    public String getJSON() {
        return JacksonUtils.write(this);
    }
}
