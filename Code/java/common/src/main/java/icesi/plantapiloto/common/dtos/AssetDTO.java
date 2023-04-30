package icesi.plantapiloto.common.dtos;

import java.io.Serializable;

public class AssetDTO implements Serializable {
    public int assetId;
    public String name;
    public String typeName;
    public String state;
    public AssetDTO[] childrens;

    /**
     * @param assetId
     * @param name
     * @param typeName
     * @param state
     * @param childrens
     */
    public AssetDTO(int assetId, String name, String typeName, String state, AssetDTO[] childrens) {
        this.assetId = assetId;
        this.name = name;
        this.typeName = typeName;
        this.state = state;
        this.childrens = childrens;
    }

    public AssetDTO() {
    }

}
