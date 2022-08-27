package unicam.pi.mqespol.viewModel;



import android.app.Application;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.AndroidViewModel;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import unicam.pi.mqespol.model.Device;
import unicam.pi.mqespol.util.WifiFuctions;

/**
 * Agregar un device, obtiene la lista  del device y la actualiza
 * Escanea la red wifi mediante la clase estatica WifiFuctions y actualiza la lista.
 */

public class AddDeviceViewModel extends AndroidViewModel {


    List<ScanResult> wifiList = new ArrayList<>();

    public AddDeviceViewModel(@NonNull Application application) {
        super(application);
    }

    public void scannWifi(FragmentActivity fragmentActivity,WifiManager wifiManager) {
        wifiList = WifiFuctions.getListNetworks(wifiManager, fragmentActivity);
    }


    public List<ScanResult> getListWifi() {
        return this.wifiList;
    }


    public Bundle addDevice(int position, String nameDevice,FragmentActivity fragmentActivity) {
        Bundle args= new Bundle();
        String topic = wifiList.get(position).SSID;
       // topic = util.getFormated(topic);
        if (topic != null) {
            String name = nameDevice.toUpperCase(Locale.ROOT);
            args.putSerializable("device",new Device(name, topic, "10"));
            return args;
        } else{
            return null;
        }
    }
}
