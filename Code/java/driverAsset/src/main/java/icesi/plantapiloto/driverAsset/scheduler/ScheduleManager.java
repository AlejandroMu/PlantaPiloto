package icesi.plantapiloto.driverAsset.scheduler;

import java.util.ArrayList;
import java.util.HashMap;
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
    private Map<Integer, List<Task>> process;
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
        List<Task> tasks = process.get(execId);
        if (tasks == null) {
            tasks = new ArrayList<>();
            newProcess = true;
        }

        if (!newProcess) {
            Optional<Task> task = tasks.stream().filter(t -> t.getPeriod() == period).findFirst();
            if (task.isPresent()) {
                task.get().addAsset(asset);
                return;
            }
        }
        Task task = new Task(concrete, asset, execId, callback, period);
        ScheduledFuture<?> future = scheduler.scheduleAtFixedRate(task, 0, period,
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
        System.out.println("Stop process: " + execId);
        List<ScheduledFuture<?>> futs = futures.get(execId);
        if (futs != null) {
            System.out.println("Stop process not null: " + execId);

            futs.stream().forEach(t -> t.cancel(false));
            process.remove(execId);
            futures.remove(execId);
        }
    }

    public void pauseProcess(int execId) {
        System.out.println("pause process: " + execId);
        List<Task> futs = process.get(execId);
        if (futs != null) {
            futs.stream().forEach(t -> t.setRunning(false));
        }
    }

    public void resumeProcess(int execId) {
        System.out.println("resume process: " + execId);
        List<Task> futs = process.get(execId);
        if (futs != null) {
            futs.stream().forEach(t -> t.setRunning(true));
        }
    }

}
