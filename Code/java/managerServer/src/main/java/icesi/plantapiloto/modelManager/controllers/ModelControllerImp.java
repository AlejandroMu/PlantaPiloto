package icesi.plantapiloto.modelManager.controllers;

import java.util.List;

import com.zeroc.Ice.Current;

import icesi.plantapiloto.common.encoders.ObjectEncoder;
import icesi.plantapiloto.common.entities.Message;
import icesi.plantapiloto.modelManager.dtos.ListOut;
import icesi.plantapiloto.modelManager.dtos.ValueQuery;
import icesi.plantapiloto.modelManager.model.Value;
import icesi.plantapiloto.modelManager.services.ValueService;

public class ModelControllerImp {

    private ValueService service;
    private ObjectEncoder encoder;

    /**
     * @param encoder the encoder to set
     */
    public void setEncoder(ObjectEncoder encoder) {
        this.encoder = encoder;
    }

    /**
     * @param service the service to set
     */
    public void setService(ValueService service) {
        this.service = service;
    }

    public String getValues(String data, Current current) {
        ValueQuery query = encoder.decode(data, ValueQuery.class);
        List<Value> values = service.getValues(query);
        ListOut<Value> out = new ListOut<>();
        out.setEllements(values);
        String ret = encoder.encode(out);
        return ret;
    }

    public void saveValues(String data, Current current) {
        Message msg = encoder.decode(data, Message.class);
        service.addValues(msg);
    }

    public void initCaptureFromPlc(String data, Current current) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'initCaptureFromPlc'");
    }

    public void stopCapture(String data, Current current) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'stopCapture'");
    }

    public void changeSetPoint(String data, Current current) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'changeSetPoint'");
    }

}
