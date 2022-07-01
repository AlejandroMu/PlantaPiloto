package icesi.plantapiloto.MQTT;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import com.hivemq.client.mqtt.mqtt5.Mqtt5BlockingClient;
import com.hivemq.client.mqtt.mqtt5.Mqtt5Client;

import icesi.plantapiloto.busEvent.Message;
import icesi.plantapiloto.busEvent.PublisherI;

public class Publisher implements PublisherI {

    private String host;
    private Mqtt5BlockingClient client;
    private String topic;

    public Publisher(String id,String host){
        this.host=host;
         client = Mqtt5Client.builder()
        .identifier(id) // use a unique identifier
        .serverHost(this.host)
        .buildBlocking();
         client.connect();
        this.topic ="default";
 
    }

    @Override
    public void publish(Message msg) {
        String message;
        try {
            message = Utils.toString(msg);
            client.publishWith()
            .topic(topic)
            .payload(message.getBytes(StandardCharsets.UTF_8))
            .send();        
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setTopic(String topic) {
        this.topic =topic;        
    }

    @Override
    public void close() {
        client.disconnect();        
    }
    
}
