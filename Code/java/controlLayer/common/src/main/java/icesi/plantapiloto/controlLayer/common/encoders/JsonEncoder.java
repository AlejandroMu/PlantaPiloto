package icesi.plantapiloto.controlLayer.common.encoders;

import com.google.gson.Gson;

public class JsonEncoder implements ObjectEncoder {

    private Gson parser;

    public JsonEncoder() {
        parser = new Gson();
    }

    @Override
    public <T> String encode(T message) {
        return parser.toJson(message);
    }

    @Override
    public <T> T decode(String json, Class<T> type) {
        return parser.fromJson(json, type);
    }

}
