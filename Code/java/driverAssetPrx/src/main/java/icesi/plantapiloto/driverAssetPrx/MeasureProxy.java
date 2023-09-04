package icesi.plantapiloto.driverAssetPrx;

import java.util.logging.Logger;

import com.zeroc.Ice.Current;

import icesi.plantapiloto.common.controllers.MeasurementManagerController;
import icesi.plantapiloto.common.controllers.MeasurementManagerControllerPrx;
import icesi.plantapiloto.common.dtos.MeasurementDTO;

public class MeasureProxy implements MeasurementManagerController {

    private static final Logger logger = Logger.getLogger(MeasureProxy.class.getName());

    private MeasurementManagerControllerPrx realObject;

    public MeasureProxy(MeasurementManagerControllerPrx real) {
        this.realObject = real;
    }

    @Override
    public MeasurementDTO[] getMeasurments(int arg0, long arg1, long arg2, Current arg3) {
        logger.info("forwarding getMeasurements");
        return realObject.getMeasurments(arg0, arg1, arg2);
    }

    @Override
    public MeasurementDTO[] getMeasurmentsByExecution(int arg0, Current arg1) {
        logger.info("forwarding getMeasurmentsByExecution");

        return getMeasurmentsByExecution(arg0, arg1);
    }

    @Override
    public void saveAssetValue(MeasurementDTO[] arg0, Current arg1) {
        logger.info("forwarding saveAssetValue");
        realObject.saveAssetValue(arg0);
    }

}
