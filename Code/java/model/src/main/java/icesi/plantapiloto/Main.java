package icesi.plantapiloto;

import java.util.List;

import com.zeroc.Ice.Communicator;
import com.zeroc.Ice.ObjectAdapter;
import com.zeroc.Ice.Util;

import icesi.plantapiloto.controlLayer.common.encoders.JsonEncoder;
import icesi.plantapiloto.controllers.ValueControllerImp;
import icesi.plantapiloto.model.Value;
import icesi.plantapiloto.repositories.ChannelRepository;
import icesi.plantapiloto.repositories.ValueRepository;
import icesi.plantapiloto.services.ValueService;

public class Main {
    public static void main(String[] args) {

        ValueService service = new ValueService();
        service.setChannelRepository(new ChannelRepository());
        service.setValueRepository(new ValueRepository());
        ValueControllerImp valueControllerImp = new ValueControllerImp();
        valueControllerImp.setService(service);

        JsonEncoder encoder = new JsonEncoder();
        valueControllerImp.setEncoder(encoder);
        Communicator communicator = Util.initialize(args, "model.config");
        ObjectAdapter adapter = communicator.createObjectAdapter("Model");
        adapter.add(valueControllerImp, Util.stringToIdentity("valueController"));
        adapter.activate();
        communicator.waitForShutdown();
        communicator.close();

    }
}
