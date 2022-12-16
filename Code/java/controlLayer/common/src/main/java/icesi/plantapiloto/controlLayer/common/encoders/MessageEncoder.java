package icesi.plantapiloto.controlLayer.common.encoders;

import icesi.plantapiloto.controlLayer.common.entities.Message;

public interface MessageEncoder {

    public String encode(Message message);

    public Message decode(String json);
}
