package icesi.plantapiloto;

import java.util.Date;

import icesi.plantapiloto.enityManager.Manager;
import icesi.plantapiloto.model.Channel;
import icesi.plantapiloto.model.ControllableDevice;
import icesi.plantapiloto.model.EntityWrapper;
import icesi.plantapiloto.model.IOModule;
import icesi.plantapiloto.model.Value;
import com.zeroc.Ice.Communicator;
import com.zeroc.Ice.Current;
import com.zeroc.Ice.Identity;
import com.zeroc.Ice.ObjectAdapter;
import com.zeroc.Ice.ObjectPrx;
import com.zeroc.Ice.Util;

public class Main {
    public static void main(String[] args) {
        Manager vManager = new Manager();
        Communicator communicator = Util.initialize(args, "model.config");
        ObjectAdapter adapter = communicator.createObjectAdapter("Model");
        adapter.add(vManager, Util.stringToIdentity("model"));
        adapter.activate();
        communicator.waitForShutdown();

    }
}
