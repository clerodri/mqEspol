package unicam.pi.mqespol.mqtt;

import io.moquette.interception.AbstractInterceptHandler;
import io.moquette.interception.messages.InterceptAcknowledgedMessage;
import io.moquette.interception.messages.InterceptConnectMessage;
import io.moquette.interception.messages.InterceptConnectionLostMessage;
import io.moquette.interception.messages.InterceptDisconnectMessage;
import io.moquette.interception.messages.InterceptPublishMessage;
import io.moquette.interception.messages.InterceptSubscribeMessage;
import io.moquette.interception.messages.InterceptUnsubscribeMessage;

public class MQTTServerListener extends AbstractInterceptHandler {

    private final String TAG = "MQTTServerListener";
    @Override
    public String getID() {
        return "MQTTServerListener";
    }

    @Override
    public void onConnect(InterceptConnectMessage msg) {
        super.onConnect(msg);
    }

    @Override
    public void onDisconnect(InterceptDisconnectMessage msg) {
        super.onDisconnect(msg);
    }

    @Override
    public void onConnectionLost(InterceptConnectionLostMessage msg) {
        super.onConnectionLost(msg);
    }

    @Override
    public void onMessageAcknowledged(InterceptAcknowledgedMessage msg) {
        super.onMessageAcknowledged(msg);
    }

    @Override
    public void onPublish(InterceptPublishMessage msg) {
        super.onPublish(msg);
    }

    @Override
    public void onSubscribe(InterceptSubscribeMessage msg) {
        super.onSubscribe(msg);
    }

    @Override
    public void onUnsubscribe(InterceptUnsubscribeMessage msg) {
        super.onUnsubscribe(msg);
    }
}
