package icesi.plantapiloto.modelManager;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

import com.zeroc.Ice.Communicator;
import com.zeroc.Ice.ObjectAdapter;
import com.zeroc.Ice.ObjectPrx;
import com.zeroc.Ice.Util;

import icesi.plantapiloto.common.controllers.AssetManagerCallbackPrx;
import icesi.plantapiloto.modelManager.assetsManager.AssetController;

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
        AssetController asset = new AssetController();
        asset.setPublisher(System.getProperty("mqtt.host"));

        ObjectAdapter adapter = communicator.createObjectAdapterWithEndpoints("Model",
                System.getProperty("Model.Endpoints"));
        ObjectPrx objectPrx = adapter.add(asset, Util.stringToIdentity("AssetManager"));
        asset.setCallback(AssetManagerCallbackPrx.checkedCast(objectPrx));
        adapter.activate();
        communicator.waitForShutdown();
        communicator.close();

    }
}
