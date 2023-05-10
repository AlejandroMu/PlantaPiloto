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
import icesi.plantapiloto.common.dtos.DriverDTO;
import icesi.plantapiloto.common.dtos.MeasurementDTO;
import icesi.plantapiloto.common.dtos.TypeDTO;
import icesi.plantapiloto.common.dtos.output.AssetDTO;
import icesi.plantapiloto.common.encoders.JsonEncoder;
import icesi.plantapiloto.common.envents.PublisherI;
import icesi.plantapiloto.common.mappers.AssetMapper;
import icesi.plantapiloto.common.mappers.DriverMapper;
import icesi.plantapiloto.common.mappers.MeasurmentMapper;
import icesi.plantapiloto.common.mappers.TypeMapper;
import icesi.plantapiloto.common.model.Asset;
import icesi.plantapiloto.common.model.Driver;
import icesi.plantapiloto.common.model.Measurement;
import icesi.plantapiloto.common.model.MetaData;
import icesi.plantapiloto.common.model.Type;

public class AssetController implements AssetManagerController {

    private AssetService service;
    private TypeService typeService;
    private DriverService driverService;

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
        typeService = new TypeService();
        driverService = new DriverService();
    }

    /**
     * @param publisher the publisher to set
     */
    public void setPublisher(String host) {
        publisher = new Publisher();
        publisher.setHost(host);
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
        Asset assetN = service.createAsset(asset.getName(), asset.getDescription(),
                asset.getTypeBean() == null ? null : asset.getTypeBean().getId(),
                asset.getDriverBean() == null ? null : asset.getDriverBean().getId(),
                asset.getAsset() == null ? null : asset.getAsset().getId(),
                asset.getAssetState());
        List<MetaData> meta = asset.getMetaData();
        System.out.println("Add Asset: metadata: " + meta.size());
        if (meta != null) {
            service.addMetadata(assetN, meta.toArray(new MetaData[meta.size()]));
        }
    }

    @Override
    public AssetDTO[] findByType(int type, Current current) {
        Type t = new Type();
        t.setId(type);
        List<Asset> asset = service.getAssetsByType(t);
        return AssetMapper.getInstance().asEntityDTO(asset).toArray(AssetDTO[]::new);
    }

    @Override
    public AssetDTO[] findAll(Current current) {
        List<Asset> assets = service.getAssets();
        return AssetMapper.getInstance().asEntityDTO(assets).toArray(AssetDTO[]::new);

    }

    @Override
    public AssetDTO[] findByState(String state, Current current) {
        List<Asset> assets = service.getAssetsByState(state);
        return AssetMapper.getInstance().asEntityDTO(assets).toArray(AssetDTO[]::new);

    }

    @Override
    public void deletById(int id, Current current) {

        service.deletById(id);
    }

    @Override
    public MeasurementDTO[] getMeasurments(AssetDTO asset, long initdata, long endDate, Current current) {

        List<Measurement> measurements = service.getMeasurements(asset, initdata, endDate);
        return MeasurmentMapper.getInstance().asEntityDTO(measurements).toArray(MeasurementDTO[]::new);
    }

    @Override
    public void captureAssetsId(int[] assets, int execId, long period, Current current) {
        service.captureAssets(assets, callback, execId, period);
    }

    @Override
    public TypeDTO[] findAllTypes(Current current) {

        List<Type> types = typeService.findAll();
        return TypeMapper.getInstance().asEntityDTO(types).toArray(TypeDTO[]::new);
    }

    @Override
    public DriverDTO[] findAllDrivers(Current current) {

        List<Driver> drivers = driverService.findAll();
        return DriverMapper.getInstance().asEntityDTO(drivers).toArray(DriverDTO[]::new);
    }

    @Override
    public void stopCapture(int execId, Current current) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'stopCapture'");
    }

}
