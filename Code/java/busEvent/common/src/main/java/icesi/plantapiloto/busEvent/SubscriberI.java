package icesi.plantapiloto.busEvent;

public interface SubscriberI {

    public void subscribe(String topic, CallbackSubI call);
    public void close();

    
}
