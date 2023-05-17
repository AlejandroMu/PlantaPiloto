package icesi.plantapiloto.modelManager.services;

import java.util.List;

import icesi.plantapiloto.common.model.Driver;
import icesi.plantapiloto.common.model.Type;
import icesi.plantapiloto.modelManager.repositories.TypeRepository;

public class TypeService {

    private TypeRepository repository;

    public TypeService() {
        repository = TypeRepository.getInstance();
    }

    public List<Type> findAll() {
        return repository.findAll();
    }

    public List<Type> findByDriver(int driverId) {
        return repository.findByDriver(driverId);
    }

    public int saveType(String name, String desc, Driver driver) {
        Type type = new Type();
        type.setDescription(desc);
        type.setDriver(driver);
        type.setName(name);
        repository.save(type);
        return type.getId();
    }

}
