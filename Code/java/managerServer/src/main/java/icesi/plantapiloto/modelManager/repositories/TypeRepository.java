package icesi.plantapiloto.modelManager.repositories;

import icesi.plantapiloto.common.model.Type;

public class TypeRepository implements Repository<Type, Integer> {
    @Override
    public Class<Type> getType() {
        return Type.class;
    }
}
