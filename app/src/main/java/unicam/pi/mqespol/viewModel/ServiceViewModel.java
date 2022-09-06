package unicam.pi.mqespol.viewModel;


import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import unicam.pi.mqespol.mqtt.MqttAppService;

public class ServiceViewModel extends ViewModel {

    private static final String TAG="ServiceViewModel";

    private final MutableLiveData<Boolean> isServiceUp = new MutableLiveData<>();
    private final MutableLiveData<MqttAppService.MyServiceBinder> mBinder = new MutableLiveData<>();


    ServiceConnection myServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.d(TAG,"onService Connected: connected to service");
            MqttAppService.MyServiceBinder myServiceBinder= (MqttAppService.MyServiceBinder) service;
            mBinder.postValue(myServiceBinder); // BOUNDED SERVICE
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.d(TAG,"onService Connected: No connected to service");
                mBinder.postValue(null); //NO BOUNDED SERVICE
        }
    };

    public LiveData<Boolean> getStateServer(){
        return isServiceUp;
    }

    public LiveData<MqttAppService.MyServiceBinder> getBinder(){
        return mBinder;
    }


    public ServiceConnection getMyServiceConnection(){
        return myServiceConnection;
    }
    public void setStateServer(Boolean state){
        isServiceUp.setValue(state);
    }

}
