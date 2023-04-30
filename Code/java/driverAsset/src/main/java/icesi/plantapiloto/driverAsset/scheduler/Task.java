package icesi.plantapiloto.driverAsset.scheduler;

import java.util.Map;
import java.util.TimerTask;

import icesi.plantapiloto.common.dtos.AssetDTO;
import icesi.plantapiloto.common.dtos.AssetValue;
import icesi.plantapiloto.common.dtos.MeasurementDTO;
import icesi.plantapiloto.driverAsset.RMSender.PublisherManager;
import icesi.plantapiloto.driverAsset.concrete.DriverAssetConcrete;

public class Task extends TimerTask {
    private DriverAssetConcrete source;
    private AssetDTO[] assets;
    private Map<String, String> configs;

    /**
     * @param sender
     * @param source
     * @param assets
     * @param configs
     */
    public Task(DriverAssetConcrete source, AssetDTO[] assets,
            Map<String, String> configs) {
        this.source = source;
        this.assets = assets;
        this.configs = configs;
    }

    @Override
    public void run() {
        MeasurementDTO[] measurementDTOs = source.readAsset(assets, configs);

        String callback = configs.get("asset.manager.proxy");
        String exeIdS = configs.get("execution.id");

        PublisherManager sender = PublisherManager.getInstance(callback);
        AssetValue value = new AssetValue(measurementDTOs, Integer.parseInt(exeIdS));
        sender.addMessage(value);
    }

}
