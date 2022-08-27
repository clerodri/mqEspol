package unicam.pi.mqespol.viewModel;

import android.app.Application;
import android.os.Bundle;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import java.util.List;
import unicam.pi.mqespol.model.Device;
import unicam.pi.mqespol.model.DeviceRepository;
import unicam.pi.mqespol.util.WifiFuctions;
import unicam.pi.mqespol.view.MainActivity;


/**
 * Se encarga de agregar un device a la base de datos cuando el AddDevice le envia un device para guardar.
 */

public class ListDeviceViewModel extends AndroidViewModel {

    private final DeviceRepository repository;
    private final LiveData<List<Device>> allDevices;
    public ListDeviceViewModel(@NonNull Application application) {
        super(application);
        repository = new DeviceRepository(application);
        allDevices = repository.getAllDevices();

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

    public void getData(Bundle bundle){
        if(bundle!=null) {
            System.out.println("bundle reciebe");
            Device device = (Device) bundle.getSerializable("device");
            insert(device);
            Log.d("TAG", "DEVICE CREATED, NAME: "+device.getName());
        }
    }


    public void setHotspotOn(FragmentActivity fragmentActivity) {
      WifiFuctions.createHostpot(fragmentActivity,MainActivity.wifiManager);
    }

}
