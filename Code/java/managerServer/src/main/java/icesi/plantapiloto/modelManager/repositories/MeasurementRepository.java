package icesi.plantapiloto.modelManager.repositories;

import java.sql.Timestamp;
import java.util.List;

import javax.persistence.EntityManager;

import icesi.plantapiloto.common.model.Asset;
import icesi.plantapiloto.common.model.Measurement;

public class MeasurementRepository implements Repository<Measurement, Integer> {
    private static MeasurementRepository instance;
    private EntityManager manager;

    private MeasurementRepository() {
        manager = managerFactory.createEntityManager();
    }

    public static MeasurementRepository getInstance() {
        if (instance == null) {
            instance = new MeasurementRepository();
        }
        return instance;
    }

    @Override
    public EntityManager getManager() {
        return manager;
    }

    @Override
    public Class<Measurement> getType() {
        return Measurement.class;
    }

    public List<Measurement> findByAssetAndDateBetween(Asset asset, long initdata, long endDate) {
        String query = "From Measurement m WHERE m.assetBean.id=?1 AND m.time between ?2 AND ?3";
        List<Measurement> values = executeQuery(query, asset.getId(), new Timestamp(initdata), new Timestamp(endDate));
        return values;
    }
}
