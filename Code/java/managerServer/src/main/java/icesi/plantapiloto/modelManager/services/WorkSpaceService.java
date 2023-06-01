package icesi.plantapiloto.modelManager.services;

import java.util.List;

import javax.persistence.EntityManager;

import icesi.plantapiloto.common.model.WorkSpace;
import icesi.plantapiloto.modelManager.repositories.WorkSpaceRepository;

public class WorkSpaceService {

    private WorkSpaceRepository workSpaceRepository;

    public WorkSpaceService() {
        workSpaceRepository = WorkSpaceRepository.getInstance();
    }

    public int saveWorkSpace(String name, String desc, int depart, EntityManager manager) {
        WorkSpace workSpace = new WorkSpace();
        workSpace.setDescription(desc);
        workSpace.setName(name);
        if (depart != -1) {
            WorkSpace dep = workSpaceRepository.findById(depart, manager).get();
            workSpace.setWorkSpace(dep);
        }
        workSpaceRepository.save(workSpace, manager);
        return workSpace.getId();
    }

    public List<WorkSpace> getAll(EntityManager manager) {
        return workSpaceRepository.findAll(manager);
    }

    public List<WorkSpace> getByDepartment(int idDep, EntityManager manager) {
        return workSpaceRepository.findByDepartment(idDep, manager);
    }

    public WorkSpace getById(int workSpaceId, EntityManager manager) {
        return workSpaceRepository.findById(workSpaceId, manager).get();
    }
}
