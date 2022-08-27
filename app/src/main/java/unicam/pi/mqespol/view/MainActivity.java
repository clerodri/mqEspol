package unicam.pi.mqespol.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.eclipse.paho.android.service.MqttAndroidClient;

import unicam.pi.mqespol.R;

import unicam.pi.mqespol.mqtt.mqttService;
import unicam.pi.mqespol.util.WifiFuctions;

/**
 * Proyecto Integrador
 *
 *
 */
public class MainActivity extends AppCompatActivity {
    public static WifiManager wifiManager;
   // public static MqttAndroidClient mclient;
    ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);
        setSupportActionBar(toolbar);
        wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        Log.i("TAG", "main activity CREATE");
      //  mclient = ClientConnection.getClient(getApplicationContext()).getMqttClient();  //USO DE CLASE SINGLETON PARA QUE LA CONEXION SE HAGA UNA SOLA VEZ!
      //  initApp();
    }

//    void initApp(){
//       // WifiFuctions.createHostpot(MainActivity.this,wifiManager);
//        Intent serviceMQTT  = new Intent(MainActivity.this, mqttService.class);
//        startForegroundService(serviceMQTT);
//    }

    @Override
    public boolean onCreateOptionsMenu(@NonNull Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    /**
     * Navegacion entre Fragments, Lista de Dispositivos y Para agregar un nuevo Dispositivo.
     * Para ingresar al fragment AddDevice se crea un hilo para que espere que el Wifi este Encendido y luego pueda ingresar.
     * */

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.fragment_addDevice:
                if(!WifiFuctions.isWifiOn){
                    WifiFuctions.setHostPotOff(this);//Off HostPot Wifi
                    WifiFuctions.setWifiOn();
                    Toast.makeText(getApplicationContext(), "Turnning On WiFi...Wait..", Toast.LENGTH_SHORT).show();
                }
                //wifiManager.setWifiEnabled(true); //Turnning ON Wifi
                progressBar.setVisibility(View.VISIBLE);
                Handler mhandler = new Handler();
                mhandler.postDelayed(() -> {
                    while (!WifiFuctions.isConnected(getApplicationContext())) {
                        Log.d("TAG", "ENCENDIENDO WIFI");

                        try {
                            Thread.sleep(1000);
                            } catch (Exception e) {
                        }
                    }
                    progressBar.setVisibility(View.GONE);
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fragmentContainer, FragmentAddDevice.class,null,"fadd")
                            .setReorderingAllowed(true).addToBackStack("name")
                            .commit();
                },1000);

                return true;
            case R.id.fragment_showdevice:
             //   wifiManager.setWifiEnabled(false);
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fragmentContainer, FragmentListDevice.class,null,"fdevice")
                            .setReorderingAllowed(true)
                            .addToBackStack("name")
                            .commit();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
