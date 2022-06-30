package icesi.plantapiloto;

import java.util.Date;



import icesi.plantapiloto.enityManager.Manager;
import icesi.plantapiloto.model.Channel;
import icesi.plantapiloto.model.ControllableDevice;
import icesi.plantapiloto.model.IOModule;
import icesi.plantapiloto.model.Value;

public class Main {
    public static void main(String[] args) {
        Manager<Value> vManager = new Manager<>();

        ControllableDevice device = new ControllableDevice(1L, "type", "processor");
        IOModule module = new IOModule(1L, "name", "type", device);
        Channel channel= new Channel(1L, "type", "signal", "range", "unit", module);
        Value value= new Value(1l, new Date(), 2.4f, channel);

        Manager.begin();
        vManager.save(value);
        vManager.save(value);
        Manager.commit();
        
        Manager.manager.close();
    }
}
