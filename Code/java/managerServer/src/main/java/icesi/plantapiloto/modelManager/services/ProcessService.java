package icesi.plantapiloto.modelManager.services;

import java.sql.Timestamp;
import java.util.HashSet;
import java.util.List;

import icesi.plantapiloto.common.controllers.DriverAssetPrx;
import icesi.plantapiloto.common.controllers.MeasurementManagerControllerPrx;
import icesi.plantapiloto.common.mappers.AssetMapper;
import icesi.plantapiloto.common.model.Asset;
import icesi.plantapiloto.common.model.Driver;
import icesi.plantapiloto.common.model.Execution;
import icesi.plantapiloto.common.model.ExecutionInstruction;
import icesi.plantapiloto.common.model.Instruction;
import icesi.plantapiloto.common.model.Process;
import icesi.plantapiloto.common.model.ProcessAsset;
import icesi.plantapiloto.common.model.ProcessAssetPK;
import icesi.plantapiloto.modelManager.Main;
import icesi.plantapiloto.modelManager.repositories.ExecutionInstructionRepository;
import icesi.plantapiloto.modelManager.repositories.ExecutionRepository;
import icesi.plantapiloto.modelManager.repositories.ProcessAssetRepository;
import icesi.plantapiloto.modelManager.repositories.ProcessRepository;
import icesi.plantapiloto.modelManager.repositories.WorkSpaceRepository;

public class ProcessService {

    private MeasurementManagerControllerPrx callback;

    private ExecutionRepository executionRepository;
    private ProcessRepository processRepository;
    private WorkSpaceRepository workSpaceRepository;
    private ExecutionInstructionRepository executionInstructionRepository;
    private ProcessAssetRepository processAssetRepository;

    private InstructionService instructionService;
    private AssetService assetService;

    public ProcessService() {
        executionRepository = ExecutionRepository.getInstance();
        processRepository = ProcessRepository.getInstance();
        workSpaceRepository = WorkSpaceRepository.getInstance();
        executionInstructionRepository = ExecutionInstructionRepository.getInstance();
        processAssetRepository = ProcessAssetRepository.getInstance();
    }

    /**
     * @param instructionService the instructionService to set
     */
    public void setInstructionService(InstructionService instructionService) {
        this.instructionService = instructionService;
    }

    /**
     * @param assetService the assetService to set
     */
    public void setAssetService(AssetService assetService) {
        this.assetService = assetService;
    }

    /**
     * @param callback the callback to set
     */
    public void setCallback(MeasurementManagerControllerPrx callback) {
        this.callback = callback;
    }

    public Process getProcessOfExeId(int execId) {
        Execution execution = executionRepository.findById(execId).get();

        return execution.getProcessBean();
    }

    public int startProcess(int processId, String name) {
        Process process = processRepository.findById(processId).get();
        Execution newEx = new Execution();
        newEx.setProcessBean(process);
        newEx.setStartDate(new Timestamp(System.currentTimeMillis()));
        newEx.setOperUsername(name);
        executionRepository.save(newEx);
        runExecution(newEx);
        return newEx.getId();
    }

    public void runExecution(Execution execution) {
        Process process = execution.getProcessBean();
        List<ProcessAsset> Procesassets = process.getProcessAssets();
        AssetMapper mapper = AssetMapper.getInstance();

        for (ProcessAsset processAsset : Procesassets) {
            Asset asset = processAsset.getAsset();
            // TODO: VERIFY ASSET STATE, BUSY?
            long period = processAsset.getDelayRead();
            Driver driver = asset.getTypeBean().getDriver();
            DriverAssetPrx prx = DriverAssetPrx
                    .checkedCast(Main.communicator.stringToProxy(driver.getServiceProxy()));
            prx.readAsset(mapper.asEntityDTO(asset), callback, execution.getId(), period, false);
        }

    }

    public void runExecution(int execution) {
        runExecution(executionRepository.findById(execution).get());
    }

    public void stopExecutionProcess(int execId) {
        stopCapure(executionRepository.findById(execId).get());
    }

    public void stopCapure(Execution execution) {

        Process process = execution.getProcessBean();
        Asset[] assets = process.getProcessAssets().stream().map(pa -> pa.getAsset()).toArray(Asset[]::new);
        HashSet<Integer> drivId = new HashSet<>();
        for (Asset asset : assets) {
            Driver driver = asset.getTypeBean().getDriver();
            if (!drivId.contains(driver.getId())) {
                DriverAssetPrx prx = DriverAssetPrx
                        .checkedCast(Main.communicator.stringToProxy(driver.getServiceProxy()));
                prx.stopRead(execution.getId());
                drivId.add(driver.getId());
            }

        }
        execution.setEndDate(new Timestamp(System.currentTimeMillis()));
        executionRepository.update(execution);
    }

