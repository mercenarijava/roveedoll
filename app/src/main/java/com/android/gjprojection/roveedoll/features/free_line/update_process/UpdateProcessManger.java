package com.android.gjprojection.roveedoll.features.free_line.update_process;

import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import com.android.gjprojection.roveedoll.features.free_line.views.FreeLineView;

import java.util.ArrayList;

public class UpdateProcessManger implements Runnable {
    enum Steps {
        FILE_CREATE_START,
        FILE_CREATE_LOADING,
        FILE_CREATE_FINISH,
        BLUETOOTH_UPLOAD
    }

    private MutableLiveData<UpdateProcessManger> processMangerMutableLiveData;
    private Steps currentStep = Steps.FILE_CREATE_START;

    private UpdateProcessManger() {
        this.processMangerMutableLiveData = new MutableLiveData<>();
        this.processMangerMutableLiveData.setValue(this);
        run();
    }

    @Override
    public void run() {

    }


    public static MutableLiveData<UpdateProcessManger> init(
            @NonNull ArrayList<FreeLineView.PointScaled> data) {
        return new UpdateProcessManger().processMangerMutableLiveData;
    }
}
