package com.bios.mv.fletesmv_v1.ui.logout;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class LogoutViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public LogoutViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("¡Muchas gracias por elegir FletesMV!");
    }

    public LiveData<String> getText() {
        return mText;
    }
}