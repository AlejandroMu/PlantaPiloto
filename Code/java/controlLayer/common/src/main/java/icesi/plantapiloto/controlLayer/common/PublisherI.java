package icesi.plantapiloto.controlLayer.common;

public interface PublisherI{

    public void publish( Message msg);
    public void setTopic(String topic);
    public void close();
}