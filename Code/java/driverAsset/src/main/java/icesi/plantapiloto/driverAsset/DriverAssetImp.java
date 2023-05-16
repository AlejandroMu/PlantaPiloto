package icesi.plantapiloto.driverAsset;

import com.zeroc.Ice.Communicator;
import com.zeroc.Ice.Current;
import com.zeroc.Ice.ObjectAdapter;
import com.zeroc.Ice.Util;

import icesi.plantapiloto.common.controllers.DriverAsset;
import icesi.plantapiloto.common.controllers.MeasurementManagerControllerPrx;
import icesi.plantapiloto.common.dtos.output.AssetDTO;
import icesi.plantapiloto.driverAsset.RMSender.PublisherManager;
import icesi.plantapiloto.driverAsset.concrete.DriverAssetConcrete;
import icesi.plantapiloto.driverAsset.scheduler.ScheduleManager;

public class DriverAssetImp implements DriverAsset {

    public static Communicator communicator;
    public static final String ENDPOINT_STRING = "driver.asset.Endpoints";
    public static final String THREADPOOL_SIZE = "driver.asset.threadpool.size";

    public static void initServices(Class<? extends DriverAssetConcrete> clasz, String filename) {
        System.out.println("Class dirver: " + clasz.getSimpleName());
        communicator = Util.initialize(null, filename);
        DriverAsset driverAsset = new DriverAssetImp(clasz);
        String pr = communicator.getProperties().getProperty(ENDPOINT_STRING);
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
    public void readAsset(AssetDTO asset, MeasurementManagerControllerPrx server, int execId, long period,
            boolean newProcess, Current current) {
        String proxy = server.ice_getEndpoints()[0]._toString();
        PublisherManager.addInstance(server, proxy);
        manager.addProcess(asset, execId, period, getConcreteInstance(), proxy, newProcess);

    }

    @Override
    public void stopRead(int exeId, Current current) {
        manager.stopProcess(exeId);
    }

    @Override
    public void setPointAsset(AssetDTO asset, double value, Current current) {
        DriverAssetConcrete obj = getConcreteInstance();
        obj.setPointAsset(asset, value);
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

    @Override
    public void pauseReader(int execId, Current current) {
        manager.pauseProcess(execId);
    }

    @Override
    public void resumeReader(int execId, Current current) {
        manager.resumeProcess(execId);
    }

}
