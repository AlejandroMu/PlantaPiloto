package icesi.plantapiloto.common.mappers;

import icesi.plantapiloto.common.dtos.TypeDTO;
import icesi.plantapiloto.common.model.Type;

public class TypeMapper implements Maper<Type, TypeDTO> {
    private static TypeMapper instance;

    public static TypeMapper getInstance() {
        if (instance == null) {
            instance = new TypeMapper();
        }
        return instance;
    }

    private TypeMapper() {

    }

    @Override
    public TypeDTO asEntityDTO(Type entity) {
        TypeDTO dto = new TypeDTO(entity.getId(), entity.getName(), entity.getDescription(), entity.getTableName());
        return dto;
    }

}
