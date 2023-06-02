package icesi.plantapiloto.modelManager.controllers;

import java.util.List;

import javax.persistence.EntityManager;

import com.zeroc.Ice.Current;

import icesi.plantapiloto.common.controllers.ProcessManagerController;
import icesi.plantapiloto.common.dtos.ExecutionDTO;
import icesi.plantapiloto.common.dtos.ProcessAssetDTO;
import icesi.plantapiloto.common.dtos.ProcessDTO;
import icesi.plantapiloto.common.mappers.AssetMapper;
import icesi.plantapiloto.common.mappers.ExecutionMapper;
import icesi.plantapiloto.common.mappers.ProcessMaper;
import icesi.plantapiloto.common.model.Execution;
import icesi.plantapiloto.common.model.Process;
import icesi.plantapiloto.common.model.ProcessAsset;
import icesi.plantapiloto.modelManager.entityManager.ManagerPool;
import icesi.plantapiloto.modelManager.services.ProcessService;

public class ProcessController implements ProcessManagerController {

    private ProcessService service;

    /**
     * @param service the service to set
     */
    public void setService(ProcessService service) {
        this.service = service;
    }

    @Override
    public int startProcess(int processId, String userName, Current current) {
        EntityManager manager = ManagerPool.getManager();
        manager.getTransaction().begin();
        int ret = service.startProcess(processId, userName, manager);
        manager.getTransaction().commit();
        ManagerPool.close(manager);
        return ret;
    }

    @Override
    public void stopExecutionProcess(int execId, Current current) {
        EntityManager manager = ManagerPool.getManager();
        manager.getTransaction().begin();
        service.stopExecutionProcess(execId, manager);
        manager.getTransaction().commit();
        ManagerPool.close(manager);
    }

    @Override
    public void pauseExecutionProcess(int execId, Current current) {
        EntityManager manager = ManagerPool.getManager();
        manager.getTransaction().begin();
        service.pauseExecutionProcess(execId, manager);
        manager.getTransaction().commit();
        ManagerPool.close(manager);
    }

    @Override
    public void continueExecutionProcess(int execId, Current current) {
        EntityManager manager = ManagerPool.getManager();
        manager.getTransaction().begin();
        service.continueExecutionProcess(execId, manager);
        manager.getTransaction().commit();
        ManagerPool.close(manager);

    }

    @Override
    public int saveProcess(String name, String desc, int workSpaceId, Current current) {
        EntityManager manager = ManagerPool.getManager();
        manager.getTransaction().begin();
        int ret = service.saveProcess(name, desc, workSpaceId, manager);
        manager.getTransaction().commit();
        ManagerPool.close(manager);
        return ret;
    }

    @Override
    public void addInstructionToProcess(int instId, int processId, Current current) {
        EntityManager manager = ManagerPool.getManager();
        manager.getTransaction().begin();
        service.addInstructionToProcess(instId, processId, manager);
        manager.getTransaction().commit();
        ManagerPool.close(manager);
    }

    @Override
    public void applyIntructionToExecution(int instId, int execId, Current current) {
        EntityManager manager = ManagerPool.getManager();
        manager.getTransaction().begin();
        service.applyIntructionToExecution(instId, execId, manager);
        manager.getTransaction().commit();
        ManagerPool.close(manager);
    }

    @Override
    public ProcessDTO[] findProcessByWorkSpace(int workSpaceId, Current current) {
        EntityManager manager = ManagerPool.getManager();
        manager.getTransaction().begin();
        List<Process> processes = service.getProcessesByWorkSpace(workSpaceId, manager);
        ProcessDTO[] ret = ProcessMaper.getInstance().asEntityDTO(processes).toArray(new ProcessDTO[processes.size()]);
        manager.getTransaction().commit();
        ManagerPool.close(manager);
        return ret;
    }

    @Override
    public ExecutionDTO[] findExecutions(int processId, long startDate, long endDate, String running,
            Current current) {
        EntityManager manager = ManagerPool.getManager();
        manager.getTransaction().begin();
        List<Execution> executions = service.getExecutionByProcessIdAndDateStartBetween(processId, startDate, endDate,
                running, manager);
        ExecutionDTO[] ret = ExecutionMapper.getInstance().asEntityDTO(executions)
                .toArray(new ExecutionDTO[executions.size()]);
        manager.getTransaction().commit();
        ManagerPool.close(manager);
        return ret;
    }

    @Override
    public void addAssetToProcess(int asset, int processId, long period, Current current) {
        EntityManager manager = ManagerPool.getManager();
        manager.getTransaction().begin();
        service.addAssetToProcess(asset, processId, period, manager);
        manager.getTransaction().commit();
        ManagerPool.close(manager);
    }

    @Override
    public void updateAssetToProcess(int asset, int processId, long period, Current current) {
        EntityManager manager = ManagerPool.getManager();
        manager.getTransaction().begin();
        service.updateAssetToProcess(asset, processId, period, manager);
        manager.getTransaction().commit();
        ManagerPool.close(manager);

    }

    @Override
    public void removeAssetToProcess(int asset, int processId, Current current) {
        EntityManager manager = ManagerPool.getManager();
        manager.getTransaction().begin();
        service.removeAssetToProcess(asset, processId, manager);
        manager.getTransaction().commit();
        ManagerPool.close(manager);
    }

    @Override
    public ProcessAssetDTO[] getAssetOfProcess(int processId, Current current) {
        EntityManager manager = ManagerPool.getManager();
        manager.getTransaction().begin();
        List<ProcessAsset> assets = service.getAssetsOfProcess(processId, manager);
        AssetMapper mapper = AssetMapper.getInstance();
        ProcessAssetDTO[] ret = assets.stream()
                .map(PA -> new ProcessAssetDTO(processId, mapper.asEntityDTO(PA.getAsset()), PA.getDelayRead()))
                .toArray(ProcessAssetDTO[]::new);
        manager.getTransaction().commit();
        ManagerPool.close(manager);
        return ret;
    }

}
