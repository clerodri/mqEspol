package unicam.pi.mqespol.viewModel;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttMessageListener;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttPersistenceException;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import unicam.pi.mqespol.model.Device;
import unicam.pi.mqespol.model.DeviceRepository;
import unicam.pi.mqespol.mqtt.MqttAppService;
import unicam.pi.mqespol.util.Util;
import unicam.pi.mqespol.util.WifiFuctions;
import unicam.pi.mqespol.view.FragmentListDevice;
import unicam.pi.mqespol.view.MainActivity;


/**
 * Se encarga de agregar un device a la base de datos cuando el AddDevice le envia un device para guardar.
 */

public class DeviceViewModel extends AndroidViewModel  {

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

    public void insert(Device device) {
        repository.insert(device);
    }

    public void update(Device device) {
        Log.d("VIEWMODEL", "UPDATE VIEWMODEL: ");
        repository.update(device);
    }

    public void delete(Device device) {
        repository.delete(device);
    }

    public void deleteAllDevices() {
        repository.deleteAllDevices();
    }

    public LiveData<List<Device>> getAllDevices() {
        return allDevices;
    }

    public Boolean sendDevice(int position, String nameDevice) {
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
    public void getNewDevice(String topic, String message) {
        Device newDevice = null;
        for (Device device : repository.getAllData()) {
            if (device.getTopic().equals(topic)) {
                newDevice = new Device(device.getName(), topic, message);
                newDevice.setId(device.getId());
            }
        }
       update(newDevice);
    }


    public List<ScanResult> getListWifi() {
        return listWifi;
    }

    public LiveData<List<ScanResult>> get() {
        return listMutableLiveData;
    }

//    public void setHotspotOn(FragmentActivity fragmentActivity) {
//        WifiFuctions.createHostpot(fragmentActivity, MainActivity.wifiManager);
//    }
//
//    public void setHostPotOff() {
//        WifiFuctions.setHostPotOff(getApplication().getApplicationContext());
//    }
    public void sendData(){

    }



    public void scanWifi(WifiManager wifiManager, BroadcastReceiver wifiReciever, FragmentActivity fragmentActivity) {
        fragmentActivity.registerReceiver(wifiReciever, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
        if (ActivityCompat.checkSelfPermission(getApplication().getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(fragmentActivity, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 0);
        } else {
            wifiManager.startScan();
            listWifi = wifiManager.getScanResults();
            listMutableLiveData.setValue(listWifi);
        }
    }

}
