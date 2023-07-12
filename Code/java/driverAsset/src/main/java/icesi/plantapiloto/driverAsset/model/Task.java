package icesi.plantapiloto.driverAsset.model;

public class Task {

    private int id;
    private long period;
    private int execId;
    private String assets;
    private String server;
    private int isShare;

    private String state;

    /**
     * @param period
     * @param execId
     * @param assets
     * @param server
     * @param isShare
     */
    public Task(int id, long period, int execId, String assets, String server, int isShare, String state) {
        this.id = id;
        this.period = period;
        this.execId = execId;
        this.assets = assets;
        this.server = server;
        this.isShare = isShare;
        this.state = state;
    }

    /**
     * @return the state
     */
    public String getState() {
        return state;
    }

    /**
     * @param state the state to set
     */
    public void setState(String state) {
        this.state = state;
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
     * @return the period
     */
    public long getPeriod() {
        return period;
    }

    /**
     * @param period the period to set
     */
    public void setPeriod(long period) {
        this.period = period;
    }

    /**
     * @return the execId
     */
    public int getExecId() {
        return execId;
    }

    /**
     * @param execId the execId to set
     */
    public void setExecId(int execId) {
        this.execId = execId;
    }

    /**
     * @return the assets
     */
    public String getAssets() {
        return assets;
    }

    /**
     * @param assets the assets to set
     */
    public void setAssets(String assets) {
        this.assets = assets;
    }

    /**
     * @return the server
     */
    public String getServer() {
        return server;
    }

    /**
     * @param server the server to set
     */
    public void setServer(String server) {
        this.server = server;
    }

    /**
     * @return the isShare
     */
    public int getIsShare() {
        return isShare;
    }

    /**
     * @param isShare the isShare to set
     */
    public void setIsShare(int isShare) {
        this.isShare = isShare;
    }

}
