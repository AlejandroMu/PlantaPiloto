package icesi.plantapiloto.driverAsset.RMSender;

import java.util.ArrayDeque;
import java.util.HashMap;

import com.zeroc.Ice.ObjectPrx;

import icesi.plantapiloto.common.controllers.AssetManagerCallbackPrx;
import icesi.plantapiloto.common.dtos.MeasurementDTO;
import icesi.plantapiloto.driverAsset.DriverAssetImp;

public class PublisherManager extends Thread {
    private static HashMap<String, PublisherManager> instances = new HashMap<>();

    public static PublisherManager getInstance(String proxy) {
        PublisherManager ret = instances.get(proxy);
        if (ret == null) {
            ObjectPrx objectPrx = DriverAssetImp.communicator.stringToProxy(proxy);
            AssetManagerCallbackPrx assetPrx = AssetManagerCallbackPrx.checkedCast(objectPrx);
            ret = new PublisherManager(assetPrx);
            ret.start();
            instances.put(proxy, ret);
        }

        return ret;
    }

    public static void addInstance(AssetManagerCallbackPrx proxy, String name) {
        if (instances.containsKey(name)) {
            return;
        }
        PublisherManager ret = new PublisherManager(proxy);
        ret.start();
        instances.put(name, ret);
    }

    private AssetManagerCallbackPrx publisherI;
    private ArrayDeque<MeasurementDTO[]> messages;
    private boolean stop;

    /**
     * @param publisherI
     */
    private PublisherManager(AssetManagerCallbackPrx publisherI) {
        this.publisherI = publisherI;

        messages = new ArrayDeque<>();
    }

    public void addMessage(MeasurementDTO[] message) {
        synchronized (messages) {
            messages.add(message);
        }
    }

    public void stopTask(boolean c) {
        stop = c;
    }

    public void run() {
        while (!stop) {
            MeasurementDTO[] mesg = null;
            try {
                while (!messages.isEmpty()) {
                    mesg = messages.peek();
                    publisherI.saveAssetValue(mesg);
                    messages.poll();
                }
                Thread.yield();

            } catch (Exception e) {
                System.out.println("FAIL SEND MESSAGE");
            }

        }
    }

}
