package icesi.plantapiloto.common.dtos.output;

import java.io.Serializable;
import java.util.Map;

public class AssetDTO implements Serializable {
    public int assetId;
    public String name;
    public String typeName;
    public String state;
    public String description;
    public int workId;
    public AssetDTO[] childrens;
    public Map<String, String> props;
    public AssetDTO parent;

    /**
     * @param assetId
     * @param name
     * @param typeName
     * @param state
     * @param childrens
     */
    public AssetDTO(int assetId, String name, String typeName, String state, String desc, AssetDTO[] childrens,
            AssetDTO par, int workId, Map<String, String> props) {
        this.assetId = assetId;
        this.name = name;
        this.typeName = typeName;
        this.state = state;
        this.childrens = childrens;
        this.props = props;
        this.parent = par;
        this.workId = workId;
        this.description = desc;
    }

    public AssetDTO() {
    }

    public boolean contains(AssetDTO dto) {
        if (assetId == dto.assetId) {
            return true;
        } else {
            if (childrens != null && childrens.length > 0) {
                boolean con = false;
                for (AssetDTO assetDTO : childrens) {
                    con |= assetDTO.contains(dto);
                    if (con) {
                        return true;
                    }
                }
                return con;
            } else {
                return false;
            }
        }
    }

}
