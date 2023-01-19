package icesi.plantapiloto.MQTT;

import java.nio.charset.StandardCharsets;

import com.hivemq.client.mqtt.mqtt5.Mqtt5BlockingClient;
import com.hivemq.client.mqtt.mqtt5.Mqtt5Client;

import icesi.plantapiloto.controlLayer.common.encoders.ObjectEncoder;
import icesi.plantapiloto.controlLayer.common.envents.PublisherI;

public class Publisher implements PublisherI {

    private String host;
    private Mqtt5BlockingClient client;
    private String topic;
    private ObjectEncoder encoder;
    private String name;

    @Override
    public <T> void publish(T msg) {
        String message;

        try {
            message = encoder.encode(msg);
            client.publishWith()
                    .topic(topic)
                    .payload(message.getBytes(StandardCharsets.UTF_8))
                    .send();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setTopic(String topic) {
        this.topic = topic;
    }

    @Override
    public void close() {
        client.disconnect();
    }

    @Override
    public void connect() {
        client = Mqtt5Client.builder()
                .identifier(name) // use a unique identifier
                .serverHost(this.host)
                .buildBlocking();
        client.connect();
    }

    @Override
    public void setEncoder(ObjectEncoder encoder) {
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

}
