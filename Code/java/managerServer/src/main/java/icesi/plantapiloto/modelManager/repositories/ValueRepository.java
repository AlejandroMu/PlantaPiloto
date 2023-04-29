package icesi.plantapiloto.modelManager.repositories;

import java.util.List;

import javax.persistence.EntityManager;

import icesi.plantapiloto.modelManager.dtos.ValueQuery;
import icesi.plantapiloto.modelManager.enityManager.Manager;
import icesi.plantapiloto.modelManager.model.Value;

public class ValueRepository implements Repository<Value, Long> {

    public List<Value> findValueByQuery(ValueQuery query) {
        EntityManager manager = Manager.managerFactory.createEntityManager();
        StringBuilder tags = new StringBuilder("m.name");
        int n = query.getTagsNames().size();
        for (int i = 0; i < n - 1; i++) {
            tags.append("m.name = '" + query.getTagsNames().get(i) + "' OR ");
        }
        tags.append("m.name = '" + query.getTagsNames().get(n - 1) + "' ");

        String queryStr = "Select v.* From Value v INNER JOIN " +
                "  (Select m.* From IOModule m INNER JOIN )";

        List<Value> result = manager.createQuery(queryStr, Value.class)
                .setParameter("plcName", query.getPlcName())
                .getResultList();
        return result;
    }

}
