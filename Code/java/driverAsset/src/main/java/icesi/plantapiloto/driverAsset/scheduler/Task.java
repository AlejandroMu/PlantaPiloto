package icesi.plantapiloto.driverAsset.scheduler;

import java.util.ArrayList;
import java.util.List;

import icesi.plantapiloto.common.dtos.MeasurementDTO;
import icesi.plantapiloto.common.dtos.output.AssetDTO;
import icesi.plantapiloto.driverAsset.RMSender.PublisherManager;
import icesi.plantapiloto.driverAsset.concrete.DriverAssetConcrete;

public class Task implements Runnable {
    private DriverAssetConcrete source;
    private List<AssetDTO> assets;
    private int exeId;
    private String callback;
    private long period;
    private boolean isRunning;

    /**
     * @param sender
     * @param source
     * @param assets
     * @param configs
     */
    public Task(DriverAssetConcrete source, AssetDTO asset,
            int exeId, String callback, long period) {
        this.source = source;
        this.assets = new ArrayList<>();
        assets.add(asset);
        this.exeId = exeId;
        this.callback = callback;
        this.period = period;
        isRunning = true;
    }

    public void addAsset(AssetDTO dto) {
        assets.add(dto);
    }

    /**
     * @return the period
     */
    public long getPeriod() {
        return period;
    }

    /**
     * @param isRunning the isRunning to set
     */
    public void setRunning(boolean isRunning) {
        this.isRunning = isRunning;
    }

    @Override
    public void run() {
        if (isRunning) {
            List<MeasurementDTO> measurementDTOs = source.readAsset(assets, exeId);
            PublisherManager sender = PublisherManager.getInstance(callback);
            sender.addMessage(measurementDTOs);
        }
    }

}
