package icesi.plantapiloto.modelManager.services;

import java.util.List;

import javax.persistence.EntityManager;

import icesi.plantapiloto.common.model.Driver;
import icesi.plantapiloto.common.model.Type;
import icesi.plantapiloto.modelManager.repositories.TypeRepository;

public class TypeService {

    private TypeRepository repository;

    public TypeService() {
        repository = TypeRepository.getInstance();
    }

    public List<Type> findAll(EntityManager manager) {
        return repository.findAll(manager);
    }

    public List<Type> findByDriver(int driverId, EntityManager manager) {
        return repository.findByDriver(driverId, manager);
    }

    public int saveType(String name, String desc, Driver driver, EntityManager manager) {
        Type type = new Type();
        type.setDescription(desc);
        type.setDriver(driver);
        type.setName(name);
        repository.save(type, manager);
        return type.getId();
    }

}
