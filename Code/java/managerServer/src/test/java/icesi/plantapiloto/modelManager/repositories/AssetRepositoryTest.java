package icesi.plantapiloto.modelManager.repositories;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Test;

import icesi.plantapiloto.common.model.Asset;

public class AssetRepositoryTest {

    @Test
    public void testGetType() {
        AssetRepository repository = new AssetRepository();
        List<Asset> assets = repository.findAll();
        Class<?> type = repository.getType();
        System.out.println("Elements: " + assets.size());
        assertEquals("Test getType", Asset.class, type);
        assertEquals("count assets: ", 58, assets.size());
    }

}
