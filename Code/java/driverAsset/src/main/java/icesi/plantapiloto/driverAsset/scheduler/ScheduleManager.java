package icesi.plantapiloto.driverAsset.scheduler;

import java.util.HashMap;
import java.util.Map;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.zeroc.Ice.Communicator;

import icesi.plantapiloto.common.dtos.AssetDTO;
import icesi.plantapiloto.driverAsset.DriverAssetImp;
import icesi.plantapiloto.driverAsset.concrete.DriverAssetConcrete;

public class ScheduleManager {

    private ScheduledExecutorService scheduler;
    private Map<Integer, TimerTask> process;

    public ScheduleManager() {
        Communicator communicator = DriverAssetImp.communicator;
        String poolSize = communicator.getProperties().getProperty("driver.asset.threadpool.size");
        int ps = poolSize != null ? Integer.parseInt(poolSize) : 5;
        scheduler = Executors.newScheduledThreadPool(ps);
        process = new HashMap<>();
    }

    public void addProcess(AssetDTO[] asset, Map<String, String> config, DriverAssetConcrete concrete) {
        TimerTask task = new Task(concrete, asset, config);
        String period = config.get("execution.asset.period");
        String execId = config.get("execution.id");
        scheduler.scheduleAtFixedRate(task, 0, Long.parseLong(period),
                TimeUnit.MILLISECONDS);
        process.put(Integer.parseInt(execId), task);
    }

    public void stopProcess(int execId) {
        TimerTask task = process.get(execId);
        if (task != null) {
            task.cancel();
            process.remove(execId);
        }
    }

}
