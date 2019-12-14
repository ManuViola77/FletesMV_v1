package com.bios.mv.fletesmv_v1.ui.transporte;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class TransporteViewModel  extends ViewModel {

    private MutableLiveData<String> mText;

    public TransporteViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("Traslados");
    }

    public LiveData<String> getText() {
        return mText;
    }
}
