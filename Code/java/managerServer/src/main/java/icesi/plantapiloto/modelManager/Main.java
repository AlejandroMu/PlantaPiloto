package icesi.plantapiloto.modelManager;

import com.zeroc.Ice.Communicator;
import com.zeroc.Ice.ObjectAdapter;
import com.zeroc.Ice.Util;

import icesi.plantapiloto.common.controllers.AssetManagerController;
import icesi.plantapiloto.modelManager.assetsManager.AssetController;

public class Main {

    public static Communicator communicator;

    public static void main(String[] args) {
        System.err.println("run ManagerServer");

        AssetManagerController asset = new AssetController();
        communicator = Util.initialize(args, "model.config");
        ObjectAdapter adapter = communicator.createObjectAdapter("Model");
        adapter.add(asset, Util.stringToIdentity("AssetManager"));
        adapter.activate();
        communicator.waitForShutdown();
        communicator.close();

    }
}
