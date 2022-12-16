package icesi.plantapiloto.controlLayer.common.envents;

import icesi.plantapiloto.controlLayer.common.encoders.MessageEncoder;
import icesi.plantapiloto.controlLayer.common.entities.Message;

public interface PublisherI {

    public void setEncoder(MessageEncoder encoder);

    public void setHost(String host);

    public void setName(String name);

    public void setTopic(String topic);

    public void publish(Message msg);

    public void close();

    public void connect();

}