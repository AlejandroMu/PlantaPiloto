package icesi.plantapiloto.modelManager.repositories;

import java.util.List;

import javax.persistence.EntityManager;

import icesi.plantapiloto.common.consts.ProcessState;
import icesi.plantapiloto.common.model.Process;

public class ProcessRepository implements Repository<Process, Integer> {
    private static ProcessRepository instance;

    private ProcessRepository() {
    }

    public static ProcessRepository getInstance() {
        if (instance == null) {
            instance = new ProcessRepository();
        }
        return instance;
    }

    @Override
    public Class<Process> getType() {
        return Process.class;
    }

    public List<Process> findByWorkSpace(int workSpaceId, EntityManager manager) {
        String query = "From Process p Where p.workSpaceBean.id = ?1 AND p.state != ?2";
        return executeQuery(manager, query, workSpaceId, ProcessState.REMOVED.getValue());
    }

    @Override
    public void deleteById(Integer id, EntityManager manager) {
        // TODO Auto-generated method stub
        Repository.super.deleteById(id, manager);
    }

    @Override
    public List<Process> findAll(EntityManager manager) {
        String query = "From Process p Where p.state != ?2";
        return executeQuery(manager, query, ProcessState.REMOVED.getValue());
    }
}
