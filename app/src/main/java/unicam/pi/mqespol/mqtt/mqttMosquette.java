package unicam.pi.mqespol.mqtt;

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


public class mqttMosquette {

    private static Server mqttBroker;

    public static void startMoquette(LocalBroker localBroker, MQTTServerListener listener) throws IOException {
        List<InterceptHandler> list = Collections.singletonList(listener);
        try {
            mqttBroker = new Server();
            if(mqttBroker!=null){
                mqttBroker.startServer(getMemoryConfig(localBroker),list);
            }
            Thread.sleep(2000);
            MqttPublishMessage mensaje = MqttMessageBuilders.publish().topicName("/sensor").retained(true).qos(MqttQoS.AT_LEAST_ONCE)
                    .payload(Unpooled.copiedBuffer("Mensaje publico al servidor!".getBytes(StandardCharsets.UTF_8)))
                    .build();
            mqttBroker.internalPublish(mensaje,"RonaldoRodriguez");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static MemoryConfig getMemoryConfig(LocalBroker ServerProperties){
        Properties defaultProperties = new Properties();
        defaultProperties.setProperty(BrokerConstants.HOST_PROPERTY_NAME,BrokerConstants.HOST);
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
}
