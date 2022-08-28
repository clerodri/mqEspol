package unicam.pi.mqespol.viewModel;

import android.app.Application;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import unicam.pi.mqespol.model.Device;
import unicam.pi.mqespol.model.DeviceRepository;
import unicam.pi.mqespol.util.WifiFuctions;
import unicam.pi.mqespol.view.MainActivity;


/**
 * Se encarga de agregar un device a la base de datos cuando el AddDevice le envia un device para guardar.
 */

public class DeviceViewModel extends AndroidViewModel {
    //private MutableLiveData<String> mensaje;
    private final DeviceRepository repository;
    private final LiveData<List<Device>> allDevices;
    private MutableLiveData<Device> device;
    private List<ScanResult> listWifi;
    public DeviceViewModel(@NonNull Application application) {
        super(application);
        repository = new DeviceRepository(application);
        allDevices = repository.getAllDevices();
        device =new MutableLiveData<>();
        listWifi=new ArrayList<>();
      //  mensaje=new MutableLiveData<>();
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

    public LiveData<List<Device>> getAllDevices(){
        return allDevices;
    }

    public Boolean sendDevice(int position,String nameDevice){
        String topic = listWifi.get(position).SSID;
        // topic = util.getFormated(topic);
        if (topic != null) {
            String name = nameDevice.toUpperCase(Locale.ROOT);
            Device newDevice = new Device(name, topic, "10");
            device.setValue(newDevice);
            insert(newDevice);
            return true;
        }else{
             return false;
        }
    }
//    public MutableLiveData<String> getMensaje(){return  mensaje;}

    public MutableLiveData<Device>  getDevice(){return device;}

    public List<ScanResult> getListWifi(FragmentActivity fragmentActivity){
        listWifi=WifiFuctions.getListNetworks(MainActivity.wifiManager,fragmentActivity);
        return listWifi;
    }
    public void setHotspotOn(FragmentActivity fragmentActivity) {
      WifiFuctions.createHostpot(fragmentActivity,MainActivity.wifiManager);
    }

}
