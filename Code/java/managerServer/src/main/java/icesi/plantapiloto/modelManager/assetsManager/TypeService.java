package icesi.plantapiloto.modelManager.assetsManager;

import java.util.List;

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

}
