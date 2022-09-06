package unicam.pi.mqespol.view;

import android.content.Context;
import android.net.wifi.WifiManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.net.SocketException;
import java.nio.charset.StandardCharsets;
import java.util.List;

import unicam.pi.mqespol.databinding.FragmentListDeviceBinding;
import unicam.pi.mqespol.model.Device;
import unicam.pi.mqespol.util.Util;
import unicam.pi.mqespol.view.Adapters.DeviceAdapter;
import unicam.pi.mqespol.viewModel.DeviceViewModel;


public class FragmentListDevice extends Fragment {


    private DeviceViewModel deviceViewModel;
    private FragmentListDeviceBinding binding;
    private DeviceAdapter deviceAdapter;
    MqttAndroidClient mqttAndroidClient;
    public static final String TAG = "PEPA";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {

        if (MainActivity.wifiManager.isWifiEnabled()) {
        //     MainActivity.wifiManager.setWifiEnabled(false);
       //     toast("Wifi Off");
        }
        mqttAndroidClient = new MqttAndroidClient(getContext(), Util.TCP + "192.168.43.1:1883", Util.CLIENT_ID); //CREAR CLIENTE MQTTANDROID
        super.onCreate(savedInstanceState);
    }



    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.e("TAG", "FRAGMENT LISTDEVICE");
        initResources();
        binding.btnRec.setOnClickListener(v -> {
            if(!mqttAndroidClient.isConnected()) {connecClient(deviceAdapter.getCurrentList());}
        });

        deviceViewModel.getAllDevices().observe(getViewLifecycleOwner(), new Observer<List<Device>>() {
            @Override
            public void onChanged(List<Device> devices) {
                deviceAdapter.submitList(devices);
                if (devices != null) {
                    connecClient(devices);    //INICIALIZA LA CONEXION  CON EL BROKER LOCAL
                }
            }
        });

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                deviceViewModel.delete(deviceAdapter.getDeviceAt(viewHolder.getAdapterPosition()));
                toast("Device Deleted");
            }
        }).attachToRecyclerView(binding.recyclerView);

    }


    public void initResources() {
        deviceViewModel = new ViewModelProvider(requireActivity()).get(DeviceViewModel.class);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        // binding.recyclerView.setHasFixedSize(true);
        deviceAdapter = new DeviceAdapter();
        binding.recyclerView.setAdapter(deviceAdapter);

    }


    public void connecClient(List<Device> devices) {
        try {
            IMqttToken token = mqttAndroidClient.connect();
            token.setActionCallback(new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    deviceViewModel.loadSubcription(mqttAndroidClient,devices);   // SE SUBSCRIBE A LA LISTA DE DEVICES CONECTADOS.
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    Toast.makeText(getContext(), "Connection Failed", Toast.LENGTH_SHORT).show();

                }
            });
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentListDeviceBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    public void toast(String msg) {
        Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
    }
}