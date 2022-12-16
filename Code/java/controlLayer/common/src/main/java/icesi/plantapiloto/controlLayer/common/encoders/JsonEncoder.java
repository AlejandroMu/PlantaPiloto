package icesi.plantapiloto.controlLayer.common.encoders;

import icesi.plantapiloto.controlLayer.common.entities.Message;
import com.google.gson.Gson;

public class JsonEncoder implements MessageEncoder {

    private Gson parser;

    public JsonEncoder() {
        parser = new Gson();
    }

    @Override
    public String encode(Message message) {
        return parser.toJson(message);
    }

    @Override
    public Message decode(String json) {
        return parser.fromJson(json, Message.class);
    }

}
