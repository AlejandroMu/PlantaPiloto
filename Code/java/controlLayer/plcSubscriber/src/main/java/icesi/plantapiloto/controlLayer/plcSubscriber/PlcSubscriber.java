package icesi.plantapiloto.controlLayer.plcSubscriber;

import icesi.plantapiloto.controlLayer.common.encoders.ObjectEncoder;
import icesi.plantapiloto.controlLayer.common.entities.Message;
import icesi.plantapiloto.controlLayer.common.envents.CallbackSubI;
import icesi.plantapiloto.controlLayer.common.envents.SubscriberI;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import icesi.plantapiloto.controllers.ValueControllerPrx;

import com.zeroc.Ice.Communicator;
import com.zeroc.Ice.Util;

public class PlcSubscriber implements CallbackSubI {

    private SubscriberI subscriberI;

    private ValueControllerPrx valueControllerPrx;
    private String topic;

    public <T> PlcSubscriber(SubscriberI sub, ValueControllerPrx man, String topic, Class<T> type) {
        this.subscriberI = sub;
        this.valueControllerPrx = man;
        this.topic = topic;
        subscriberI.connect();
        subscriberI.subscribe(this.topic, this, type);
    }

    @Override
    public <T> void reciveMessage(T msg) {
        String data = subscriberI.getEncoder().encode(msg);
        System.out.println(data);
        valueControllerPrx.saveValues(data);
    }

    public static void main(String[] args) throws Exception {
        Communicator communicator = Util.initialize(args, "subscriber.config");
        ValueControllerPrx manager = ValueControllerPrx.checkedCast(communicator.propertyToProxy("Model.Proxy"));

        String clientId = communicator.getProperties().getProperty("subscriber.id");
        String subIp = communicator.getProperties().getProperty("subscriber.ip");
        String topic = communicator.getProperties().getProperty("subscriber.topic");
        String subClass = communicator.getProperties().getProperty("subscriber.class");
        String encoderClass = communicator.getProperties().getProperty("subscriber.encoder");

        SubscriberI subscriberI = (SubscriberI) Class.forName(subClass).getDeclaredConstructor().newInstance();
        ObjectEncoder encoder = (ObjectEncoder) Class.forName(encoderClass).getDeclaredConstructor()
                .newInstance();

        subscriberI.setName(clientId);
        subscriberI.setHost(subIp);
        subscriberI.setEncoder(encoder);
        new PlcSubscriber(subscriberI, manager, topic, Message.class);
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String line = reader.readLine();
        while (!line.equals("exit")) {
            line = reader.readLine();
        }

    }

}
