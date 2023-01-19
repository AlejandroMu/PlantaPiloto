package icesi.plantapiloto.controllers;

import java.util.List;

import com.zeroc.Ice.Current;

import icesi.plantapiloto.controlLayer.common.encoders.ObjectEncoder;
import icesi.plantapiloto.controlLayer.common.entities.Message;
import icesi.plantapiloto.model.Value;
import icesi.plantapiloto.services.ValueService;

public class ValueControllerImp implements ValueController {

    private ValueService service;
    private ObjectEncoder encoder;

    /**
     * @param encoder the encoder to set
     */
    public void setEncoder(ObjectEncoder encoder) {
        this.encoder = encoder;
    }

    @Override
    public String getValues(Current current) {
        List<Value> values = service.getValues();
        return encoder.encode(values);
    }

    @Override
    public void saveValues(String data, Current current) {
        Message message = encoder.decode(data, Message.class);
        service.addValues(message);

    }

    /**
     * @param service the service to set
     */
    public void setService(ValueService service) {
        this.service = service;
    }

}
