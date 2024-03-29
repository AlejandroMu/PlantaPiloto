package icesi.plantapiloto.plcDriver;

import icesi.plantapiloto.driverAsset.DriverAssetImp;
import icesi.plantapiloto.driverAsset.concrete.DriverAssetConcrete;
import icesi.plantapiloto.plcDriver.plcReader.PLCReader;

public class PlcDriver {
    public static void main(String[] args) {
        System.out.println("PlcDriver running");
        String propName = "application.properties";
        Class<? extends DriverAssetConcrete> concrete = PLCReader.class;
        DriverAssetImp.initServices(concrete, propName);
    }
}
