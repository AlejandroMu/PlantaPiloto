package icesi.plantapiloto.plcDriver;

import icesi.plantapiloto.driverAsset.DriverAssetImp;
import icesi.plantapiloto.plcDriver.plcReader.PLCReaderDummy;

public class PlcDriver {
    public static void main(String[] args) {
        System.out.println("PlcDriver running");
        DriverAssetImp.initServices(PLCReaderDummy.class);
    }
}
