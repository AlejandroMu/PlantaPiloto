package icesi.plantapiloto.plcDriver;

import icesi.plantapiloto.driverAsset.DriverAssetImp;
import icesi.plantapiloto.plcDriver.plcReader.PLCReader;

public class PlcDriver {
    public static void main(String[] args) {
        DriverAssetImp.initServices(PLCReader.class);
    }
}
