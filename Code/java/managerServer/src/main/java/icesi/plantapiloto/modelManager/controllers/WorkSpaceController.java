package icesi.plantapiloto.modelManager.controllers;

import java.util.List;

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
        return service.saveWorkSpace(name, desc, depart);
    }

    @Override
    public WorkSpaceDTO[] findWorkSpaces(Current current) {
        List<WorkSpace> workSpaces = service.getAll();
        return WorkSpaceMapper.getInstance().asEntityDTO(workSpaces).toArray(WorkSpaceDTO[]::new);
    }

    @Override
    public WorkSpaceDTO[] findWorkSpacesByDepartment(int idDep, Current current) {
        List<WorkSpace> workSpaces = service.getByDepartment(idDep);
        return WorkSpaceMapper.getInstance().asEntityDTO(workSpaces).toArray(WorkSpaceDTO[]::new);
    }

    @Override
    public int saveDriver(String name, String serviceProxy, int workSpaceId, Current current) {
        return driverService.save(name, serviceProxy, workSpaceId);
    }

    @Override
    public DriverDTO[] findAllDrivers(Current current) {

        List<Driver> drivers = driverService.findAll();
        return DriverMapper.getInstance().asEntityDTO(drivers).toArray(DriverDTO[]::new);
    }

    @Override
    public DriverDTO[] findDriversByWorkSpace(int workSpaceId, Current current) {

        List<Driver> drivers = driverService.findByWorkSpace(workSpaceId);
        return DriverMapper.getInstance().asEntityDTO(drivers).toArray(DriverDTO[]::new);
    }

    @Override
    public TypeDTO[] findTypesByDriver(int driverId, Current current) {
        List<Type> types = typeService.findByDriver(driverId);
        return TypeMapper.getInstance().asEntityDTO(types).toArray(TypeDTO[]::new);
    }

    @Override
    public TypeDTO[] findAllTypes(Current current) {

        List<Type> types = typeService.findAll();
        return TypeMapper.getInstance().asEntityDTO(types).toArray(TypeDTO[]::new);
    }

    @Override
    public int saveAssetType(String name, String desc, int driver, Current current) {
        return typeService.saveType(name, desc, driverService.findById(driver));
    }
}
