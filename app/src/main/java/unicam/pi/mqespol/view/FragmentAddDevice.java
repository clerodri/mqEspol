package unicam.pi.mqespol.view;

import android.content.Intent;
import android.net.wifi.ScanResult;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.List;
import java.util.Objects;

import unicam.pi.mqespol.R;
import unicam.pi.mqespol.databinding.FragmentAddDeviceBinding;
import unicam.pi.mqespol.util.Util;
import unicam.pi.mqespol.util.WifiReciber;
import unicam.pi.mqespol.view.adapters.SsidAdapter;
import unicam.pi.mqespol.viewModel.DeviceViewModel;

public class FragmentAddDevice extends Fragment {

    private SsidAdapter ssidAdapter;
    private FragmentAddDeviceBinding binding;
    private DeviceViewModel deviceViewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        Log.d("PEPA","FRAGMENT2 ON CREATE");
        MainActivity.wifiReciever = new WifiReciber();
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        Log.d("PEPA","FRAGMENT 2 ON VIEW CREATED");
        super.onViewCreated(view, savedInstanceState);
        initRecyclerView();
        deviceViewModel.setHostPotOff();
        binding.btnWifi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent panelIntent = new Intent(Settings.Panel.ACTION_INTERNET_CONNECTIVITY);
                startActivity(panelIntent);
            }
        });

        binding.btnScan.setOnClickListener(v -> {
            if (MainActivity.wifiManager.isWifiEnabled()) {
                toast("Scanning Networks...");
                deviceViewModel.scanWifi(MainActivity.wifiManager, MainActivity.wifiReciever, requireActivity());
            } else {
                toast("WiFi is OFF.. Turn it ON for Scanning");
            }
        });
        deviceViewModel.get().observe(getViewLifecycleOwner(), new Observer<List<ScanResult>>() {
            @Override
            public void onChanged(List<ScanResult> scanResults) {
                ssidAdapter.setSSID(scanResults);
            }
        });



        binding.btnAdd.setOnClickListener(v -> {
            if (Util.isValid(binding.lblNameDevice.getText().toString(), ssidAdapter.getItemSelected())) {
                if (deviceViewModel.sendDevice(ssidAdapter.getItemSelected(), binding.lblNameDevice.getText().toString())) {
                    toast("Device Connected");
                    Navigation.findNavController(view).navigate(R.id.action_fragmentAddDevice_to_fragmentListDevice);
                } else {
                    toast("Topic Device Wrong Format, Not Added");
                }
            } else {
                toast("Please Type a Name and Select a Network");
            }
        });
    }


    public void initRecyclerView() {
        deviceViewModel = new ViewModelProvider(this).get(DeviceViewModel.class);
        binding.recyclerViewSsid.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recyclerViewSsid.setHasFixedSize(true);
        ssidAdapter = new SsidAdapter(deviceViewModel.getListWifi());
        binding.recyclerViewSsid.setAdapter(ssidAdapter);
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentAddDeviceBinding.inflate(getLayoutInflater());
        Log.d("PEPA","FRAGMENT2 ON CREATE VIEW");
        return binding.getRoot();
    }

    public void toast(String msg) {
        Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onStart() {
        Log.d("PEPA","FRAGMENT2 ON START");
        super.onStart();
    }

    @Override
    public void onResume() {
        Log.d("PEPA","FRAGMENT2 ON RESUMEN");
        super.onResume();
    }

    @Override
    public void onStop() {
        Log.d("PEPA","FRAGMENT2 ON STOP");
        super.onStop();
    }

    @Override
    public void onPause() {
        Log.d("PEPA","FRAGMENT2 ON PAUSE");
        super.onPause();
    }

    @Override
    public void onDestroy() {
        Log.d("PEPA","FRAGMENT2 ON DESTROY");
        super.onDestroy();
    }

    @Override
    public void onDestroyView() {
        Log.d("PEPA","FRAGMENT2 ON DESTROY VIEW");
        super.onDestroyView();
    }
}