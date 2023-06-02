package icesi.plantapiloto.common.mappers;

import java.util.HashMap;
import java.util.List;

import icesi.plantapiloto.common.dtos.output.AssetDTO;
import icesi.plantapiloto.common.model.Asset;
import icesi.plantapiloto.common.model.MetaData;

public class AssetMapper implements Maper<Asset, AssetDTO> {
    private static AssetMapper instance;

    public static AssetMapper getInstance() {
        if (instance == null) {
            instance = new AssetMapper();
        }
        return instance;
    }

    private AssetMapper() {

    }

    public AssetDTO asEntityDTO(Asset asset) {
        AssetDTO dto = new AssetDTO(asset.getId(), asset.getName(), asset.getTypeBean().getName(),
                asset.getAssetState(), asset.getDescription(), null, null, asset.getWorkSpace().getId(), null);
        List<MetaData> metaDatas = asset.getMetaData();
        HashMap<String, String> props = new HashMap<>();
        if (metaDatas != null) {
            for (MetaData metaData : metaDatas) {
                props.put(metaData.getName(), metaData.getValue());
            }
        }
        if (asset.getAssets() != null) {
            AssetDTO[] childs = new AssetDTO[asset.getAssets().size()];
            for (int i = 0; i < childs.length; i++) {
                childs[i] = asEntityDTO(asset.getAssets().get(i));
            }

            dto.childrens = childs;
        }
        dto.props = props;
        dto.parent = getParent(asset);
        return dto;
    }

    public AssetDTO getParent(Asset asset) {
        Asset assetPar = asset.getAsset();
        if (assetPar == null) {
            return null;
        }
        AssetDTO parent = new AssetDTO(assetPar.getId(), assetPar.getName(),
                assetPar.getTypeBean().getName(), assetPar.getAssetState(), assetPar.getDescription(), null, null,
                assetPar.getWorkSpace().getId(),
                null);
        HashMap<String, String> props = new HashMap<>();
        List<MetaData> metaDatas = assetPar.getMetaData();
        if (metaDatas != null) {
            for (MetaData metaData : metaDatas) {
                props.put(metaData.getName(), metaData.getValue());
            }
        }
        parent.props = props;
        parent.parent = getParent(assetPar);
        return parent;
    }

    public Asset updateAsset(AssetDTO dto, Asset asset) {
        return asset;
    }

}
