package icesi.plantapiloto.modelManager.controllers;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;

import com.zeroc.Ice.Current;

import icesi.plantapiloto.MQTT.Publisher;
import icesi.plantapiloto.common.controllers.MeasurementManagerController;
import icesi.plantapiloto.common.dtos.MeasurementDTO;
import icesi.plantapiloto.common.encoders.JsonEncoder;
import icesi.plantapiloto.common.envents.PublisherI;
import icesi.plantapiloto.common.mappers.MeasurmentMapper;
import icesi.plantapiloto.common.model.Measurement;
import icesi.plantapiloto.modelManager.entityManager.ManagerPool;
import icesi.plantapiloto.modelManager.services.MeasurementService;

public class MeasurementController implements MeasurementManagerController {
    private PublisherI publisher;

    private MeasurementService service;

    public MeasurementController() {
    }

    /**
     * @param service the service to set
     */
    public void setService(MeasurementService service) {
        this.service = service;
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
        EntityManager manager = ManagerPool.getManager();
        manager.getTransaction().begin();
        service.saveMeasurements(value, manager);
        manager.getTransaction().commit();
        Map<Object, List<MeasurementDTO>> map = new HashMap<>();
        map = Arrays.asList(value).stream().collect(Collectors.groupingBy(t -> t.execId));
        Iterator<Object> keys = map.keySet().iterator();
        while (keys.hasNext()) {
            Integer execId = (Integer) keys.next();
            List<MeasurementDTO> data = map.get(execId);
            publisher.setTopic(execId + "");
            publisher.publish(data);
        }
        ManagerPool.close(manager);
    }

    @Override
    public MeasurementDTO[] getMeasurments(int assetId, long initdata, long endDate, Current current) {
        EntityManager manager = ManagerPool.getManager();
        manager.getTransaction().begin();
        List<Measurement> measurements = service.getMeasurements(assetId, initdata, endDate, manager);
        MeasurementDTO[] ret = MeasurmentMapper.getInstance().asEntityDTO(measurements).toArray(MeasurementDTO[]::new);
        manager.getTransaction().commit();
        ManagerPool.close(manager);
        return ret;
    }

    @Override
    public MeasurementDTO[] getMeasurmentsByExecution(int execId, Current current) {
        EntityManager manager = ManagerPool.getManager();
        manager.getTransaction().begin();
        List<Measurement> measurements = service.getMeasurementsByExecution(execId, manager);
        MeasurementDTO[] ret = MeasurmentMapper.getInstance().asEntityDTO(measurements).toArray(MeasurementDTO[]::new);

        manager.getTransaction().commit();
        ManagerPool.close(manager);
        return ret;

    }

}
