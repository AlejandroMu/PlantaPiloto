package icesi.plantapiloto.controlLayer.common;

public interface SubscriberI {

    public void subscribe(String topic, CallbackSubI call);
    public void close();

    
}
