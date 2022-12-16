package icesi.plantapiloto.controlLayer.plcSubscriber;

import icesi.plantapiloto.controlLayer.common.encoders.MessageEncoder;
import icesi.plantapiloto.controlLayer.common.entities.Measure;
import icesi.plantapiloto.controlLayer.common.entities.Message;
import icesi.plantapiloto.controlLayer.common.envents.CallbackSubI;
import icesi.plantapiloto.controlLayer.common.envents.SubscriberI;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Date;
import java.util.List;

import icesi.plantapiloto.MQTT.Subscriber;
import icesi.plantapiloto.model.*;
import icesi.plantapiloto.controller.entityManager.*;

import com.zeroc.Ice.Communicator;
import com.zeroc.Ice.Current;
import com.zeroc.Ice.Identity;
import com.zeroc.Ice.ObjectAdapter;
import com.zeroc.Ice.ObjectPrx;
import com.zeroc.Ice.Util;

public class PlcSubscriber implements CallbackSubI {

    private SubscriberI subscriberI;

    private ManagerIPrx manager;
    private String topic;

    public PlcSubscriber(SubscriberI sub, ManagerIPrx man, String topic) {
        this.subscriberI = sub;
        this.manager = man;
        this.topic = topic;
        init();
    }

    private void init() {
        subscriberI.subscribe(this.topic, this);
    }

    @Override
    public void reciveMessage(Message msg) {
        String sourceData = msg.getSourceData();
        List<Measure> measures = msg.getMeasures();
        long time = msg.getTime().getTime();
        for (Measure measure : measures) {
            String value = measure.getValue();
            String registry = sourceData + ":" + value + ":" + time + ":" + measure.getName();
            manager.save(registry);
        }
    }

    public static void main(String[] args) throws Exception {
        Communicator communicator = Util.initialize(args, "subscriber.config");
        ManagerIPrx manager = ManagerIPrx.checkedCast(communicator.propertyToProxy("Model.Proxy"));

        String clientId = communicator.getProperties().getProperty("subscriber.id");
        String subIp = communicator.getProperties().getProperty("subscriber.ip");
        String topic = communicator.getProperties().getProperty("subscriber.topic");
        String subClass = communicator.getProperties().getProperty("subscriber.class");
        String encoderClass = communicator.getProperties().getProperty("subscriber.encoder");

        SubscriberI subscriberI = (SubscriberI) Class.forName(subClass).getDeclaredConstructor().newInstance();
        MessageEncoder encoder = (MessageEncoder) Class.forName(encoderClass).getDeclaredConstructor().newInstance();

        subscriberI.setName(clientId);
        subscriberI.setHost(subIp);
        subscriberI.setEncoder(encoder);
        PlcSubscriber plcSubscriber = new PlcSubscriber(subscriberI, manager, topic);
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String line = reader.readLine();
        while (!line.equals("exit")) {
            line = reader.readLine();
        }

    }

}
