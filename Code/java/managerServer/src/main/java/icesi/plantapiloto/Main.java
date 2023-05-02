package icesi.plantapiloto;

import java.util.logging.Level;
import java.util.logging.Logger;

public class Main {

    public static void main(String[] args) {
        Logger.getLogger("Main").log(Level.INFO, "run managerServer");
        System.err.println("run ManagerServer");
        throw new RuntimeException("error");
        // ValueService service = new ValueService();
        // service.setChannelRepository(new ChannelRepository());
        // service.setValueRepository(new ValueRepository());
        // ModelControllerImp valueControllerImp = new ModelControllerImp();
        // valueControllerImp.setService(service);

        // JsonEncoder encoder = new JsonEncoder();
        // valueControllerImp.setEncoder(encoder);
        // Communicator communicator = Util.initialize(args, "model.config");
        // ObjectAdapter adapter = communicator.createObjectAdapter("Model");
        // adapter.add(valueControllerImp, Util.stringToIdentity("Model"));
        // adapter.activate();
        // communicator.waitForShutdown();
        // communicator.close();

    }
}
