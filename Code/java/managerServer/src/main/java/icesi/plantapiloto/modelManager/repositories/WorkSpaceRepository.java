package icesi.plantapiloto.modelManager.repositories;

import java.util.List;

import javax.persistence.EntityManager;

import icesi.plantapiloto.common.model.WorkSpace;

public class WorkSpaceRepository implements Repository<WorkSpace, Integer> {
    private static WorkSpaceRepository instance;

    private WorkSpaceRepository() {
    }

    public static WorkSpaceRepository getInstance() {
        if (instance == null) {
            instance = new WorkSpaceRepository();
        }
        return instance;
    }

    @Override
    public Class<WorkSpace> getType() {
        return WorkSpace.class;
    }

    public List<WorkSpace> findByDepartment(int idDep, EntityManager manager) {
        String query = "FROM WorkSpace w From w.workSpace.id = ?1";
        return executeQuery(manager, query, idDep);
    }
}
