package icesi.plantapiloto.busEvent;

public interface PublisherI{

    public void publish( Message msg);
    public void setTopic(String topic);
    public void close();
}