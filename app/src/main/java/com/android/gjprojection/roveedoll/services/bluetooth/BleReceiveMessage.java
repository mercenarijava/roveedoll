package com.android.gjprojection.roveedoll.services.bluetooth;


public class BleReceiveMessage {
    private static final int DISTANCE_COLLISION_DETECTED_CM = 10;

    private int obstacleDistanceCM;
    private long isLineViewUploadSuccessfully = 0; // if > 0 is success, < 0 is failure, 0 is nothing

    public BleReceiveMessage(int obstacleDistanceCM, long isLineViewUploadSuccessfully) {
        this.obstacleDistanceCM = obstacleDistanceCM;
        this.isLineViewUploadSuccessfully = isLineViewUploadSuccessfully;
    }

    public int getObstacleDistanceCM() {
        return obstacleDistanceCM;
    }

    public long getLineId(){
        return isLineViewUploadSuccessfully;
    }

    public boolean isUploadProcessSuccess(final long success) {
        return isLineViewUploadSuccessfully == success;
    }

    public boolean hasCollided() {
        return this.obstacleDistanceCM <= DISTANCE_COLLISION_DETECTED_CM;
    }
}
