package com.oss.proteger.ui.verify_apps;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class VerifyAppsModel extends ViewModel {

    private MutableLiveData<String> mText;

    public VerifyAppsModel() {
        mText = new MutableLiveData<>();
    }

    public LiveData<String> getText() {
        return mText;
    }
}