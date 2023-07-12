package icesi.plantapiloto.driverAsset.RMSender;

import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.List;

import com.zeroc.Ice.ObjectPrx;

import icesi.plantapiloto.common.controllers.MeasurementManagerControllerPrx;
import icesi.plantapiloto.common.dtos.MeasurementDTO;
import icesi.plantapiloto.driverAsset.DriverAssetImp;

public class PublisherManager extends Thread {
    private static HashMap<String, PublisherManager> instances = new HashMap<>();

    public static PublisherManager getInstance(String proxy) {
        PublisherManager ret = instances.get(proxy);
        if (ret == null) {
            ObjectPrx objectPrx = DriverAssetImp.communicator.stringToProxy(proxy);
            MeasurementManagerControllerPrx assetPrx = MeasurementManagerControllerPrx.checkedCast(objectPrx);
            ret = new PublisherManager(assetPrx);
            ret.start();
            instances.put(proxy, ret);
        }

        return ret;
    }

    private MeasurementManagerControllerPrx publisherI;
    private ArrayDeque<List<MeasurementDTO>> messages;
    private boolean stop;

    /**
     * @param publisherI
     */
    private PublisherManager(MeasurementManagerControllerPrx publisherI) {
        this.publisherI = publisherI;

        messages = new ArrayDeque<>();
    }

    public void addMessage(List<MeasurementDTO> message) {
        synchronized (messages) {
            messages.add(message);
        }
    }

    public void stopTask(boolean c) {
        stop = c;
    }

    public void run() {
        while (!stop) {
            List<MeasurementDTO> mesg = null;
            try {
                while (!messages.isEmpty()) {
                    mesg = messages.peek();

                    publisherI.saveAssetValue(mesg.toArray(new MeasurementDTO[mesg.size()]));
                    messages.poll();
                }
                Thread.yield();

            } catch (Exception e) {
                System.out.println("FAIL SEND MESSAGE");
            }

        }
    }

}
