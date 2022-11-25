package icesi.plantapiloto.MQTT;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.function.Consumer;

import com.hivemq.client.mqtt.mqtt5.Mqtt5BlockingClient;
import com.hivemq.client.mqtt.mqtt5.Mqtt5Client;
import com.hivemq.client.mqtt.mqtt5.message.publish.Mqtt5Publish;

import icesi.plantapiloto.controlLayer.common.CallbackSubI;
import icesi.plantapiloto.controlLayer.common.Message;
import icesi.plantapiloto.controlLayer.common.SubscriberI;

public class Subscriber implements SubscriberI {

    private String host;
    private Mqtt5BlockingClient client;

    public Subscriber(String id,String host){
        this.host=host;
         client = Mqtt5Client.builder()
        .identifier(id) // use a unique identifier
        .serverHost(this.host)
        .buildBlocking();
         client.connect();
         System.out.println("connect");
 
    }

    @Override
    public void subscribe(String topic, CallbackSubI call) {
        Consumer<Mqtt5Publish> callback = (publish)->{
            String msg = new String(publish.getPayloadAsBytes(), StandardCharsets.UTF_8);
            Message messObject;
            try {
                messObject = (Message)Utils.fromString(msg);
                call.reciveMessage(messObject);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        };
        client.toAsync().subscribeWith()
                .topicFilter(topic)
                .callback(callback)
                .send();
    }

    @Override
    public void close() {
        client.disconnect();
    }
    
}
