package icesi.plantapiloto.modelManager.assetsManager;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.zeroc.Ice.Current;

import icesi.plantapiloto.MQTT.Publisher;
import icesi.plantapiloto.common.controllers.AssetManagerCallbackPrx;
import icesi.plantapiloto.common.controllers.AssetManagerController;
import icesi.plantapiloto.common.dtos.MeasurementDTO;
import icesi.plantapiloto.common.dtos.output.AssetDTO;
import icesi.plantapiloto.common.encoders.JsonEncoder;
import icesi.plantapiloto.common.envents.PublisherI;
import icesi.plantapiloto.common.mappers.AssetMapper;
import icesi.plantapiloto.common.model.Asset;
import icesi.plantapiloto.common.model.MetaData;
import icesi.plantapiloto.common.model.Type;
import icesi.plantapiloto.modelManager.Main;

public class AssetController implements AssetManagerController {

    public static final String DRIVER_ENDPOINT = "service.proxy";

    private AssetService service;
    private PublisherI publisher;
    private AssetManagerCallbackPrx callback;

    /**
     * @return the callback
     */
    public AssetManagerCallbackPrx getCallback() {
        return callback;
    }

    /**
     * @param callback the callback to set
     */
    public void setCallback(AssetManagerCallbackPrx callback) {
        this.callback = callback;
    }

    public AssetController() {
        service = new AssetService();
        publisher = new Publisher();
        publisher.setHost(Main.communicator.getProperties().getProperty("mqtt.host"));
        publisher.setName("AssetController");
        publisher.setEncoder(new JsonEncoder());
        publisher.connect();
    }

    @Override
    public void saveAssetValue(MeasurementDTO[] value, Current current) {
        service.saveMeasurements(value);
        Map<Object, List<MeasurementDTO>> map = new HashMap<>();
        map = Arrays.asList(value).stream().collect(Collectors.groupingBy(t -> t.exeId));
        Iterator<Object> keys = map.keySet().iterator();
        while (keys.hasNext()) {
            Integer execId = (Integer) keys.next();
            List<MeasurementDTO> data = map.get(execId);
            publisher.setTopic(execId + "");
            publisher.publish(data);
        }
    }

    @Override
    public void captureAssets(Asset[] assets, int execId, long period, Current current) {
        service.captureAssets(assets, callback, execId, period);
    }

    @Override
    public void changeAssetValue(AssetDTO asset, double value, Current current) {
        service.changeSetPoint(asset, value);
    }

    @Override
    public void saveAsset(Asset asset, Current current) {
        asset = service.createAsset(asset.getName(), asset.getDescription(),
                asset.getTypeBean() == null ? null : asset.getTypeBean().getId(),
                asset.getDriverBean() == null ? null : asset.getDriverBean().getId(),
                asset.getAsset() == null ? null : asset.getAsset().getId(),
                asset.getAssetState());
        List<MetaData> meta = asset.getMetaData();
        if (meta != null) {
            service.addMetadata(asset, (MetaData[]) meta.toArray());
        }
    }

    @Override
    public AssetDTO[] findByType(Type type, Current current) {
        List<Asset> asset = service.getAssetsByType(type);
        return AssetMapper.getInstance().asAssetDto((Asset[]) asset.toArray());
    }

    @Override
    public AssetDTO[] findByState(String state, Current current) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findByState'");
    }

    @Override
    public void deletById(int id, Current current) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'deletById'");
    }

    @Override
    public MeasurementDTO[] getMeasurments(AssetDTO asset, int initdata, int endDate, Current current) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getMeasurments'");
    }

    @Override
    public void captureAssetsId(int[] assets, int execId, long period, Current current) {
        service.captureAssets(assets, callback, execId, period);
    }

}
