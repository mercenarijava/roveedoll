package com.android.gjprojection.roveedoll.services.bluetooth;

import android.support.annotation.Nullable;

import com.android.gjprojection.roveedoll.utils.JacksonUtils;

public class BleSendMessageV implements WritableJSON{

    public Long lineId;     // lineId
    private Integer speed;      // cm per second
    private Integer rotation;   // clockwise degree (-90 turns left by 90 degree, setting + will turn right)
    private Integer travel;     // distance in cm to travel

    public BleSendMessageV(
            final long lineId,
            final int speed,
            final int rotation,
            final int distance) {
        this.lineId = lineId;
        this.speed = speed;
        this.rotation = rotation;
        this.travel = distance;
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
