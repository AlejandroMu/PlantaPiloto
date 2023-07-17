package icesi.plantapiloto.driverAsset.model;

import icesi.plantapiloto.common.dtos.MeasurementDTO;

public class Measurements extends MeasurementDTO {

    private String serverProxy;
    private int id;

    /**
     * @param assetId
     * @param assetName
     * @param value
     * @param execId
     * @param timeStamp
     * @param serverProxy
     */
    public Measurements(int id, int assetId, String assetName, double value, int execId, long timeStamp,
            String serverProxy) {
        super(assetId, assetName, value, execId, timeStamp);
        this.serverProxy = serverProxy;
        this.id = id;
    }

    /**
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * @return the serverProxy
     */
    public String getServerProxy() {
        return serverProxy;
    }

    /**
     * @param serverProxy the serverProxy to set
     */
    public void setServerProxy(String serverProxy) {
        this.serverProxy = serverProxy;
    }
}
