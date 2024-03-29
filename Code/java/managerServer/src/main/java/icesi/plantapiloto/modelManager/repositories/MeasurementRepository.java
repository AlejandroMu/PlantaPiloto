package icesi.plantapiloto.modelManager.repositories;

import java.sql.Timestamp;
import java.util.List;

import javax.persistence.EntityManager;

import icesi.plantapiloto.common.model.Measurement;

public class MeasurementRepository implements Repository<Measurement, Integer> {
    private static MeasurementRepository instance;

    private MeasurementRepository() {
    }

    public static MeasurementRepository getInstance() {
        if (instance == null) {
            instance = new MeasurementRepository();
        }
        return instance;
    }

    @Override
    public Class<Measurement> getType() {
        return Measurement.class;
    }

    public List<Measurement> findByAssetAndDateBetween(int assetId, long initdata, long endDate,
            EntityManager manager) {
        String query = "From Measurement m WHERE m.assetBean.id=?1 AND m.time between ?2 AND ?3";
        List<Measurement> values = executeQuery(manager, query, assetId, new Timestamp(initdata),
                new Timestamp(endDate));
        return values;
    }

    public List<Measurement> findByExecution(int execId, EntityManager manager) {
        String query = "From Measurement m WHERE m.executionBean.id=?1";
        List<Measurement> values = executeQuery(manager, query, execId);
        return values;
    }
}
