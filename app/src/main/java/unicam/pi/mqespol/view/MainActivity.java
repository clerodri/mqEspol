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

import org.eclipse.paho.client.mqttv3.MqttException;

import unicam.pi.mqespol.R;

import unicam.pi.mqespol.mqtt.MqttConnection;
import unicam.pi.mqespol.mqtt.mqttService;
import unicam.pi.mqespol.util.WifiFuctions;

/**
 * Proyecto Integrador
 *
 *
 */
public class MainActivity extends AppCompatActivity {
    public static WifiManager wifiManager;
    ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);
        //setSupportActionBar(toolbar);
        wifiManager = (WifiManager) this.getSystemService(Context.WIFI_SERVICE);

        try {
            MqttConnection.connect(getApplicationContext());
        } catch (MqttException e) {
            e.printStackTrace();
        }
        Log.i("ACTIVTY", "main activity CREATE");
    }


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
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fragmentContainer, FragmentListDevice.class,null,"fdevice")
                            .setReorderingAllowed(true)
                            .addToBackStack("name")
                            .commit();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
