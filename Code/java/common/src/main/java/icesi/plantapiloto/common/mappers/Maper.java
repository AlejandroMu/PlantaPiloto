package icesi.plantapiloto.common.mappers;

import java.util.ArrayList;
import java.util.List;

public interface Maper<T, D> {

    public D asEntityDTO(T entity);

    public default List<D> asEntityDTO(List<T> entity) {
        List<D> list = new ArrayList<>();
        for (int i = 0; i < entity.size(); i++) {
            list.add(asEntityDTO(entity.get(i)));
        }
        return list;
    }

}
