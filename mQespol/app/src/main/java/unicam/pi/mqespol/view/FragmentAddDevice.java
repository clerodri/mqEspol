package unicam.pi.mqespol.view;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.ScanResult;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import android.view.View;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import android.widget.Toast;

import java.util.List;

import unicam.pi.mqespol.R;
import unicam.pi.mqespol.databinding.FragmentAddDeviceBinding;
import unicam.pi.mqespol.util.Util;
import unicam.pi.mqespol.util.WifiReciber;
import unicam.pi.mqespol.view.Adapters.SsidAdapter;
import unicam.pi.mqespol.viewModel.DeviceViewModel;


public class FragmentAddDevice extends Fragment {


    private SsidAdapter ssidAdapter;
    private FragmentAddDeviceBinding binding;
    private DeviceViewModel deviceViewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {

        MainActivity.wifiReciever = new WifiReciber();
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.e("TAG", " FRAGMENT ADDDEVICE");
        initRecyclerView();

        binding.btnWifi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.wifiManager.setWifiEnabled(true);
                toast("Turning On WiFi...Wait..");
            }
        });
        binding.btnScan.setOnClickListener(new View.OnClickListener() {

                   @Override
                   public void onClick(View v) {
                       if (MainActivity.wifiManager.isWifiEnabled()) {
                           toast("Scanning Networks...");
                           deviceViewModel.scanWifi(MainActivity.wifiManager, MainActivity.wifiReciever, requireActivity());
                       } else {
                           toast("WiFi is OFF.. Turn it ON for Scanning");
                       }
                   }
               }
        );
        deviceViewModel.get().observe(getViewLifecycleOwner(), new Observer<List<ScanResult>>() {
            @Override
            public void onChanged(List<ScanResult> scanResults) {
                ssidAdapter.setSSID(scanResults);
            }
        });

        binding.btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Util.isValid(binding.lblNameDevice.getText().toString(), ssidAdapter.getItemSelected())) {
                    if (deviceViewModel.addDevice(ssidAdapter.getItemSelected(), binding.lblNameDevice.getText().toString())) {
                        toast("Device Connected");
                        Navigation.findNavController(view).navigate(R.id.action_fragmentAddDevice_to_fragmentListDevice);
                    } else {
                        toast("Topic Device Wrong Format, Not Added");
                    }
                } else {
                    toast("Please Type a Name and Select a Network");
                }
            }
        });
    }

    @Override
    public void onPause() {
        MainActivity.wifiManager.setWifiEnabled(false);
        Log.e("TAG", "on pause FRAGMENT ADD DEVICE");
        super.onPause();
    }

    public void initRecyclerView() {
        deviceViewModel = new ViewModelProvider(this).get(DeviceViewModel.class);
        binding.recyclerViewSsid.setLayoutManager(new LinearLayoutManager(getContext()));
       // binding.recyclerViewSsid.setHasFixedSize(true);
        ssidAdapter = new SsidAdapter(deviceViewModel.getListWifi());
        binding.recyclerViewSsid.setAdapter(ssidAdapter);
    }


    @Override
    public void onDestroy() {
        Log.e("TAG", "on DESTROY FRAGMENT ADDDEVICE");
        super.onDestroy();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentAddDeviceBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    class WifiReceiever extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
        }
    }

    public void toast(String msg) {
        Toast.makeText(getContext().getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
    }


}
