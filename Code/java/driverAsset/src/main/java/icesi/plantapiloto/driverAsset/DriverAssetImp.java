package icesi.plantapiloto.driverAsset;

import java.sql.Timestamp;
import java.util.List;

import com.zeroc.Ice.Communicator;
import com.zeroc.Ice.Current;
import com.zeroc.Ice.Endpoint;
import com.zeroc.Ice.ObjectAdapter;
import com.zeroc.Ice.Util;

import icesi.plantapiloto.common.controllers.DriverAsset;
import icesi.plantapiloto.common.controllers.MeasurementManagerControllerPrx;
import icesi.plantapiloto.common.dtos.output.AssetDTO;
import icesi.plantapiloto.common.encoders.JsonEncoder;
import icesi.plantapiloto.driverAsset.concrete.DriverAssetConcrete;
import icesi.plantapiloto.driverAsset.model.DataManager;
import icesi.plantapiloto.driverAsset.model.Task;
import icesi.plantapiloto.driverAsset.scheduler.ScheduleManager;

public class DriverAssetImp implements DriverAsset {

    public static Communicator communicator;
    public static final String ENDPOINT_STRING = "driver.asset.port";
    public static final String THREADPOOL_SIZE = "driver.asset.threadpool.size";

    public static void initServices(Class<? extends DriverAssetConcrete> clasz, String filename) {
        System.out.println("Class dirver: " + clasz.getSimpleName());
        communicator = Util.initialize(null, filename);
        DriverAssetImp driverAsset = new DriverAssetImp(clasz);
        String pr = communicator.getProperties().getProperty(ENDPOINT_STRING);
        String port = "1804";
        if (pr != null) {
            port = pr;
        }
        ObjectAdapter adapter = communicator.createObjectAdapterWithEndpoints("DriverService", "tcp -h * -p " + port);
        adapter.add(driverAsset, Util.stringToIdentity("DriverAsset"));
        adapter.activate();
        driverAsset.loadData();

        communicator.waitForShutdown();

    }

    private ScheduleManager manager;
    private Class<? extends DriverAssetConcrete> type;
    private DataManager dataManager;
    private JsonEncoder encoder;

    public DriverAssetImp(Class<? extends DriverAssetConcrete> type) {
        this.manager = new ScheduleManager();
        dataManager = new DataManager();
        encoder = new JsonEncoder();
        this.type = type;
    }

    // read task incompletes from database
    public void loadData() {

        List<Task> tasks = dataManager.getTasks();
        if (tasks.size() > 0) {
            System.out.println(new Timestamp(System.currentTimeMillis()).toString() + ": iniciando procesos activos");
            for (Task task : tasks) {
                if (task.getState().equals("r")) {
                    AssetDTO dto = encoder.decode(task.getAssets(), AssetDTO.class);
                    manager.addProcess(dto, task.getExecId(), task.getPeriod(), getConcreteInstance(), task.getServer(),
                            task.getIsShare() == 1);
                }
            }
        }

    }

    @Override
    public int setPointAsset(AssetDTO asset, double value, Current current) {
        DriverAssetConcrete obj = getConcreteInstance();
        obj.setPointAsset(asset, value);
        int execId = manager.getExecutionOf(asset);
        return execId;
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
    public void readAsset(AssetDTO asset, MeasurementManagerControllerPrx server, int execId, long period,
            boolean newProcess, Current current) {
        String proxy = server.ice_getIdentity().name;
        Endpoint endpoints[] = server.ice_getEndpoints();
        for (Endpoint endpoint : endpoints) {
            proxy += ":" + endpoint._toString();
        }
        manager.addProcess(asset, execId, period, getConcreteInstance(), proxy, newProcess);

        String assString = encoder.encode(asset);
        Task t = new Task(0, period, execId, assString, proxy, newProcess ? 1 : 0, "r");
        dataManager.saveTask(t);
    }

    @Override
    public void pauseReader(int execId, Current current) {
        manager.stopProcess(execId);
        dataManager.setState(execId, "p");
    }

    @Override
    public void resumeReader(int execId, Current current) {
        List<Task> tasks = dataManager.getTasksByExecId(execId);
        for (Task task : tasks) {
            if (task.getState().equals("p")) {
                AssetDTO dto = encoder.decode(task.getAssets(), AssetDTO.class);
                manager.addProcess(dto, task.getExecId(), task.getPeriod(), getConcreteInstance(), task.getServer(),
                        task.getIsShare() == 1);
            }
        }
        dataManager.setState(execId, "r");
    }

    @Override
    public void stopRead(int exeId, Current current) {
        manager.stopProcess(exeId);
        dataManager.removeByExecId(exeId);
    }
}
