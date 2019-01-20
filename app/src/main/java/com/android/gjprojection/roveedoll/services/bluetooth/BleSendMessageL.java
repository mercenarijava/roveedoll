package com.android.gjprojection.roveedoll.services.bluetooth;

import com.android.gjprojection.roveedoll.utils.Constants;

public class BleSendMessageL implements BleWrittable {
    private Integer speedMotorSx;
    private Integer speedMotorDx;

    public BleSendMessageL(
            final int speedMotorSx,
            final int speedMotorDx) {
        this.speedMotorSx = speedMotorSx;
        this.speedMotorDx = speedMotorDx;
    }

    @Override
    public String getJSON() {
        return String.valueOf(speedMotorSx) +
                Constants.MESSAGE_SEPARATOR +
                String.valueOf(speedMotorDx);
    }
}
