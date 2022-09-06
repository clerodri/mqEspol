package unicam.pi.mqespol.viewModel;

import android.Manifest;
import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.util.ArrayList;

import java.util.List;
import java.util.Locale;

import unicam.pi.mqespol.model.Device;
import unicam.pi.mqespol.model.DeviceRepository;


public class DeviceViewModel extends AndroidViewModel {

    private final DeviceRepository repository;
    private final LiveData<List<Device>> allDevices;
    private List<ScanResult> listWifi;
    private final MutableLiveData<List<ScanResult>> listMutableLiveData = new MutableLiveData<>();


    public DeviceViewModel(@NonNull Application application) {
        super(application);
        repository = new DeviceRepository(application);
        allDevices = repository.getAllDevices();
        listWifi = new ArrayList<>();
        listMutableLiveData.setValue(listWifi);
    }

    public void insert(Device device){
        repository.insert(device);
    }
    public void update(Device device){
        repository.update(device);
    }
    public void delete(Device device){
        repository.delete(device);
    }
    public void deleteAllDevices(){
        repository.deleteAllDevices();
    }
    public List<ScanResult> getListWifi() {
        return listWifi;
    }
    public LiveData<List<ScanResult>> get() {
        return listMutableLiveData;
    }
    public LiveData<List<Device>> getAllDevices(){
        return allDevices;
    }



    public Boolean addDevice(int position, String nameDevice) {
        String topic = listWifi.get(position).SSID;
        // topic = Util.getFormated(topic);
        if (topic != null) {
            String name = nameDevice.toUpperCase(Locale.ROOT);
            Device newDevice = new Device(name, topic, "10");
            insert(newDevice);
            return true;
        } else {
            return false;
        }
    }
    public void updateDevice(String topic, String message) {
        Device newDevice = null;
        for (Device device : repository.getAllData()) {
            if (device.getTopic().equals(topic)) {
                newDevice = new Device(device.getName(), topic, message);
                newDevice.setId(device.getId());
            }
        }
        update(newDevice);
    }

    public void loadSubcription(MqttAndroidClient mqttAndroidClient,  List<Device> devices){
        for(Device d: devices){
            Log.i("DIS", d.getName());
            if(mqttAndroidClient.isConnected()){
                Log.i("TOPIC:", d.getTopic());
                subscribe(mqttAndroidClient,d.getTopic());
            }
        }

    }
    public void subscribe(MqttAndroidClient mqttAndroidClient,String topic){
        try {
            IMqttToken subToken = mqttAndroidClient.subscribe(topic, 1);
            subToken.setActionCallback(new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    mqttAndroidClient.setCallback(new MqttCallback() {
                        @Override
                        public void connectionLost(Throwable cause) {

                        }

                        @Override
                        public void messageArrived(String topic, MqttMessage message) throws Exception {
                            updateDevice(topic, message.toString());
                        }

                        @Override
                        public void deliveryComplete(IMqttDeliveryToken token) {

                        }
                    });
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken,
                                      Throwable exception) {
                }
            });
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }




    public void scanWifi(WifiManager wifiManager, BroadcastReceiver wifiReciever, FragmentActivity fragmentActivity) {
        fragmentActivity.registerReceiver(wifiReciever, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
        if (ActivityCompat.checkSelfPermission(getApplication().getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(fragmentActivity, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 0);
        } else {
            Log.d("WIFI","SSTART SACN0");
            wifiManager.startScan();
            listWifi = wifiManager.getScanResults();
            Log.d("WIFI","list  "+listWifi);
            listMutableLiveData.setValue(listWifi);
        }
    }

//    public void sendData(String name, String topic, String message){
//        Device newDevice = null;
//        for (Device device : repository.getAllData()) {
//            if (device.getTopic().equals(topic) && device.getName().equals(name) ) {
//                newDevice = new Device(device.getName(), topic, message);
//                newDevice.setId(device.getId());
//            }
//        }
//        update(newDevice);
//    }




}
