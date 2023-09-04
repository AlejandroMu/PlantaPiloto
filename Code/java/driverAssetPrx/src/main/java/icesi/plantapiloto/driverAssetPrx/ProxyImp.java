package icesi.plantapiloto.driverAssetPrx;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import com.zeroc.Ice.Current;
import com.zeroc.Ice.Endpoint;
import com.zeroc.Ice.ObjectPrx;
import com.zeroc.Ice.Util;

import icesi.plantapiloto.common.controllers.DriverAsset;
import icesi.plantapiloto.common.controllers.DriverAssetPrx;
import icesi.plantapiloto.common.controllers.MeasurementManagerControllerPrx;
import icesi.plantapiloto.common.dtos.output.AssetDTO;

public class ProxyImp implements DriverAsset {

    private DriverAssetPrx realObject;
    private Map<String, MeasurementManagerControllerPrx> measuresPrx;

    public ProxyImp() {
        measuresPrx = new HashMap<>();
    }

    /**
     * @param realObject the realObject to set
     */
    public void setRealObject(DriverAssetPrx realObject) {
        this.realObject = realObject;
    }

    @Override
    public void pauseReader(int arg0, Current arg1) {
        realObject.pauseReader(arg0);
    }

    @Override
    public void readAsset(AssetDTO arg0, MeasurementManagerControllerPrx arg1, int arg2, long arg3, boolean arg4,
            Current current) {
        System.out.println("readAsset Prx");
        String proxy = arg1.ice_getIdentity().name;
        Endpoint endpoints[] = arg1.ice_getEndpoints();
        for (Endpoint endpoint : endpoints) {
            proxy += ":" + endpoint._toString();
        }
        MeasurementManagerControllerPrx server = measuresPrx.get(proxy);

        if (server == null) {
            MeasureProxy measureProxy = new MeasureProxy(arg1);
            ObjectPrx prx = current.adapter.add(measureProxy, Util.stringToIdentity(UUID.randomUUID().toString()));
            server = MeasurementManagerControllerPrx.uncheckedCast(prx);
            measuresPrx.put(proxy, server);
        }

        realObject.readAsset(arg0, server, arg2, arg3, arg4);
    }

    @Override
    public void resumeReader(int arg0, Current arg1) {
        realObject.resumeReader(arg0);
    }

    @Override
    public int setPointAsset(AssetDTO arg0, double arg1, Current arg2) {
        return realObject.setPointAsset(arg0, arg1);
    }

    @Override
    public void stopRead(int arg0, Current arg1) {
        realObject.stopRead(arg0);
    }

}
