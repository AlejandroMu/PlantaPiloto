package icesi.plantapiloto.modelManager.services;

import java.sql.Timestamp;
import java.util.List;

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

    public void saveMeasurements(MeasurementDTO[] dto) {
        for (MeasurementDTO measurementDTO : dto) {
            Timestamp timestamp = new Timestamp(measurementDTO.timeStamp);
            Execution execution = executionRepository.findById(measurementDTO.execId).get();
            Asset asset = assetRepository.findById(measurementDTO.assetId).get();
            Measurement measurement = new Measurement();
            measurement.setAssetBean(asset);
            measurement.setExecutionBean(execution);
            measurement.setTime(timestamp);
            measurement.setValue(measurementDTO.value);
            measurementRepository.save(measurement);
        }
    }

    public List<Measurement> getMeasurements(int assetId, long initdata, long endDate) {
        List<Measurement> measurements = measurementRepository.findByAssetAndDateBetween(assetId, initdata, endDate);
        return measurements;
    }

    public List<Measurement> getMeasurementsByExecution(int execId) {
        List<Measurement> measurements = measurementRepository.findByExecution(execId);

        return measurements;
    }
}
