package icesi.plantapiloto.controlLayer.common.envents;

import icesi.plantapiloto.controlLayer.common.encoders.MessageEncoder;

public interface SubscriberI {
    public void setEncoder(MessageEncoder encoder);

    public void setHost(String host);

    public void setName(String name);

    public void subscribe(String topic, CallbackSubI call);

    public void close();

    public void connect();

}
