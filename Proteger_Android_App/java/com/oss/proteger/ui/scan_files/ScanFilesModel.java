package com.oss.proteger.ui.scan_files;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ScanFilesModel extends ViewModel {

    private MutableLiveData<String> mText;

    public ScanFilesModel() {
        mText = new MutableLiveData<>();
    }

    public LiveData<String> getText() {
        return mText;
    }
}