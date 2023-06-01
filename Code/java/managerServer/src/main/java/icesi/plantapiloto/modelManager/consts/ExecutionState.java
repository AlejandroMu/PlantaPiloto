package icesi.plantapiloto.modelManager.consts;

public enum ExecutionState {
    RUNNING("running"),
    PAUSED("paused"),
    STOPED("stoped");

    private final String value;

    private ExecutionState(String val) {
        this.value = val;
    }

    /**
     * @return the value
     */
    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return value;
    }
}
