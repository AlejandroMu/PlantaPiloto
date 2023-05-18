package icesi.plantapiloto.modelManager;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

import com.zeroc.Ice.Communicator;
import com.zeroc.Ice.ObjectAdapter;
import com.zeroc.Ice.ObjectPrx;
import com.zeroc.Ice.Util;

import icesi.plantapiloto.common.controllers.MeasurementManagerControllerPrx;
import icesi.plantapiloto.modelManager.controllers.ActionController;
import icesi.plantapiloto.modelManager.controllers.AssetController;
import icesi.plantapiloto.modelManager.controllers.InstructionController;
import icesi.plantapiloto.modelManager.controllers.MeasurementController;
import icesi.plantapiloto.modelManager.controllers.ProcessController;
import icesi.plantapiloto.modelManager.controllers.WorkSpaceController;
import icesi.plantapiloto.modelManager.services.ActionService;
import icesi.plantapiloto.modelManager.services.AssetService;
import icesi.plantapiloto.modelManager.services.DriverService;
import icesi.plantapiloto.modelManager.services.InstructionService;
import icesi.plantapiloto.modelManager.services.MeasurementService;
import icesi.plantapiloto.modelManager.services.ProcessService;
import icesi.plantapiloto.modelManager.services.TypeService;
import icesi.plantapiloto.modelManager.services.WorkSpaceService;

public class Main {

    public static Communicator communicator;

    public static void main(String[] args) throws Exception {
        System.err.println("run ManagerServer");
        String propName = "application.properties";
        for (int i = 0; i < args.length; i++) {
            if (args[i].equals("-props")) {
                propName = args[++i];
            }
        }
        InputStream stream = ClassLoader.getSystemClassLoader().getResourceAsStream(propName);
        if (stream == null) {
            stream = new FileInputStream(propName);
        }
        Properties p = new Properties(System.getProperties());
        p.load(stream);
        System.setProperties(p);

        communicator = Util.initialize();

        // Services

        ProcessService processService = new ProcessService();
        AssetService assetService = new AssetService();
        TypeService typeService = new TypeService();
        DriverService driverService = new DriverService();
        MeasurementService measureService = new MeasurementService();
        InstructionService instructionService = new InstructionService();
        ActionService actionService = new ActionService();
        WorkSpaceService workSpaceService = new WorkSpaceService();

        // Controllers
        AssetController asset = new AssetController();
        MeasurementController measure = new MeasurementController();
        ProcessController processController = new ProcessController();
        ActionController actionController = new ActionController();
        InstructionController instructionController = new InstructionController();
        WorkSpaceController workSpaceController = new WorkSpaceController();

        // dependencies
        asset.setService(assetService);

        measure.setService(measureService);
        measure.setPublisher(System.getProperty("mqtt.host"));

        instructionService.setActionService(actionService);

        processService.setInstructionService(instructionService);
        processService.setAssetService(assetService);
        processController.setService(processService);

        actionController.setService(actionService);

        instructionController.setService(instructionService);

        workSpaceController.setService(workSpaceService);
        workSpaceController.setTypeService(typeService);
        workSpaceController.setDriverService(driverService);

        driverService.setWorkSpaceService(workSpaceService);

        ObjectAdapter adapter = communicator.createObjectAdapterWithEndpoints("Model",
                System.getProperty("Model.Endpoints"));

        adapter.add(asset, Util.stringToIdentity("AssetManager"));
        adapter.add(processController, Util.stringToIdentity("ProcessManager"));
        adapter.add(actionController, Util.stringToIdentity("ActionManager"));
        adapter.add(instructionController, Util.stringToIdentity("InstructionManager"));
        adapter.add(workSpaceController, Util.stringToIdentity("WorkSpaceManager"));

        ObjectPrx measurePrx = adapter.add(measure, Util.stringToIdentity("MeasureManager"));

        processService.setCallback(MeasurementManagerControllerPrx.uncheckedCast(measurePrx));
        adapter.activate();
        communicator.waitForShutdown();
        communicator.close();

    }
}
