package unicam.pi.mqespol.mqtt;

import android.app.Application;
import android.content.Context;
import android.util.AndroidException;
import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStore;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.DisconnectedBufferOptions;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import unicam.pi.mqespol.util.WifiFuctions;
import unicam.pi.mqespol.view.MainActivity;
import unicam.pi.mqespol.viewModel.DeviceViewModel;


public  class MqttConnection {

    public static MqttAndroidClient mqttAndroidClient;
    public static String clientId = MqttClient.generateClientId();

    public static void connect(Context context) throws MqttException {
            mqttAndroidClient = new MqttAndroidClient(context,"tcp://0.0.0.0:1883",clientId);

            IMqttToken token = mqttAndroidClient.connect();
            token.setActionCallback(new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    Toast.makeText(context,"connected!!",Toast.LENGTH_LONG).show();
                    Log.e("TAG", "CONEXION EXITOsa");
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    Toast.makeText(context,"Conexion Fallida",Toast.LENGTH_LONG).show();

                }
            });


    }

}

