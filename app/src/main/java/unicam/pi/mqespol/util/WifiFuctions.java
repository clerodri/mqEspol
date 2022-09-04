package unicam.pi.mqespol.util;

import android.Manifest;
import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Handler;
import android.text.format.Formatter;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;
import java.util.List;

import unicam.pi.mqespol.view.MainActivity;

/**
 * Esta clase Sirve para brindar funciones de la API Wifi Manager
 * Implementa ON/OFF HostPot Local,Scanear Redes Wifi y verificar Connectividad con Wifi e IP.
 * */


public class WifiFuctions extends Application {

    static  WifiManager.LocalOnlyHotspotReservation myreservation;
    public static Boolean isHostPotOn=false;
    public static Boolean isWifiOn=false;




    public static void createHostpot(FragmentActivity activity, WifiManager wifiManager){
        isHostPotOn=true;
        if (ActivityCompat.checkSelfPermission(activity.getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 0);
        }
        wifiManager.startLocalOnlyHotspot(new WifiManager.LocalOnlyHotspotCallback() {
            @Override
            public void onStarted(WifiManager.LocalOnlyHotspotReservation reservation) {

                super.onStarted(reservation);
                myreservation =reservation;
                Log.i("TAG", "ssid is:" + reservation.getWifiConfiguration().SSID);
                Log.i("TAG", "password is:" + reservation.getWifiConfiguration().preSharedKey);
                Log.i("TAG", "HOTSTPOT STARTED");
                Toast.makeText(activity.getApplicationContext(), "NETWORK LOCAL STARTED", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onStopped() {
                Toast.makeText(activity.getApplicationContext(), "HOSTPOT STOPPED", Toast.LENGTH_SHORT).show();
                Log.i("TAG", "STOPED");
                super.onStopped();

            }

            @Override
            public void onFailed(int reason) {
                Toast.makeText(activity.getApplicationContext(), "HOSTPOT FAILED!", Toast.LENGTH_SHORT).show();
                Log.i("TAG", "HOTSPOT FAILED");
                super.onFailed(reason);

            }
        }, new Handler());
    }

    public static void setHostPotOff(Context context){
        if(myreservation!=null) {
            isHostPotOn=false;
            myreservation.close();
            Toast.makeText(context, "NETWORK LOCAL STOPPED", Toast.LENGTH_SHORT).show();
            Log.i("TAG", "setHostPotoff");
        }
    }


    public static String getIp(WifiManager wifiManager){
        WifiInfo connectionInfo = wifiManager.getConnectionInfo();
        int ipAddress = connectionInfo.getIpAddress();
        return Formatter.formatIpAddress(ipAddress);
    }


    }

