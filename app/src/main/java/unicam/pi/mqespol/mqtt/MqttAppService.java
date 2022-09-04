package unicam.pi.mqespol.mqtt;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;

import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.text.format.Formatter;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.lifecycle.LifecycleObserver;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Properties;

import unicam.pi.mqespol.R;
import unicam.pi.mqespol.model.LocalBroker;
import unicam.pi.mqespol.util.Util;
import unicam.pi.mqespol.view.MainActivity;


public class MqttAppService extends Service  {
    public static final String TAG="MqttAppService ";

    private Boolean isServiceUp;
    final String CHANNELID = "Foreground Service ID";

    private final IBinder mBinder= new MyServiceBinder();
    private  PendingIntent pendingIntent;
    private Properties props;


    public class MyServiceBinder extends Binder{


        public MqttAppService getService(){
            return MqttAppService.this;
        }
    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.e("Service","ON BIND");
        return mBinder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.e("Service","ON UNBIND");
        return super.onUnbind(intent);
    }

    public void initService(){
        int NOTIFICATION_ID = 1;
        startForeground(NOTIFICATION_ID, mostrarNotificacion( pendingIntent ));

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Bundle mBundle = intent.getBundleExtra("bundle");
        File file = (File) mBundle.get("configFile");
        props= (Properties) mBundle.get("props");
        if(!isServiceUp){
            initService();
            doTask(file);
            isServiceUp=true;
        }
        return START_STICKY;
    }


    void doTask(File file){
        new Thread(
                new Runnable() {
                    @Override
                    public void run() {
                        Log.e("Service", "Service is running...");
                        LocalBroker localBroker=new LocalBroker(Util.getBrokerURL(getApplicationContext()),1883,"roro");
                        Log.e("TAG", "IP HOST:"+localBroker.getHost());
                        try {
                            mqttMosquette.startMoquette(file);
                            Thread.sleep(2000);
                        } catch (InterruptedException | IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
        ).start();

    }


    public Boolean getStatusService(){
        return isServiceUp;
    }
    public void setStatusService(Boolean status){
        isServiceUp=status;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mqttMosquette.stopMoquette();
        Log.e(TAG,"ON DESTROY");

    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.e(TAG,"ON CREATE");
        isServiceUp=false;
        pendingIntent = PendingIntent.getActivity(this, 0, new Intent(this, MainActivity.class), 0);


    }


    private Notification mostrarNotificacion(PendingIntent pendingIntent){


        NotificationChannel channel = new NotificationChannel(
                CHANNELID,
                "Notificacion Broker",
                    NotificationManager.IMPORTANCE_DEFAULT
        );
        NotificationManager notificationManager=getSystemService(NotificationManager.class);
        notificationManager.createNotificationChannel(channel);
        Notification.Builder notification = new Notification.Builder(this, CHANNELID)
                .setContentText("Server MQTT running...")
                .setContentTitle("Server MQTT running...")
                .setOngoing(true)
                .setTicker("MQTT")
                .setOnlyAlertOnce(true)
                .setContentIntent(pendingIntent)
                .setSmallIcon(R.drawable.custom_bottom);
        return notification.build();
    }
}
