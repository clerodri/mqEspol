package unicam.pi.mqespol.view;


import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import java.util.List;
import unicam.pi.mqespol.databinding.FragmentListDeviceBinding;
import unicam.pi.mqespol.model.Device;
import unicam.pi.mqespol.util.WifiFuctions;
import unicam.pi.mqespol.view.adapters.DeviceAdapter;
import unicam.pi.mqespol.viewModel.ListDeviceViewModel;


public class FragmentListDevice extends Fragment {

    private ListDeviceViewModel listDeviceViewModel;
    private FragmentListDeviceBinding binding;
    private DeviceAdapter deviceAdapter;


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        savedInstanceState = getArguments();
        listDeviceViewModel = new ViewModelProvider(this).get(ListDeviceViewModel.class);
        if(!WifiFuctions.isHostPotOn){
            WifiFuctions.setWifiOff();
            listDeviceViewModel.setHotspotOn(getActivity());  //ACTIVAR EL HOSTPOT OnHostPot Wifi
        }

       // mqttConnection.mqttConnect(getContext());
        listDeviceViewModel.getData(savedInstanceState);
        listDeviceViewModel.getAllDevices().observe(getViewLifecycleOwner(), new Observer<List<Device>>() {
            @Override
            public void onChanged(List<Device> devices) {
                deviceAdapter.setDevices(devices);
            }
        });

        initRecyclerView();
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                    listDeviceViewModel.delete(deviceAdapter.getDeviceAt(viewHolder.getAdapterPosition()));
                    toast("Device Deleted");
            }
        }).attachToRecyclerView(binding.recyclerView);
    }

    public void initRecyclerView(){
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recyclerView.setHasFixedSize(true);
        deviceAdapter = new DeviceAdapter();
        binding.recyclerView.setAdapter(deviceAdapter);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentListDeviceBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    public void toast(String msg){
        Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
    }
}