    public void pauseExecutionProcess(int execId) {
        pauseExecutionProcess(executionRepository.findById(execId).get());
    }

    public void pauseExecutionProcess(Execution execution) {
        Process process = execution.getProcessBean();
        Asset[] assets = process.getProcessAssets().stream().map(pa -> pa.getAsset()).toArray(Asset[]::new);
        HashSet<Integer> drivId = new HashSet<>();
        for (Asset asset : assets) {
            Driver driver = asset.getTypeBean().getDriver();
            if (!drivId.contains(driver.getId())) {
                DriverAssetPrx prx = DriverAssetPrx
                        .checkedCast(Main.communicator.stringToProxy(driver.getServiceProxy()));
                prx.pauseReader(execution.getId());
                drivId.add(driver.getId());
            }

        }
    }

    public void continueExecutionProcess(int execId) {
        continueExecutionProcess(executionRepository.findById(execId).get());

    }

    public void continueExecutionProcess(Execution execution) {
        Process process = execution.getProcessBean();
        Asset[] assets = process.getProcessAssets().stream().map(pa -> pa.getAsset()).toArray(Asset[]::new);
        HashSet<Integer> drivId = new HashSet<>();
        for (Asset asset : assets) {
            Driver driver = asset.getTypeBean().getDriver();
            if (!drivId.contains(driver.getId())) {
                DriverAssetPrx prx = DriverAssetPrx
                        .checkedCast(Main.communicator.stringToProxy(driver.getServiceProxy()));
                prx.resumeReader(execution.getId());
                drivId.add(driver.getId());
            }

        }
    }

    public int saveProcess(String name, String desc, int workSpaceId) {
        Process process = new Process();
        process.setDescription(desc);
        process.setName(name);
        process.setWorkSpaceBean(workSpaceRepository.findById(workSpaceId).get());
        processRepository.save(process);
        return process.getId();
    }

    public void addInstructionToProcess(int instId, int processId) {
        Instruction instruction = instructionService.getInstruction(instId);
        Process process = processRepository.findById(processId).get();

        process.addInstruction(instruction);

        processRepository.update(process);

    }

    public void applyIntructionToExecution(int instId, int execId) {
        Instruction ins = instructionService.getInstruction(instId);
        Execution exc = executionRepository.findById(execId).get();
        applyIntructionToExecution(ins, exc);
    }

    public void applyIntructionToExecution(Instruction instId, Execution execId) {
        boolean contains = instId.getProcesses().stream().anyMatch(p -> p.getId() == execId.getProcessBean().getId());
        if (contains) {
            ExecutionInstruction exeIns = new ExecutionInstruction();
            exeIns.setExcTime(new Timestamp(System.currentTimeMillis()));
            exeIns.setExecutionBean(execId);
            exeIns.setInstructionBean(instId);
            executionInstructionRepository.save(exeIns);
        } else {
            System.out.println("La instrucción no es aplicable a la ejecución, no pertenecen al mismo proceso");
            // TODO: error report
        }
    }

    public List<Process> getProcessesByWorkSpace(int workSpaceId) {

        return processRepository.findByWorkSpace(workSpaceId);
    }

    public List<Execution> getExecutionByProcessIdAndDateStartBetween(int processId, long startDate, long endDate,
            boolean run) {
        return executionRepository.findByProcessAndStartDateBetween(processId, startDate, endDate, run);
    }

    public void addAssetToProcess(int asset, int processId, long period) {
        Asset asset2 = assetService.getAssetById(asset);
        Process process = processRepository.findById(processId).get();
        if (asset2.getWorkSpace().getId() == process.getWorkSpaceBean().getId()) {
            ProcessAsset v = new ProcessAsset();
            v.setAsset(asset2);
            v.setDelayRead(period);
            v.setProcess(process);
            processAssetRepository.save(v);
        } else {
            System.out.println("el asset no es añadible al proceso, no pertenecen al mismo workspace");
            // TODO: error report
        }
    }

    public List<ProcessAsset> getAssetsOfProcess(int processId) {
        Process process = processRepository.findById(processId).get();
        return process.getProcessAssets();
    }

    public List<Execution> findExecutionsRunning(int processId) {
        return executionRepository.findExecutionsRunning(processId);
    }

    public void updateAssetToProcess(int asset, int processId, long period) {
        ProcessAssetPK pk = new ProcessAssetPK();
        pk.setAssetId(asset);
        pk.setProcessId(processId);
        ProcessAsset pa = processAssetRepository.findById(pk).get();
        pa.setDelayRead(period);
        processAssetRepository.update(pa);

    }

}
