//
// Copyright (c) ZeroC, Inc. All rights reserved.
//
package icesi.plantapiloto.icestorm;

import com.zeroc.Ice.Communicator;
import com.zeroc.Ice.Util;
import com.zeroc.IceStorm.TopicManagerPrx;
import com.zeroc.IceStorm.TopicPrx;

import icesi.plantapiloto.controlLayer.common.encoders.MessageEncoder;
import icesi.plantapiloto.controlLayer.common.entities.Message;
import icesi.plantapiloto.controlLayer.common.envents.PublisherI;
import icesi.plantapiloto.icestorm.publisher.MeasuresPrx;

public class Publisher implements PublisherI {
    private TopicManagerPrx manager;
    private String proxy;
    private MeasuresPrx measures;
    private Communicator communicator;
    private TopicPrx topic;
    private String name;
    private String topicName;

    private MessageEncoder encoder;

    public Publisher() {

        communicator = Util.initialize();

    }

    public void setTopic(String topicName) {
        this.topicName = topicName;
    }

    @Override
    public void publish(Message msg) {
        String mseg = encoder.encode(msg);
        measures.putMeasure(mseg);

    }

    @Override
    public void connect() {
        try {
            manager = TopicManagerPrx.checkedCast(
                    communicator.stringToProxy(this.proxy));

            if (manager == null) {
                System.err.println("invalid proxy");
            }
            topic = manager.retrieve(topicName);
        } catch (com.zeroc.IceStorm.NoSuchTopic e) {
            try {
                topic = manager.create(topicName);
            } catch (com.zeroc.IceStorm.TopicExists ex) {
                System.err.println("temporary failure, try again.");
            }
        }
        com.zeroc.Ice.ObjectPrx publisher = topic.getPublisher();
        measures = MeasuresPrx.uncheckedCast(publisher);
    }

    @Override
    public void close() {
        topic = null;
        measures = null;
        manager = null;

    }

    @Override
    public void setEncoder(MessageEncoder encoder) {
        this.encoder = encoder;
    }

    @Override
    public void setHost(String host) {
        String storm = "DemoIceStorm/TopicManager:default -h " + host + " -p 10000";
        this.proxy = storm;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }
}
