package icesi.plantapiloto.modelManager.assetsManager;

import java.util.List;

import icesi.plantapiloto.common.model.Driver;
import icesi.plantapiloto.modelManager.repositories.DriverRepository;

public class DriverService {

    private DriverRepository repository;

    public DriverService() {
        repository = DriverRepository.getInstance();
    }

    public List<Driver> findAll() {
        return repository.findAll();
    }

}
