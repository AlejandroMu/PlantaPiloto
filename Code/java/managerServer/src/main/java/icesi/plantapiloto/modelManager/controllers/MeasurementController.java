package icesi.plantapiloto.modelManager.controllers;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.zeroc.Ice.Current;

import icesi.plantapiloto.MQTT.Publisher;
import icesi.plantapiloto.common.controllers.MeasurementManagerController;
import icesi.plantapiloto.common.dtos.MeasurementDTO;
import icesi.plantapiloto.common.dtos.output.AssetDTO;
import icesi.plantapiloto.common.encoders.JsonEncoder;
import icesi.plantapiloto.common.envents.PublisherI;
import icesi.plantapiloto.common.mappers.MeasurmentMapper;
import icesi.plantapiloto.common.model.Measurement;
import icesi.plantapiloto.modelManager.services.MeasurementService;

public class MeasurementController implements MeasurementManagerController {
    private PublisherI publisher;

    private MeasurementService service;

    public MeasurementController() {
        service = new MeasurementService();
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
    public MeasurementDTO[] getMeasurments(AssetDTO asset, long initdata, long endDate, Current current) {

        List<Measurement> measurements = service.getMeasurements(asset, initdata, endDate);
        return MeasurmentMapper.getInstance().asEntityDTO(measurements).toArray(MeasurementDTO[]::new);
    }

}
