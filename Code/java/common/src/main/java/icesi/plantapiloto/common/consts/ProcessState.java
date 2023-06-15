package icesi.plantapiloto.common.consts;

public enum ProcessState {
    ACTIVE("A"),
    REMOVED("R");

    private final String value;

    private ProcessState(String val) {
        this.value = val;
    }

    /**
     * @return the value
     */
    public String getValue() {
        return value;
    }
}
