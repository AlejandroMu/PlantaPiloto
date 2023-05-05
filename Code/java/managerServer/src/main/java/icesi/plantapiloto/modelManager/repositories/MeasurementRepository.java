package icesi.plantapiloto.modelManager.repositories;

import icesi.plantapiloto.common.model.Measurement;

public class MeasurementRepository implements Repository<Measurement, Integer> {
    @Override
    public Class<Measurement> getType() {
        return Measurement.class;
    }
}
