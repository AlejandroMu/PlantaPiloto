package icesi.plantapiloto.driverAsset.scheduler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import com.zeroc.Ice.Communicator;

import icesi.plantapiloto.common.dtos.output.AssetDTO;
import icesi.plantapiloto.driverAsset.DriverAssetImp;
import icesi.plantapiloto.driverAsset.concrete.DriverAssetConcrete;

public class ScheduleManager {

    private ScheduledExecutorService scheduler;
    private Map<Integer, List<TaskThread>> process;
    private Map<Integer, List<ScheduledFuture<?>>> futures;

    public ScheduleManager() {
        Communicator communicator = DriverAssetImp.communicator;
        String poolSize = communicator.getProperties().getProperty(DriverAssetImp.THREADPOOL_SIZE);
        int ps = poolSize != null ? Integer.parseInt(poolSize) : 5;
        scheduler = Executors.newScheduledThreadPool(ps);
        process = new HashMap<>();
        futures = new HashMap<>();
    }

    public void addProcess(AssetDTO asset, int execId, long period, DriverAssetConcrete concrete,
            String callback, boolean newProcess) {
        List<TaskThread> tasks = process.get(execId);
        boolean control = newProcess;
        if (tasks == null) {
            tasks = new ArrayList<>();
            control = true;
        }

        if (!control) {
            Optional<TaskThread> task = tasks.stream().filter(t -> t.getPeriod() == period && t.isShare()).findFirst();
            if (task.isPresent()) {
                task.get().addAsset(asset);
                return;
            }
        }
        TaskThread task = new TaskThread(concrete, asset, execId, callback, period, !newProcess);
        ScheduledFuture<?> future = scheduler.scheduleAtFixedRate(task, period, period,
                TimeUnit.MILLISECONDS);
        List<ScheduledFuture<?>> futs = futures.get(execId);
        if (futs == null) {
            futs = new ArrayList<>();
        }
        futs.add(future);
        futures.put(execId, futs);
        tasks.add(task);
        process.put(execId, tasks);

    }

    public void stopProcess(int execId) {
        List<ScheduledFuture<?>> futs = futures.get(execId);
        if (futs != null) {
            System.out.println("Stop process: " + execId);

            futs.stream().forEach(t -> t.cancel(false));
            process.remove(execId);
            futures.remove(execId);
        }
    }

    public int getExecutionOf(AssetDTO asset) {
        Iterator<Integer> execIds = process.keySet().iterator();
        while (execIds.hasNext()) {
            int execId = execIds.next();
            List<TaskThread> tasks = process.get(execId);
            boolean contains = tasks.stream().anyMatch(t -> t.contains(asset));
            if (contains) {
                return execId;
            }
        }
        return -1;
    }

}
