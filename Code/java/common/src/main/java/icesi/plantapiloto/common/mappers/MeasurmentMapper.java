package icesi.plantapiloto.common.mappers;

import icesi.plantapiloto.common.dtos.MeasurementDTO;
import icesi.plantapiloto.common.model.Measurement;

public class MeasurmentMapper {
    private static MeasurmentMapper instance;

    public static MeasurmentMapper getInstance() {
        if (instance == null) {

        }
        return instance;
    }

    public Measurement[] asMeasurement(MeasurementDTO[] values) {

        return null;
    }

}
