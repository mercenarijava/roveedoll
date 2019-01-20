package com.android.gjprojection.roveedoll.services.bluetooth;

import com.android.gjprojection.roveedoll.utils.Constants;

public class BleSendMessageV implements BleWrittable {

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

    @Override
    public String getJSON() {
        return String.valueOf(lineId) +
                Constants.MESSAGE_SEPARATOR +
                String.valueOf(speed) +
                Constants.MESSAGE_SEPARATOR +
                String.valueOf(rotation) +
                Constants.MESSAGE_SEPARATOR +
                String.valueOf(travel);
    }
}
