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

        // ControllableDevice device = new ControllableDevice(1L, "type", "processor");
        // IOModule module = new IOModule(1L, "name", "type", device);
        // Channel channel = new Channel(1L, "type", "signal", "range", "unit", module);
        // Value value = new Value(1l, new Date(), 2.4f, channel);
        // EntityWrapper<Value> wr = new EntityWrapper<>();
        // wr.setObject(value);
        // vManager.begin(null);
        // vManager.save(wr, null);
        // vManager.save(wr, null);
        // vManager.commit(null);
        Communicator communicator = Util.initialize(args, "model.config");
        ObjectAdapter adapter = communicator.createObjectAdapter("Model");
        adapter.add(vManager, Util.stringToIdentity("model"));
        adapter.activate();
        communicator.waitForShutdown();

    }
}
