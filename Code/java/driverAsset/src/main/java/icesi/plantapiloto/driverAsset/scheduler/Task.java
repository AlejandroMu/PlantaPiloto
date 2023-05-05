package icesi.plantapiloto.driverAsset.scheduler;

import java.util.TimerTask;

import icesi.plantapiloto.common.dtos.MeasurementDTO;
import icesi.plantapiloto.common.dtos.output.AssetDTO;
import icesi.plantapiloto.driverAsset.RMSender.PublisherManager;
import icesi.plantapiloto.driverAsset.concrete.DriverAssetConcrete;

public class Task extends TimerTask {
    private DriverAssetConcrete source;
    private AssetDTO[] assets;
    private int exeId;
    private String callback;

    /**
     * @param sender
     * @param source
     * @param assets
     * @param configs
     */
    public Task(DriverAssetConcrete source, AssetDTO[] assets,
            int exeId, String callback) {
        this.source = source;
        this.assets = assets;
        this.exeId = exeId;
        this.callback = callback;
    }

    @Override
    public void run() {
        MeasurementDTO[] measurementDTOs = source.readAsset(assets, exeId);

        PublisherManager sender = PublisherManager.getInstance(callback);
        sender.addMessage(measurementDTOs);
    }

}
