package icesi.plantapiloto.common.envents;

import icesi.plantapiloto.common.encoders.ObjectEncoder;

public interface PublisherI {

    public ObjectEncoder getEncoder();

    public void setEncoder(ObjectEncoder encoder);

    public void setHost(String host);

    public void setName(String name);

    public void setTopic(String topic);

    public <T> void publish(T msg);

    public void close();

    public void connect();

}