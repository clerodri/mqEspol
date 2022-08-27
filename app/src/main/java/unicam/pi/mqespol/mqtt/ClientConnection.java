package unicam.pi.mqespol.mqtt;


import android.content.Context;
import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.MqttClient;

import unicam.pi.mqespol.util.WifiFuctions;
import unicam.pi.mqespol.view.MainActivity;

public class ClientConnection {

    private MqttAndroidClient client;
    private ClientConnection(Context context) {
            client = new MqttAndroidClient(context.getApplicationContext(), "tcp://"+ WifiFuctions.getIp(MainActivity.wifiManager) +":1883", MqttClient.generateClientId());
    }
    private static  ClientConnection mClient;

    public MqttAndroidClient getMqttClient(){
        return client;
    }
    public static ClientConnection getClient(Context context){
        if(mClient==null){
            mClient = new ClientConnection(context);
        }
        return mClient;
    }
}
