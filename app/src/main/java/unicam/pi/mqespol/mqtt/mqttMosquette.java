package unicam.pi.mqespol.mqtt;

import android.util.Log;

import org.eclipse.paho.client.mqttv3.IMqttMessageListener;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;
import java.util.Properties;
import io.moquette.BrokerConstants;
import io.moquette.broker.Server;
import io.moquette.broker.config.MemoryConfig;
import io.moquette.interception.InterceptHandler;
import io.netty.buffer.Unpooled;
import io.netty.handler.codec.mqtt.MqttMessageBuilders;
import io.netty.handler.codec.mqtt.MqttPublishMessage;
import io.netty.handler.codec.mqtt.MqttQoS;
import unicam.pi.mqespol.model.LocalBroker;


public class mqttMosquette implements IMqttMessageListener {

    private static Server mqttBroker;
    private static Boolean isServerOn=false;

    public static void startMoquette(File file) throws IOException {
        try {
            mqttBroker = new Server();
            if(mqttBroker!=null){
               // mqttBroker.startServer(getMemoryConfig(localBroker));
                mqttBroker.startServer(file);
                Log.e("TAG", "Devices is running..."+mqttBroker.listConnectedClients());

                isServerOn = true;
            }
            Thread.sleep(2000);
//            MqttPublishMessage mensaje = MqttMessageBuilders.publish().topicName("OSIRIS").retained(true).qos(MqttQoS.AT_LEAST_ONCE)
//                    .payload(Unpooled.copiedBuffer("Mensaje publico al servidor!".getBytes(StandardCharsets.UTF_8)))
//                    .build();
//            mqttBroker.internalPublish(mensaje,"RonaldoRodriguez");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static Boolean getServerStatus(){
        return isServerOn;
    }

    private static MemoryConfig getMemoryConfig(LocalBroker ServerProperties){
        Properties defaultProperties = new Properties();
        defaultProperties.setProperty(BrokerConstants.HOST_PROPERTY_NAME,ServerProperties.getHost());
        defaultProperties.setProperty(BrokerConstants.PORT_PROPERTY_NAME,Integer.toString(ServerProperties.getPort()));
        defaultProperties.setProperty(BrokerConstants.METRICS_ENABLE_PROPERTY_NAME,Boolean.FALSE.toString());
        defaultProperties.setProperty(BrokerConstants.ALLOW_ANONYMOUS_PROPERTY_NAME,Boolean.TRUE.toString());
        return new MemoryConfig(defaultProperties);
    }
    public static void stopMoquette() {
        try {
            if(mqttBroker!=null) mqttBroker.stopServer();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void messageArrived(String topic, MqttMessage message) throws Exception {
        Log.d("TAG", "Mensaje recibido callback: " + message);
    }
}
