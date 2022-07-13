//
// Copyright (c) ZeroC, Inc. All rights reserved.
//
package icesi.plantapiloto.icestorm;

import com.zeroc.Ice.Communicator;
import com.zeroc.Ice.Util;
import com.zeroc.IceStorm.TopicManagerPrx;
import com.zeroc.IceStorm.TopicPrx;

import icesi.plantapiloto.busEvent.Message;
import icesi.plantapiloto.busEvent.PublisherI;
import icesi.plantapiloto.icestorm.publisher.MeasuresPrx;

public class Publisher implements PublisherI
{
    private TopicManagerPrx manager;
    private String proxy;
    private MeasuresPrx measures;
    private Communicator communicator;
    private TopicPrx topic;

    public Publisher(String proxy)
    {
        String storm ="DemoIceStorm/TopicManager:default -h "+proxy+" -p 10000";

        this.proxy = storm;

        communicator = Util.initialize();
        manager = TopicManagerPrx.checkedCast(
            communicator.stringToProxy(this.proxy));
        if(manager == null)
        {
            System.err.println("invalid proxy");
        }

    }


    public void setTopic(String topicName)
    {

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
        com.zeroc.Ice.ObjectPrx publisher = topic.getPublisher();
        measures = MeasuresPrx.uncheckedCast(publisher);

    }


    @Override
    public void publish( Message msg) {
        
        measures.putMeasure(msg);

    }


    @Override
    public void close() {
        communicator.close();        
    }
}
