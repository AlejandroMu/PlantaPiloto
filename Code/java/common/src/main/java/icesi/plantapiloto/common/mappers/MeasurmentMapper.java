package icesi.plantapiloto.common.mappers;

import icesi.plantapiloto.common.dtos.MeasurementDTO;
import icesi.plantapiloto.common.model.Measurement;

public class MeasurmentMapper implements Maper<Measurement, MeasurementDTO> {
    private static MeasurmentMapper instance;

    public static MeasurmentMapper getInstance() {
        if (instance == null) {
            instance = new MeasurmentMapper();
        }
        return instance;
    }

    public MeasurementDTO asEntityDTO(Measurement value) {
        MeasurementDTO measurementDTO = new MeasurementDTO(value.getId(), value.getAssetBean().getName(),
                value.getValue(), value.getExecutionBean().getId(), value.getTime().getTime());
        return measurementDTO;
    }

}
