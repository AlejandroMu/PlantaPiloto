package icesi.plantapiloto.common.mappers;

import java.util.HashMap;
import java.util.List;

import icesi.plantapiloto.common.dtos.output.AssetDTO;
import icesi.plantapiloto.common.model.Asset;
import icesi.plantapiloto.common.model.MetaData;

public class AssetMapper {
    private static AssetMapper instance;

    public static AssetMapper getInstance() {
        if (instance == null) {

        }
        return instance;
    }

    public AssetDTO asAssetDto(Asset asset) {
        AssetDTO dto = new AssetDTO(asset.getId(), asset.getName(), asset.getTypeBean().getName(),
                asset.getAssetState(), null, null, null);
        List<MetaData> metaDatas = asset.getMetaData();
        HashMap<String, String> props = new HashMap<>();
        for (MetaData metaData : metaDatas) {
            props.put(metaData.getName(), metaData.getValue());
        }
        AssetDTO[] childs = new AssetDTO[asset.getAssets().size()];
        for (int i = 0; i < childs.length; i++) {
            childs[i] = asAssetDto(asset.getAssets().get(i));
        }
        dto.props = props;
        dto.childrens = childs;
        dto.parent = getParent(asset);
        return dto;
    }

    public AssetDTO getParent(Asset asset) {
        if (asset.getAsset() == null) {
            return null;
        }
        AssetDTO parent = new AssetDTO(asset.getAsset().getId(), asset.getAsset().getName(),
                asset.getAsset().getTypeBean().getName(), asset.getAsset().getAssetState(), null, null, null);
        HashMap<String, String> props = new HashMap<>();
        List<MetaData> metaDatas = asset.getMetaData();

        for (MetaData metaData : metaDatas) {
            props.put(metaData.getName(), metaData.getValue());
        }
        parent.props = props;
        parent.parent = getParent(asset.getAsset());
        return parent;
    }

    public AssetDTO[] asAssetDto(Asset[] assets) {
        AssetDTO[] dtos = new AssetDTO[assets.length];
        for (int i = 0; i < dtos.length; i++) {
            dtos[i] = asAssetDto(assets[i]);
        }
        return dtos;
    }
}
