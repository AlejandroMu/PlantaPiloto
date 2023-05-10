package icesi.plantapiloto.common.mappers;

import icesi.plantapiloto.common.dtos.DriverDTO;
import icesi.plantapiloto.common.model.Driver;

public class DriverMapper implements Maper<Driver, DriverDTO> {
    private static DriverMapper instance;

    public static DriverMapper getInstance() {
        if (instance == null) {
            instance = new DriverMapper();
        }
        return instance;
    }

    private DriverMapper() {

    }

    @Override
    public DriverDTO asEntityDTO(Driver entity) {
        DriverDTO dto = new DriverDTO(entity.getId(), entity.getName(), entity.getServiceProxy(),
                entity.getWorkSpaceBean().getId());
        return dto;
    }

}
