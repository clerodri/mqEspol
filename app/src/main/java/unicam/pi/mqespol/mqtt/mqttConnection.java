package unicam.pi.mqespol.mqtt;

import android.app.Application;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;

import unicam.pi.mqespol.util.WifiFuctions;
import unicam.pi.mqespol.util.util;
import unicam.pi.mqespol.view.MainActivity;


public  class mqttConnection {

    private  static String clientId;



//    public static void mqttConnect(Context context){
//        try {
//            IMqttToken token = MainActivity.mclient.connect();
//            token.setActionCallback(new IMqttActionListener() {
//                @Override
//                public void onSuccess(IMqttToken asyncActionToken) {
//                   Toast.makeText(context,"connected!!",Toast.LENGTH_LONG).show();
//                   // setSubscription();
//
//                }
//
//                @Override
//                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
//                    Toast.makeText(context,"connection failed!!",Toast.LENGTH_LONG).show();
//                    exception.printStackTrace();
//                }
//            });
//        } catch (MqttException mqttException) {
//            mqttException.printStackTrace();
//        }
//
//    }





}
