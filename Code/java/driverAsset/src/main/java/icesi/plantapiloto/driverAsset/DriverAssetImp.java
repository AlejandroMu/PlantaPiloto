package icesi.plantapiloto.driverAsset;

import java.util.Map;

import com.zeroc.Ice.Communicator;
import com.zeroc.Ice.Current;
import com.zeroc.Ice.ObjectAdapter;
import com.zeroc.Ice.Util;

import icesi.plantapiloto.driverAsset.RMSender.PublisherManager;
import icesi.plantapiloto.driverAsset.concrete.DriverAssetConcrete;
import icesi.plantapiloto.driverAsset.scheduler.ScheduleManager;
import icesi.plantapiloto.modelManager.controllers.AssetDTO;
import icesi.plantapiloto.modelManager.controllers.AssetManagerCallbackPrx;
import icesi.plantapiloto.modelManager.controllers.DriverAsset;

public class DriverAssetImp implements DriverAsset {

    public static Communicator communicator;

    public static void initServices(Class<? extends DriverAssetConcrete> clasz) {
        communicator = Util.initialize(null, "application.properties");
        DriverAsset driverAsset = new DriverAssetImp(clasz);
        String pr = communicator.getProperties().getProperty("driver.asset.Endpoints");
        if (pr == null) {
            pr = "tcp -h * -p 8041";
        }
        ObjectAdapter adapter = communicator.createObjectAdapterWithEndpoints("DriverService", pr);
        adapter.add(driverAsset, Util.stringToIdentity("DriverAsset"));
        adapter.activate();
        communicator.waitForShutdown();

    }

    private ScheduleManager manager;
    private Class<? extends DriverAssetConcrete> type;

    public DriverAssetImp(Class<? extends DriverAssetConcrete> type) {
        this.manager = new ScheduleManager();
        this.type = type;
    }

    @Override
    public void readAsset(AssetDTO[] asset, Map<String, String> config, AssetManagerCallbackPrx server,
            Current current) {
        String proxy = server.ice_getEndpoints()[0]._toString();
        PublisherManager.addInstance(server, proxy);
        config.put("asset.manager.proxy", proxy);
        manager.addProcess(asset, config, getConcreteInstance());

    }

    @Override
    public void stopRead(int exeId, Current current) {
        manager.stopProcess(exeId);
    }

    @Override
    public void setPointAsset(AssetDTO asset, double value, Map<String, String> config, Current current) {
        DriverAssetConcrete obj = getConcreteInstance();
        obj.setPointAsset(asset, value, config);
    }

    public DriverAssetConcrete getConcreteInstance() {
        DriverAssetConcrete obj = null;
        try {
            obj = (DriverAssetConcrete) type.getConstructor().newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return obj;
    }

}
