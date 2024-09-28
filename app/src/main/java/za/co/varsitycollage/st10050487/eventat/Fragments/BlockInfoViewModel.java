package za.co.varsitycollage.st10050487.eventat.Fragments;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class BlockInfoViewModel extends ViewModel {
    private final MutableLiveData<String> blockInfo = new MutableLiveData<>();

    public void setBlockInfo(String info) {
        blockInfo.setValue(info);
    }

    public LiveData<String> getBlockInfo() {
        return blockInfo;
    }
}