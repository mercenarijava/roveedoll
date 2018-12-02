package com.android.gjprojection.roveedoll.features.free_line;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.android.gjprojection.roveedoll.features.free_line.views.FreeLineView;

public class FreeLineViewModel extends ViewModel {
    private MutableLiveData<Integer> linesCount;
    private MutableLiveData<FreeLineView.PointScaled> pointAdd;
    private MutableLiveData<FreeLineView.PointScaled> pointDeleted;

    public MutableLiveData<Integer> getLinesCount() {
        if (this.linesCount == null) {
            this.linesCount = new MutableLiveData<>();
            this.linesCount.setValue(0);
        }
        return this.linesCount;
    }

    public MutableLiveData<FreeLineView.PointScaled> getPointAdd() {
        if (this.pointAdd == null) {
            this.pointAdd = new MutableLiveData<>();
        }
        return this.pointAdd;
    }

    public MutableLiveData<FreeLineView.PointScaled> getPointDeleted() {
        if (this.pointDeleted == null) {
            this.pointDeleted = new MutableLiveData<>();
        }
        return this.pointDeleted;
    }

}
