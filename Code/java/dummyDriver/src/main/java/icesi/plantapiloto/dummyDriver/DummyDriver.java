package icesi.plantapiloto.dummyDriver;

import icesi.plantapiloto.driverAsset.DriverAssetImp;
import icesi.plantapiloto.driverAsset.concrete.DriverAssetConcrete;
import icesi.plantapiloto.dummyDriver.dummyReader.PLCReaderDummy;

public class DummyDriver {
    public static void main(String[] args) {
        System.out.println("DummyDriver running");
        String propName = "application.properties";
        Class<? extends DriverAssetConcrete> concrete = PLCReaderDummy.class;
        DriverAssetImp.initServices(concrete, propName);
    }
}
