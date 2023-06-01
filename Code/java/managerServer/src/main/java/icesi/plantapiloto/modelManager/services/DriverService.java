package icesi.plantapiloto.modelManager.services;

import java.util.List;

import javax.persistence.EntityManager;

import icesi.plantapiloto.common.model.Driver;
import icesi.plantapiloto.common.model.WorkSpace;
import icesi.plantapiloto.modelManager.repositories.DriverRepository;

public class DriverService {

    private DriverRepository repository;

    private WorkSpaceService workSpaceService;

    /**
     * @param workSpaceService the workSpaceService to set
     */
    public void setWorkSpaceService(WorkSpaceService workSpaceService) {
        this.workSpaceService = workSpaceService;
    }

    public DriverService() {
        repository = DriverRepository.getInstance();
    }

    public List<Driver> findAll(EntityManager manager) {
        return repository.findAll(manager);
    }

    public List<Driver> findByWorkSpace(int workSpaceId, EntityManager manager) {
        return repository.findByWorkSpace(workSpaceId, manager);
    }

    public int save(String name, String serviceProxy, int workSpaceId, EntityManager manager) {
        String[] split = serviceProxy.split(" ");
        String host = null;
        String port = "1804";
        for (int i = 0; i < split.length; i++) {
            if (split[i].equals("-h")) {
                host = split[++i];
            } else if (split[i].equals("-p")) {
                port = split[++i];
            }
        }
        if (host != null) {
            Driver driver = new Driver();
            driver.setName(name);
            driver.setServiceProxy("DriverAsset:tcp -h " + host + " -p " + port);
            WorkSpace workSpace = workSpaceService.getById(workSpaceId, manager);
            driver.setWorkSpaceBean(workSpace);
            repository.save(driver, manager);
            return driver.getId();

        } else {
            return -1;
        }
    }

    public Driver findById(int driver, EntityManager manager) {
        return repository.findById(driver, manager).get();
    }

}
