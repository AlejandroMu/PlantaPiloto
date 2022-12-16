package icesi.plantapiloto.MQTT;

import java.nio.charset.StandardCharsets;
import java.util.function.Consumer;

import com.hivemq.client.mqtt.mqtt5.Mqtt5BlockingClient;
import com.hivemq.client.mqtt.mqtt5.Mqtt5Client;
import com.hivemq.client.mqtt.mqtt5.message.publish.Mqtt5Publish;

import icesi.plantapiloto.controlLayer.common.encoders.MessageEncoder;
import icesi.plantapiloto.controlLayer.common.entities.Message;
import icesi.plantapiloto.controlLayer.common.envents.CallbackSubI;
import icesi.plantapiloto.controlLayer.common.envents.SubscriberI;

public class Subscriber implements SubscriberI {

    private String host;
    private Mqtt5BlockingClient client;
    private MessageEncoder encoder;
    private String name;

    @Override
    public void subscribe(String topic, CallbackSubI call) {
        Consumer<Mqtt5Publish> callback = (publish) -> {
            String msg = new String(publish.getPayloadAsBytes(), StandardCharsets.UTF_8);
            Message messObject;
            messObject = encoder.decode(msg);
            call.reciveMessage(messObject);

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

    @Override
    public void setEncoder(MessageEncoder encoder) {
        this.encoder = encoder;
    }

    @Override
    public void setHost(String host) {
        this.host = host;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public void connect() {
        client = Mqtt5Client.builder()
                .identifier(name) // use a unique identifier
                .serverHost(this.host)
                .buildBlocking();
        client.connect();
    }

}
