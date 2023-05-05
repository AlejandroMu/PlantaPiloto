package icesi.plantapiloto.driverAsset.scheduler;

import java.util.HashMap;
import java.util.Map;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.zeroc.Ice.Communicator;

import icesi.plantapiloto.common.dtos.output.AssetDTO;
import icesi.plantapiloto.driverAsset.DriverAssetImp;
import icesi.plantapiloto.driverAsset.concrete.DriverAssetConcrete;

public class ScheduleManager {

    public static final String PERIOD_KEY = "execution.asset.period";
    public static final String EXECUTION_ID_KEY = "execution.id";

    private ScheduledExecutorService scheduler;
    private Map<Integer, TimerTask> process;

    public ScheduleManager() {
        Communicator communicator = DriverAssetImp.communicator;
        String poolSize = communicator.getProperties().getProperty(DriverAssetImp.THREADPOOL_SIZE);
        int ps = poolSize != null ? Integer.parseInt(poolSize) : 5;
        scheduler = Executors.newScheduledThreadPool(ps);
        process = new HashMap<>();
    }

    public void addProcess(AssetDTO[] asset, int execId, long period, DriverAssetConcrete concrete,
            String callback) {

        TimerTask task = new Task(concrete, asset, execId, callback);
        scheduler.scheduleAtFixedRate(task, 0, period,
                TimeUnit.MILLISECONDS);
        process.put(execId, task);
    }

    public void stopProcess(int execId) {
        TimerTask task = process.get(execId);
        if (task != null) {
            task.cancel();
            process.remove(execId);
        }
    }

}
