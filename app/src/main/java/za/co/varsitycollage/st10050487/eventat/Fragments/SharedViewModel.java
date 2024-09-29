// SharedViewModel.java
package za.co.varsitycollage.st10050487.eventat.Fragments;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class SharedViewModel extends ViewModel {
    private final MutableLiveData<String> eventName = new MutableLiveData<>();

    public void setEventName(String name) {
        eventName.setValue(name);
    }

    public LiveData<String> getEventName() {
        return eventName;
    }
}