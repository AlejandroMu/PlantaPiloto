package icesi.plantapiloto.driverAsset.scheduler;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import icesi.plantapiloto.common.dtos.MeasurementDTO;
import icesi.plantapiloto.common.dtos.output.AssetDTO;
import icesi.plantapiloto.driverAsset.RMSender.PublisherManager;
import icesi.plantapiloto.driverAsset.concrete.DriverAssetConcrete;

public class TaskThread implements Runnable {

    private static final Logger logger = Logger.getLogger(TaskThread.class.getName());

    private DriverAssetConcrete source;
    private List<AssetDTO> assets;
    private int exeId;
    private String callback;
    private long period;
    private boolean share;

    /**
     * @param sender
     * @param source
     * @param assets
     * @param configs
     */
    public TaskThread(DriverAssetConcrete source, AssetDTO asset,
            int exeId, String callback, long period, boolean share) {
        this.source = source;
        this.assets = new ArrayList<>();
        assets.add(asset);
        this.exeId = exeId;
        this.callback = callback;
        this.period = period;
        this.share = share;
    }

    /**
     * @return the share
     */
    public boolean isShare() {
        return share;
    }

    public void addAsset(AssetDTO dto) {
        synchronized (assets) {
            assets.add(dto);
        }
    }

    public boolean contains(AssetDTO asset) {
        return assets.stream().anyMatch(a -> a.contains(asset));
    }

    /**
     * @return the period
     */
    public long getPeriod() {
        return period;
    }

    @Override
    public void run() {
        logger.info("Task executing, read execution: " + exeId + " freq: " + period);
        List<MeasurementDTO> measurementDTOs = null;
        try {
            synchronized (assets) {
                measurementDTOs = source.readAsset(assets, exeId);
                PublisherManager sender = PublisherManager.getInstance();
                sender.addMessage(measurementDTOs, callback);
            }
        } catch (Exception e) {
            logger.severe("Task fail, read execution: " + exeId + " freq: " + period);
        }

    }

}
