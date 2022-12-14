package icesi.plantapiloto.controlLayer.plcSubscriber;

import icesi.plantapiloto.controlLayer.common.CallbackSubI;
import icesi.plantapiloto.controlLayer.common.Message;
import icesi.plantapiloto.controlLayer.common.SubscriberI;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Date;

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

    public PlcSubscriber(SubscriberI sub, ManagerIPrx man) {
        this.subscriberI = sub;
        this.manager = man;
        init();
    }

    private void init() {
        subscriberI.subscribe("40B", this);
    }

    @Override
    public void reciveMessage(Message msg) {
        String sourceData = msg.getSourceData();
        String type = msg.getType();
        String value = msg.getValue();
        long time = msg.getTime().getTime();
        String registry = sourceData + ":" + type + ":" + value + ":" + time + ":" + msg.getName();
        manager.save(registry);
    }

    public static void main(String[] args) throws Exception {
        Communicator communicator = Util.initialize(args, "subscriber.config");
        ManagerIPrx manager = ManagerIPrx.checkedCast(communicator.propertyToProxy("Model.Proxy"));
        PlcSubscriber plcSubscriber = new PlcSubscriber(new Subscriber("plcSJava", "10.147.19.2"), manager);
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String line = reader.readLine();
        while (!line.equals("exit")) {
            line = reader.readLine();
        }

    }

}
