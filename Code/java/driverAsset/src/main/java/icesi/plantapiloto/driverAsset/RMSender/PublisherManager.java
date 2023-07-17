package icesi.plantapiloto.driverAsset.RMSender;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import icesi.plantapiloto.common.controllers.MeasurementManagerControllerPrx;
import icesi.plantapiloto.common.dtos.MeasurementDTO;
import icesi.plantapiloto.driverAsset.DriverAssetImp;
import icesi.plantapiloto.driverAsset.model.MeasurementRepository;
import icesi.plantapiloto.driverAsset.model.Measurements;

public class PublisherManager extends Thread {

    private boolean stop;
    private MeasurementRepository repository;
    private static PublisherManager instance;

    public static PublisherManager getInstance() {
        if (instance == null) {
            instance = new PublisherManager();
            instance.start();
        }
        return instance;
    }

    /**
     * @param publisherI
     */
    private PublisherManager() {
        repository = MeasurementRepository.getInstance();
    }

    public void addMessage(List<MeasurementDTO> message, String server) {
        List<Measurements> m = message.stream()
                .map(d -> new Measurements(0, d.assetId, d.assetName, d.value, d.execId, d.timeStamp, server))
                .collect(Collectors.toList());
        for (Measurements measurements : m) {
            repository.insert(measurements);
        }
    }

    public void stopTask(boolean c) {
        stop = c;
    }

    public void run() {
        while (!stop) {
            Map<String, List<Measurements>> data = repository.getMeasurements();
            Iterator<String> keys = data.keySet().iterator();
            try {
                while (keys.hasNext()) {
                    String proxy = keys.next();
                    List<Measurements> mesg = data.get(proxy);

                    MeasurementManagerControllerPrx publisherI = MeasurementManagerControllerPrx
                            .checkedCast(DriverAssetImp.communicator.stringToProxy(proxy));
                    Measurements[] elements = mesg.toArray(new Measurements[mesg.size()]);
                    publisherI.saveAssetValue(elements);
                    repository.remove(elements);
                }
                Thread.yield();

            } catch (Exception e) {
                System.out.println("FAIL SEND MESSAGE");
            }

        }
    }

}
