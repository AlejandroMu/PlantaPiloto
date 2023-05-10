package icesi.plantapiloto.plcDriver;

import icesi.plantapiloto.driverAsset.DriverAssetImp;
import icesi.plantapiloto.driverAsset.concrete.DriverAssetConcrete;
import icesi.plantapiloto.plcDriver.plcReader.PLCReader;
import icesi.plantapiloto.plcDriver.plcReader.PLCReaderDummy;

public class PlcDriver {
    public static void main(String[] args) {
        System.out.println("PlcDriver running");
        boolean prod = false;
        String propName = "application.properties";
        for (int i = 0; i < args.length; i++) {
            if (args[i].equals("-props")) {

                prod = args[++i].contains("prod");
            }
        }
        Class<? extends DriverAssetConcrete> concrete = !prod ? PLCReaderDummy.class : PLCReader.class;
        DriverAssetImp.initServices(concrete, propName);
    }
}
