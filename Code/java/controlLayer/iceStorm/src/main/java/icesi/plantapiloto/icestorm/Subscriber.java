//
// Copyright (c) ZeroC, Inc. All rights reserved.
//
package icesi.plantapiloto.icestorm;

import icesi.plantapiloto.controlLayer.common.encoders.ObjectEncoder;
import icesi.plantapiloto.controlLayer.common.envents.CallbackSubI;
import icesi.plantapiloto.controlLayer.common.envents.SubscriberI;
import icesi.plantapiloto.icestorm.publisher.*;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;

import com.zeroc.Ice.Communicator;
import com.zeroc.Ice.Current;
import com.zeroc.Ice.Identity;
import com.zeroc.Ice.ObjectAdapter;
import com.zeroc.Ice.ObjectPrx;
import com.zeroc.Ice.Util;
import com.zeroc.IceStorm.TopicManagerPrx;
import com.zeroc.IceStorm.TopicPrx;

public class Subscriber implements SubscriberI {
    public static class MeasuresI<T> implements Measures {
        private CallbackSubI calbackSubI;
        private ObjectEncoder encoder;
        private Class<T> type;

        MeasuresI(CallbackSubI calbackSubI, ObjectEncoder encoder, Class<T> type) {
            this.calbackSubI = calbackSubI;
            this.encoder = encoder;
            this.type = type;
        }

        @Override
        public void putMeasure(String me, Current current) {
            calbackSubI.reciveMessage(encoder.decode(me, type));
        }

    }

    private TopicManagerPrx manager;
    private ObjectAdapter adapter;
    private Communicator communicator;
    private TopicPrx topic;
    private ObjectPrx subscriber;
    private ObjectEncoder encoder;

    private String endpoint;
    private String uriStorm;
    private String name;

    public Subscriber() {

    }

    @Override
    public <T> void subscribe(String topicName, CallbackSubI call, Class<T> type) {
        topic = null;
        try {
            topic = manager.retrieve(topicName);
        } catch (com.zeroc.IceStorm.NoSuchTopic e) {
            try {
                topic = manager.create(topicName);
            } catch (com.zeroc.IceStorm.TopicExists ex) {
                System.err.println("temporary failure, try again.");
            }
        }
        Identity subId = Util.stringToIdentity(this.name);

        subscriber = adapter.add(new MeasuresI<T>(call, encoder, type), subId);
        adapter.activate();

        java.util.Map<String, String> qos = new java.util.HashMap<>();

        try {
            topic.subscribeAndGetPublisher(qos, subscriber);
        } catch (com.zeroc.IceStorm.AlreadySubscribed e) {
            // This should never occur when subscribing with an UUID

            System.out.println("reactivating persistent subscriber");
        } catch (com.zeroc.IceStorm.InvalidSubscriber e) {
            e.printStackTrace();
        } catch (com.zeroc.IceStorm.BadQoS e) {
            e.printStackTrace();
        }

    }

    @Override
    public void close() {
        topic.unsubscribe(subscriber);
        communicator.close();
    }

    @Override
    public void setEncoder(ObjectEncoder encoder) {
        this.encoder = encoder;
    }

    @Override
    public void setHost(String host) {
        String storm = "DemoIceStorm/TopicManager:default -h " + host + " -p 10000";
        this.uriStorm = storm;

        String localHost = getAdress(host);
        this.endpoint = "tcp -h " + localHost + " -p 9099";
    }

    private String getAdress(String netSeg) {
        try {

            Enumeration<NetworkInterface> nets = NetworkInterface.getNetworkInterfaces();
            InetAddress i1 = InetAddress.getByName(netSeg);
            boolean isv4 = i1 instanceof Inet4Address;

            while (nets.hasMoreElements()) {
                NetworkInterface net = nets.nextElement();
                boolean reach = i1.isReachable(net, 0, 50);
                if (reach) {
                    System.out.println("Network: " + net.getName());
                    Enumeration<InetAddress> adres = net.getInetAddresses();
                    while (adres.hasMoreElements()) {
                        InetAddress same = adres.nextElement();
                        boolean sisv4 = same instanceof Inet4Address;
                        if ((sisv4 && isv4) || (!sisv4 && !isv4)) {
                            return same.getHostAddress();
                        }

                    }

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "localhost";
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public void connect() {

        communicator = Util.initialize();

        manager = TopicManagerPrx.checkedCast(
                communicator.stringToProxy(this.uriStorm));
        if (manager == null) {
            System.err.println("invalid proxy");
        }

        adapter = communicator.createObjectAdapterWithEndpoints("subscribe", this.endpoint);

    }

    @Override
    public ObjectEncoder getEncoder() {
        return encoder;
    }
}
