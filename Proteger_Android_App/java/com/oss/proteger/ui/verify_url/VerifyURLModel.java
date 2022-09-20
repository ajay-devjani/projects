package com.oss.proteger.ui.verify_url;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class VerifyURLModel extends ViewModel {

    private MutableLiveData<String> mText;

    public VerifyURLModel() {
        mText = new MutableLiveData<>();
    }

    public LiveData<String> getText() {
        return mText;
    }
}