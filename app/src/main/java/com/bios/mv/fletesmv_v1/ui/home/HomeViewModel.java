package com.bios.mv.fletesmv_v1.ui.home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class HomeViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public HomeViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("Texto por defecto");
    }

    public void setText(String texto) {
        mText.setValue(texto);
    }

    public LiveData<String> getText() {
        return mText;
    }
}