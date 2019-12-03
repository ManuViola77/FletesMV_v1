package com.bios.mv.fletesmv_v1.ui.mapa;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class MapaViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public MapaViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("Mapa de Transportes (Orígenes)");
    }

    public LiveData<String> getText() {
        return mText;
    }
}