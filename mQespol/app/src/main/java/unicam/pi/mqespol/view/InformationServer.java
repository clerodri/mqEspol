package unicam.pi.mqespol.view;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import unicam.pi.mqespol.R;
import unicam.pi.mqespol.databinding.FragmentInformationServerBinding;
import unicam.pi.mqespol.util.Util;


public class InformationServer extends Fragment {

     private FragmentInformationServerBinding binding;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }



    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.tvIp.setText(Util.getIpLocal());
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentInformationServerBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }
}