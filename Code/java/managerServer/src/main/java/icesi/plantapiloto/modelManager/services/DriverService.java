package icesi.plantapiloto.modelManager.services;

import java.util.List;

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

    public List<Driver> findAll() {
        return repository.findAll();
    }

    public List<Driver> findByWorkSpace(int workSpaceId) {
        return repository.findByWorkSpace(workSpaceId);
    }

    public int save(String name, String serviceProxy, int workSpaceId) {
        Driver driver = new Driver();
        driver.setName(name);
        driver.setServiceProxy(serviceProxy);
        WorkSpace workSpace = workSpaceService.getById(workSpaceId);
        driver.setWorkSpaceBean(workSpace);
        repository.save(driver);
        return driver.getId();
    }

    public Driver findById(int driver) {
        return repository.findById(driver).get();
    }

}
