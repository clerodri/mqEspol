package unicam.pi.mqespol.view;


import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Objects;

import unicam.pi.mqespol.databinding.FragmentListDeviceBinding;
import unicam.pi.mqespol.model.Device;
import unicam.pi.mqespol.util.Util;
import unicam.pi.mqespol.view.adapters.DeviceAdapter;
import unicam.pi.mqespol.viewModel.DeviceViewModel;
import unicam.pi.mqespol.viewModel.ServiceViewModel;



public class FragmentListDevice extends Fragment {

    private DeviceViewModel deviceViewModel;
    private FragmentListDeviceBinding binding;
    private DeviceAdapter deviceAdapter;
    MqttAndroidClient mqttAndroidClient;
    private ServiceViewModel mServiceViewModel;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentListDeviceBinding.inflate(getLayoutInflater());
        Log.d("PEPA", "FRAGMENT ON CREATE VIEW");
        return binding.getRoot();

    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initResources();
        Log.d("PEPA", "FRAGMENT ON VIEW CREATED");
        Log.d("TAG", "INTERNET ON: " + MainActivity.wifiManager.isWifiEnabled());
        //
//        if (!WifiFuctions.isHostPotOn) {
//            //     MainActivity.wifiManager.setWifiEnabled(false);
//            deviceViewModel.setHotspotOn(getActivity());  //ACTIVAR EL HOSTPOT OnHostPot Wifi
//        }



        binding.btnCon.setOnClickListener(v -> {
            if(mServiceViewModel.getBinder()!=null) {
                if (MainActivity.mService.getStatusService()) {
                    connecClient();
                } else {
                    toast("Error, Service MQTT OFF");
                }
            }
        });
        binding.btnPub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               publish("OSIRIS");
            }
        });


        deviceViewModel.getAllDevices().observe(getViewLifecycleOwner(), new Observer<List<Device>>() {
                    @Override
                    public void onChanged(List<Device> devices) {
                        deviceAdapter.submitList(devices);
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
        mServiceViewModel = new ViewModelProvider(this).get(ServiceViewModel.class);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recyclerView.setHasFixedSize(true);
        deviceAdapter = new DeviceAdapter();
        binding.recyclerView.setAdapter(deviceAdapter);
        MainActivity.wifiManager = (WifiManager) requireActivity().getSystemService(Context.WIFI_SERVICE);
    }



    public void connecClient() {
        try {
            IMqttToken token = mqttAndroidClient.connect();
            token.setActionCallback(new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    Toast.makeText(getContext(), "Connected", Toast.LENGTH_SHORT).show();
                    subcription();
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

    public void publish(String topic){
        if(MainActivity.mService.getStatusService()) {
            String msj = binding.valorPub.getText().toString();
            byte[] encode;
            try {
                encode = msj.getBytes(StandardCharsets.UTF_8);
                MqttMessage message = new MqttMessage(encode);
                mqttAndroidClient.publish(topic, message);
                mqttAndroidClient.setCallback(new MqttCallback() {
                    @Override
                    public void connectionLost(Throwable cause) {

                    }

                    @Override
                    public void messageArrived(String topic, MqttMessage message) throws Exception {
                        Log.d("TAG", "Mensaje recibido callback: " + message);
                       deviceViewModel.getNewDevice(topic,message.toString());

                    }

                    @Override
                    public void deliveryComplete(IMqttDeliveryToken token) {

                    }
                });
            } catch (MqttException e) {
                e.printStackTrace();
            }
        }else{
            Toast.makeText(getContext(),"Error, Service MQTT OFF",Toast.LENGTH_SHORT).show();
        }
    }


    public void subcription() {
        try {
            IMqttToken subToken = mqttAndroidClient.subscribe("OSIRIS", 1);
            subToken.setActionCallback(new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    toast("Subcription Successfully");
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken,
                                      Throwable exception) {
                    toast("Subcription Failed");
                }
            });
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    public void toast(String msg) {
        Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onStart() {
        Log.d("PEPA", "FRAGMENT ON START");
        super.onStart();
    }

    @Override
    public void onResume() {
        Log.d("PEPA", "FRAGMENT ON RESUMEN");
        super.onResume();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        Log.d("PEPA", "FRAGMENT ON CREATE");
        mqttAndroidClient = new MqttAndroidClient(getContext(), Util.TCP + Util.getBrokerURL(requireContext()) + ":1883", Util.CLIENT_ID);
        try {
            mqttAndroidClient.connect();
        } catch (MqttException e) {
            e.printStackTrace();
        }
        Log.e("TAG", "IP INIT SOURCE CLIENTE: "+Util.getBrokerURL(requireContext()));
        super.onCreate(savedInstanceState);
    }
//
//    @Override
//    public void onSaveInstanceState(@NonNull Bundle outState) {
//
//        super.onSaveInstanceState(outState);
//    }

    @Override
    public void onPause() {
        Log.d("PEPA", "FRAGMENT ON PAUSE");
        super.onPause();
    }

    @Override
    public void onStop() {
        Log.d("PEPA", "FRAGMENT ON STOP");
        super.onStop();
    }

    @Override
    public void onDestroy() {
        Log.d("PEPA", "FRAGMENT ON DESTROY");
        super.onDestroy();

    }

    @Override
    public void onDestroyView() {
        Log.d("PEPA", "FRAGMENT ON DESTROY VIEW");
        super.onDestroyView();
    }
}