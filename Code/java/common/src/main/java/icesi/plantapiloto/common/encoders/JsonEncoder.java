package icesi.plantapiloto.common.encoders;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class JsonEncoder implements ObjectEncoder {

    private Gson parser;

    public JsonEncoder() {

        parser = new GsonBuilder().serializeSpecialFloatingPointValues().create();
    }

    public <T> String encodePretty(T message) {
        GsonBuilder builder = new GsonBuilder();
        String ret = builder.setPrettyPrinting().serializeSpecialFloatingPointValues().create().toJson(message);
        return ret;
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
