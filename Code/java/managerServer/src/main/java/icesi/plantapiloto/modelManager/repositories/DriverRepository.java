package icesi.plantapiloto.modelManager.repositories;

import icesi.plantapiloto.common.model.Driver;

public class DriverRepository implements Repository<Driver, Integer> {
    @Override
    public Class<Driver> getType() {
        return Driver.class;
    }
}
