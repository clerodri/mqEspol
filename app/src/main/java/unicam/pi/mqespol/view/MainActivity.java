package unicam.pi.mqespol.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Properties;

import unicam.pi.mqespol.R;

import unicam.pi.mqespol.mqtt.MqttAppService;
import unicam.pi.mqespol.util.Util;
import unicam.pi.mqespol.util.WifiReciber;
import unicam.pi.mqespol.viewModel.ServiceViewModel;


/**
 * Proyecto Integrador
 */
public class MainActivity extends AppCompatActivity {
    public static final String TAG = "TORO";
    public static WifiManager wifiManager;
    public static WifiReciber wifiReciever;
    private Properties properties;
    Intent serviceIntent;
    public static MqttAppService mService;
    private ServiceViewModel mServiceViewModel;
    Button btn_service;

    // ProgressBar progressBar;
    Toolbar toolbar;
    NavController navController;
    AppBarConfiguration appBarConfiguration;
    NavHostFragment navHostFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("PEPA", "ACTIVITY ON CREATE");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init_Resources();
        btn_service.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "APLASTADO");
                toogle();
            }
        });

        mServiceViewModel.getBinder().observe(this, new Observer<MqttAppService.MyServiceBinder>() {
            @Override
            public void onChanged(MqttAppService.MyServiceBinder myServiceBinder) {
                if (myServiceBinder != null) {
                    Log.d(TAG, "onChangeTrigger:Bound to service");
                    mService = myServiceBinder.getService();
                } else {
                    Log.d(TAG, "onChangeTrigger: Unbound to service");
                    mService = null;    //UNBOUND
                }
            }
        });

        mServiceViewModel.getStateServer().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (aBoolean) {
                    Log.d(TAG, "Boolean triger status: True Bound to service");
                    btn_service.setText("STOP SERVICE");
                } else {
                    Log.d(TAG, "Boolean triger status: False Bound to service");
                    btn_service.setText("START SERVICE");
                }
            }
        });
    }

    public void toogle() {
        if (mService != null) {
            if (!mService.getStatusService()) {
                Log.d(TAG, "STATUS FALSE TO TRUE, RUNNING SERVER FIRST TIME");
                startService();
                mServiceViewModel.setStateServer(true);
            } else {
                Log.d(TAG, "STATUS SERVICE IS TRUE, U WANT TO STOP  SERVICE");
                stopService();

            }
        }
    }

    void init_Resources() {
        serviceIntent = new Intent(this, MqttAppService.class);
        mServiceViewModel = new ViewModelProvider(this).get(ServiceViewModel.class);
        toolbar = findViewById(R.id.toolbar);
        btn_service = findViewById(R.id.btn_service);
        navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.fragmentContainer);
        assert navHostFragment != null;
        navController = navHostFragment.getNavController();
        appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).build();
        NavigationUI.setupWithNavController(toolbar, navController, appBarConfiguration);

    }


    @Override
    public boolean onCreateOptionsMenu(@NonNull Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }


    /**
     * Navegacion entre Fragments, Lista de Dispositivos y Para agregar un nuevo Dispositivo.
     * Para ingresar al fragment AddDevice se crea un hilo para que espere que el Wifi este Encendido y luego pueda ingresar.
     */

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        //      switch (item.getItemId()) {
        //           case R.id.fragment_addDevice:
        //               deviceViewModel.activeWifi(wifiManager);
        //    navController.navigate(R.id.action_fragmentListDevice_to_fragmentAddDevice);
//                if(!WifiFuctions.isWifiOn){
//                    WifiFuctions.setHostPotOff(this);//Off HostPot Wifi
//                    WifiFuctions.setWifiOn();
//                    Toast.makeText(getApplicationContext(), "Turnning On WiFi...Wait..", Toast.LENGTH_SHORT).show();
//                }
//                progressBar.setVisibility(View.VISIBLE);


        return NavigationUI.onNavDestinationSelected(item, navController)
                || super.onOptionsItemSelected(item);
    }


    @Override
    protected void onDestroy() {
        Log.d("PEPA", "ACTIVITY ON DESTROY");
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
    }

    @Override
    protected void onStart() {
        Log.d("PEPA", "ACTIVITY ON START");
        super.onStart();
    }

    @Override
    protected void onResume() {
        Log.d("PEPA", "ACTIVITY ON RESUME");
        super.onResume();
        startService();
        mServiceViewModel.setStateServer(true);
    }

    private void startService() {
        Log.e(TAG, "SERVICE STARTED AND BOUND TO ACTIVTY");
        Bundle bundle = new Bundle();
        bundle.putSerializable("configFile", createConfigurationFile());
        bundle.putSerializable("props", properties);
        serviceIntent.putExtra("bundle", bundle);
        startForegroundService(serviceIntent);
        bindService();
    }

    private void stopService() {
        if (mServiceViewModel.getBinder() != null) {
            unbindService(mServiceViewModel.getMyServiceConnection());
            Log.d(TAG, "SERVICE UNBOUND");
        }
        mService.setStatusService(false); //FOR TURNING ON AGAIN THE SERVICE IF IS BUTTON PUSH IT
        mServiceViewModel.setStateServer(false);
        Log.d(TAG, "STATE SERVICE FALSE");
        stopService(serviceIntent);
    }

    private void bindService() {
        bindService(serviceIntent, mServiceViewModel.getMyServiceConnection(), Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onPause() {
//        if(mServiceViewModel.getBinder()!=null){
//            unbindService(mServiceViewModel.getMyServiceConnection());
//        }
//        Log.d("PEPA","ACTIVITY ON PAUSE");
        super.onPause();
    }

    @Override
    protected void onStop() {
        Log.d("PEPA", "ACTIVITY ON STOP");
        super.onStop();
    }

    @Override
    protected void onRestart() {
        Log.d("PEPA", "ACTIVITY ON RESTART");
        super.onRestart();
    }

    private File createConfigurationFile() {
        File file = getApplication().getApplicationContext().getDir("media", 0);
        file = new File(file.getAbsolutePath() + Util.BROKER_CONF_FILE);
        try {
            if (file.exists())
                return writeToConfFile(file);
            if (file.createNewFile())
                return writeToConfFile(file);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return file;
    }

    private File writeToConfFile(File paramFile) throws Exception {
        FileOutputStream fileOutputStream = new FileOutputStream(paramFile);
        fileOutputStream.write(("port " + "1883" + "\n").getBytes());
        this.properties.put("port", "1883");
        fileOutputStream.write(("host " + Util.getBrokerURL(getApplication().getApplicationContext()) + "\n").getBytes());
        this.properties.put("host", Util.getBrokerURL(getApplication().getApplicationContext()));
        fileOutputStream.write(("websocket_port " + 8080 + "\n").getBytes());
        fileOutputStream.close();
        return paramFile;
    }
}

