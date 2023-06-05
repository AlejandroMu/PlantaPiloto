package icesi.plantapiloto.common.consts;

public enum AssetState {
    ACTIVE("A"),
    INACTIVE("I"),
    BUSY("B");

    private final String value;

    private AssetState(String v) {
        this.value = v;
    }

    public String getValue() {
        return value;
    }
}
