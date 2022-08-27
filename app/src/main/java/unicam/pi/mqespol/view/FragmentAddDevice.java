package unicam.pi.mqespol.view;


import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import unicam.pi.mqespol.R;
import unicam.pi.mqespol.databinding.FragmentAddDeviceBinding;
import unicam.pi.mqespol.util.WifiFuctions;
import unicam.pi.mqespol.util.util;
import unicam.pi.mqespol.view.adapters.SsidAdapter;
import unicam.pi.mqespol.viewModel.AddDeviceViewModel;

public class FragmentAddDevice extends Fragment  {

    private AddDeviceViewModel addDeviceViewModel;
    private SsidAdapter ssidAdapter;
    private FragmentAddDeviceBinding binding;
    private FragmentActivity fragmentActivity;
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Toast.makeText(getContext(), "WiFi ON!", Toast.LENGTH_SHORT).show();
        addDeviceViewModel = new ViewModelProvider(this).get(AddDeviceViewModel.class);
        fragmentActivity=getActivity();
        initRecyclerView();
        binding.btnScan.setOnClickListener(v -> {
            toast("Scanning Networks...");
            addDeviceViewModel.scannWifi(getActivity(),MainActivity.wifiManager);
            ssidAdapter.setSSID(addDeviceViewModel.getListWifi());
        }
        );

        binding.btnAdd.setOnClickListener(v -> {
            if(util.isValid(binding.lblNameDevice.getText().toString(),ssidAdapter.getItemSelected())){
                Bundle bundle = addDeviceViewModel.addDevice(ssidAdapter.getItemSelected(),binding.lblNameDevice.getText().toString(),fragmentActivity);
                if(bundle!=null) {
                    toast("Device Connected");
                    FragmentListDevice listFragment = new FragmentListDevice();
                    listFragment.setArguments(bundle);
                    getActivity().getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fragmentContainer, listFragment)
                            .commit();
                }else{
                    toast("Topic Device Wrong Format, Not Added");
                }
            }else{
                toast("Please Type a Name and Select a Network");
            }
        });
    }

    public void initRecyclerView(){
        binding.recyclerViewSsid.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recyclerViewSsid.setHasFixedSize(true);
        ssidAdapter = new SsidAdapter(addDeviceViewModel.getListWifi());
        binding.recyclerViewSsid.setAdapter(ssidAdapter);
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding  = FragmentAddDeviceBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    public void toast(String msg){
        Toast.makeText(getContext(), msg,Toast.LENGTH_SHORT).show();
    }
}