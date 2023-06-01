package icesi.plantapiloto.modelManager.services;

import java.sql.Timestamp;
import java.util.List;

import javax.persistence.EntityManager;

import icesi.plantapiloto.common.dtos.MeasurementDTO;
import icesi.plantapiloto.common.model.Asset;
import icesi.plantapiloto.common.model.Execution;
import icesi.plantapiloto.common.model.Measurement;
import icesi.plantapiloto.modelManager.repositories.AssetRepository;
import icesi.plantapiloto.modelManager.repositories.ExecutionRepository;
import icesi.plantapiloto.modelManager.repositories.MeasurementRepository;

public class MeasurementService {

    private AssetRepository assetRepository;
    private MeasurementRepository measurementRepository;
    private ExecutionRepository executionRepository;

    public MeasurementService() {
        assetRepository = AssetRepository.getInstance();
        measurementRepository = MeasurementRepository.getInstance();
        executionRepository = ExecutionRepository.getInstance();
    }

    public void saveMeasurements(MeasurementDTO[] dto, EntityManager manager) {
        for (MeasurementDTO measurementDTO : dto) {
            Timestamp timestamp = new Timestamp(measurementDTO.timeStamp);
            Execution execution = executionRepository.findById(measurementDTO.execId, manager).get();
            Asset asset = assetRepository.findById(measurementDTO.assetId, manager).get();
            Measurement measurement = new Measurement();
            measurement.setAssetBean(asset);
            measurement.setExecutionBean(execution);
            measurement.setTime(timestamp);
            measurement.setValue(measurementDTO.value);
            measurementRepository.save(measurement, manager);
        }
    }

    public List<Measurement> getMeasurements(int assetId, long initdata, long endDate, EntityManager manager) {
        List<Measurement> measurements = measurementRepository.findByAssetAndDateBetween(assetId, initdata, endDate,
                manager);
        return measurements;
    }

    public List<Measurement> getMeasurementsByExecution(int execId, EntityManager manager) {
        List<Measurement> measurements = measurementRepository.findByExecution(execId, manager);

        return measurements;
    }
}
