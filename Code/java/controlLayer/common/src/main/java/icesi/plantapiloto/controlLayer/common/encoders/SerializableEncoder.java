package icesi.plantapiloto.controlLayer.common.encoders;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Base64;

import icesi.plantapiloto.controlLayer.common.entities.Message;

public class SerializableEncoder implements ObjectEncoder {

    @Override
    public <T> String encode(T message) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            Serializable ser = (Serializable) message;
            oos.writeObject(ser);
            oos.close();

            return Base64.getEncoder().encodeToString(baos.toByteArray());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public <T> T decode(String string, Class<T> type) {
        try {
            byte[] data = Base64.getDecoder().decode(string);
            ObjectInputStream ois = new ObjectInputStream(
                    new ByteArrayInputStream(data));
            Object o = ois.readObject();
            ois.close();

            return type.cast(o);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}