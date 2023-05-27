package icesi.plantapiloto.modelManager.repositories;

import org.junit.BeforeClass;
import org.junit.Test;

public class AssetRepositoryTest {
    @BeforeClass
    public static void setUp1() {
        System.getProperties().forEach((k, v) -> {
            System.out.println("Key: " + k + " Value: " + v);
        });
    }

    @Test
    public void testGetAssetByType() {

        // AssetRepository repository = AssetRepository.getInstance();
        // List<Asset> assets = repository.findByType(1);
        // assertEquals("getType PLC", 1, assets.size());
        // assertEquals("getType PLC Name", "B40B", assets.get(0).getName());

        // assets = repository.findByType(2);
        // assertEquals("getType TAG: ", 3, assets.size());
    }

}
