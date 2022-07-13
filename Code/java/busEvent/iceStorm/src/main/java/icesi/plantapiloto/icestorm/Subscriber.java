//
// Copyright (c) ZeroC, Inc. All rights reserved.
//
package icesi.plantapiloto.icestorm;

import icesi.plantapiloto.busEvent.*;
import icesi.plantapiloto.icestorm.publisher.*;

import java.util.UUID;

import com.zeroc.Ice.Communicator;
import com.zeroc.Ice.Current;
import com.zeroc.Ice.Identity;
import com.zeroc.Ice.ObjectAdapter;
import com.zeroc.Ice.ObjectPrx;
import com.zeroc.Ice.Util;
import com.zeroc.IceStorm.TopicManagerPrx;
import com.zeroc.IceStorm.TopicPrx;

public class Subscriber implements SubscriberI
{
    public static class MeasuresI implements Measures
    {
        private CallbackSubI calbackSubI;
        MeasuresI(CallbackSubI calbackSubI){
            this.calbackSubI = calbackSubI;
        }
        @Override
        public void putMeasure(Message me, Current current)
        {
            calbackSubI.reciveMessage(me);
        }

    }

    private TopicManagerPrx manager;
    private String endpoint;
    private ObjectAdapter adapter;
    private String uriStorm;
    private Communicator communicator;
    private TopicPrx topic;
    private ObjectPrx subscriber;

    public Subscriber(String endpoint, String uriStorm){
        String storm ="DemoIceStorm/TopicManager:default -h "+uriStorm+" -p 10000";
        String host = "tcp -h "+endpoint+" -p 9099";
        this.endpoint = host;
        this.uriStorm =storm;

        communicator = Util.initialize();
        

        manager = TopicManagerPrx.checkedCast(
            communicator.stringToProxy(this.uriStorm));
        if(manager == null)
        {
            System.err.println("invalid proxy");
        }

        adapter = communicator.createObjectAdapterWithEndpoints("subscribe",this.endpoint);

        
    }


    @Override
    public void subscribe(String topicName, CallbackSubI call) {
        topic = null;
        try
        {
            topic = manager.retrieve(topicName);
        }
        catch(com.zeroc.IceStorm.NoSuchTopic e)
        {
            try
            {
                topic = manager.create(topicName);
            }
            catch(com.zeroc.IceStorm.TopicExists ex)
            {
                System.err.println("temporary failure, try again.");
            }
        }
        String id = UUID.randomUUID().toString();
        Identity subId = Util.stringToIdentity(id);

        subscriber = adapter.add(new MeasuresI(call), subId);
        adapter.activate();

        java.util.Map<String, String> qos = new java.util.HashMap<>();
      
        try
        {
            topic.subscribeAndGetPublisher(qos, subscriber);
        }
        catch(com.zeroc.IceStorm.AlreadySubscribed e)
        {
            // This should never occur when subscribing with an UUID
            
            System.out.println("reactivating persistent subscriber");
        }
        catch(com.zeroc.IceStorm.InvalidSubscriber e)
        {
            e.printStackTrace();
        }
        catch(com.zeroc.IceStorm.BadQoS e)
        {
            e.printStackTrace();
        }


    }


    @Override
    public void close() {
        topic.unsubscribe(subscriber);
        communicator.close();
    }
}
