package icesi.plantapiloto.modelManager.services;

import java.util.List;

import icesi.plantapiloto.common.model.WorkSpace;
import icesi.plantapiloto.modelManager.repositories.WorkSpaceRepository;

public class WorkSpaceService {

    private WorkSpaceRepository workSpaceRepository;

    public WorkSpaceService() {
        workSpaceRepository = WorkSpaceRepository.getInstance();
    }

    public int saveWorkSpace(String name, String desc, int depart) {
        WorkSpace workSpace = new WorkSpace();
        workSpace.setDescription(desc);
        workSpace.setName(name);
        if (depart != -1) {
            WorkSpace dep = workSpaceRepository.findById(depart).get();
            workSpace.setWorkSpace(dep);
        }
        workSpaceRepository.save(workSpace);
        return workSpace.getId();
    }

    public List<WorkSpace> getAll() {
        return workSpaceRepository.findAll();
    }

    public List<WorkSpace> getByDepartment(int idDep) {
        return workSpaceRepository.findByDepartment(idDep);
    }

    public WorkSpace getById(int workSpaceId) {
        return workSpaceRepository.findById(workSpaceId).get();
    }
}
