package icesi.plantapiloto.driverAssetPrx;

import java.util.logging.Logger;

import com.zeroc.Ice.Communicator;
import com.zeroc.Ice.ObjectAdapter;
import com.zeroc.Ice.Util;

import icesi.plantapiloto.common.controllers.DriverAssetPrx;

public class Main {
    public static final String ENDPOINT_STRING = "driver.asset.port";

    private static final Logger logger = Logger.getLogger(Main.class.getName());

    public static void main(String[] args) {
        logger.info("Starting Proxy");
        String driver = null;
        String host = null;
        String portD = "1804";

        for (int i = 0; i < args.length; i++) {
            if (args[i].equals("-h")) {
                host = args[++i];
            } else if (args[i].equals("-p")) {
                portD = args[++i];
            }

        }
        if (host != null) {
            driver = "DriverAsset:tcp -h " + host + " -p " + portD;
        }
        Communicator communicator = Util.initialize(null, "proxy.cfg");
        String pr = communicator.getProperties().getProperty(ENDPOINT_STRING);
        String port = "1804";
        if (pr != null) {
            port = pr;
        }
        ProxyImp proxyImp = new ProxyImp();
        DriverAssetPrx real = null;
        if (driver != null) {
            real = DriverAssetPrx.checkedCast(communicator.stringToProxy(driver));
        } else {
            real = DriverAssetPrx.checkedCast(communicator.propertyToProxy("driver"));
        }
        proxyImp.setRealObject(real);
        ObjectAdapter adapter = communicator.createObjectAdapterWithEndpoints("DriverService",
                "tcp -h * -p " + port);
        adapter.add(proxyImp, Util.stringToIdentity("DriverAsset"));
        adapter.activate();

        communicator.waitForShutdown();

    }
}
