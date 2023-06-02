package icesi.plantapiloto.modelManager.controllers;

import java.util.List;

import javax.persistence.EntityManager;

import com.zeroc.Ice.Current;

import icesi.plantapiloto.common.controllers.WorkSpaceManagerController;
import icesi.plantapiloto.common.dtos.DriverDTO;
import icesi.plantapiloto.common.dtos.TypeDTO;
import icesi.plantapiloto.common.dtos.WorkSpaceDTO;
import icesi.plantapiloto.common.mappers.DriverMapper;
import icesi.plantapiloto.common.mappers.TypeMapper;
import icesi.plantapiloto.common.mappers.WorkSpaceMapper;
import icesi.plantapiloto.common.model.Driver;
import icesi.plantapiloto.common.model.Type;
import icesi.plantapiloto.common.model.WorkSpace;
import icesi.plantapiloto.modelManager.entityManager.ManagerPool;
import icesi.plantapiloto.modelManager.services.DriverService;
import icesi.plantapiloto.modelManager.services.TypeService;
import icesi.plantapiloto.modelManager.services.WorkSpaceService;

public class WorkSpaceController implements WorkSpaceManagerController {

    private WorkSpaceService service;
    private DriverService driverService;
    private TypeService typeService;

    /**
     * @param typeService the typeService to set
     */
    public void setTypeService(TypeService typeService) {
        this.typeService = typeService;
    }

    /**
     * @param service the service to set
     */
    public void setService(WorkSpaceService service) {
        this.service = service;
    }

    /**
     * @param driverService the driverService to set
     */
    public void setDriverService(DriverService driverService) {
        this.driverService = driverService;
    }

    @Override
    public int saveWorkSpace(String name, String desc, int depart, Current current) {
        EntityManager manager = ManagerPool.getManager();
        manager.getTransaction().begin();

        int ret = service.saveWorkSpace(name, desc, depart, manager);

        manager.getTransaction().commit();
        ManagerPool.close(manager);
        return ret;
    }

    @Override
    public WorkSpaceDTO[] findWorkSpaces(Current current) {
        EntityManager manager = ManagerPool.getManager();
        manager.getTransaction().begin();
        List<WorkSpace> workSpaces = service.getAll(manager);
        WorkSpaceDTO[] ret = WorkSpaceMapper.getInstance().asEntityDTO(workSpaces)
                .toArray(new WorkSpaceDTO[workSpaces.size()]);

        manager.getTransaction().commit();
        ManagerPool.close(manager);
        return ret;
    }

    @Override
    public WorkSpaceDTO[] findWorkSpacesByDepartment(int idDep, Current current) {
        EntityManager manager = ManagerPool.getManager();
        manager.getTransaction().begin();
        List<WorkSpace> workSpaces = service.getByDepartment(idDep, manager);
        WorkSpaceDTO[] ret = WorkSpaceMapper.getInstance().asEntityDTO(workSpaces)
                .toArray(new WorkSpaceDTO[workSpaces.size()]);
        manager.getTransaction().commit();
        ManagerPool.close(manager);
        return ret;
    }

    @Override
    public int saveDriver(String name, String serviceProxy, int workSpaceId, Current current) {
        EntityManager manager = ManagerPool.getManager();
        manager.getTransaction().begin();
        int ret = driverService.save(name, serviceProxy, workSpaceId, manager);
        manager.getTransaction().commit();
        ManagerPool.close(manager);
        return ret;
    }

    @Override
    public DriverDTO[] findAllDrivers(Current current) {
        EntityManager manager = ManagerPool.getManager();
        manager.getTransaction().begin();
        List<Driver> drivers = driverService.findAll(manager);
        DriverDTO[] ret = DriverMapper.getInstance().asEntityDTO(drivers).toArray(new DriverDTO[drivers.size()]);
        manager.getTransaction().commit();
        ManagerPool.close(manager);
        return ret;
    }

    @Override
    public DriverDTO[] findDriversByWorkSpace(int workSpaceId, Current current) {
        EntityManager manager = ManagerPool.getManager();
        manager.getTransaction().begin();
        List<Driver> drivers = driverService.findByWorkSpace(workSpaceId, manager);
        DriverDTO[] ret = DriverMapper.getInstance().asEntityDTO(drivers).toArray(new DriverDTO[drivers.size()]);
        manager.getTransaction().commit();
        ManagerPool.close(manager);
        return ret;
    }

    @Override
    public TypeDTO[] findTypesByDriver(int driverId, Current current) {
        EntityManager manager = ManagerPool.getManager();
        manager.getTransaction().begin();
        List<Type> types = typeService.findByDriver(driverId, manager);
        TypeDTO[] ret = TypeMapper.getInstance().asEntityDTO(types).toArray(new TypeDTO[types.size()]);
        manager.getTransaction().commit();
        ManagerPool.close(manager);
        return ret;
    }

    @Override
    public TypeDTO[] findAllTypes(Current current) {
        EntityManager manager = ManagerPool.getManager();
        manager.getTransaction().begin();
        List<Type> types = typeService.findAll(manager);
        TypeDTO[] ret = TypeMapper.getInstance().asEntityDTO(types).toArray(new TypeDTO[types.size()]);
        manager.getTransaction().commit();
        ManagerPool.close(manager);
        return ret;
    }

    @Override
    public int saveAssetType(String name, String desc, int driver, Current current) {
        EntityManager manager = ManagerPool.getManager();
        manager.getTransaction().begin();
        int ret = typeService.saveType(name, desc, driverService.findById(driver, manager), manager);
        manager.getTransaction().commit();
        ManagerPool.close(manager);
        return ret;
    }
}
