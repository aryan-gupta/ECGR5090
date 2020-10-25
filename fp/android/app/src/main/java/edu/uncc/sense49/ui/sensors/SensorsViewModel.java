package edu.uncc.sense49.ui.sensors;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class SensorsViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public SensorsViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is sensor fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}