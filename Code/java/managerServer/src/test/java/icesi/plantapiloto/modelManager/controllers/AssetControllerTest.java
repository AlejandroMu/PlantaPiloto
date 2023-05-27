package icesi.plantapiloto.modelManager.controllers;

import org.junit.Before;
import org.junit.Test;

import icesi.plantapiloto.modelManager.services.AssetService;

public class AssetControllerTest {

    private AssetController controller;

    @Before
    public void setup() {
        controller = new AssetController();
        AssetService assetService = new AssetService();
        controller.setService(assetService);
    }

    @Test
    public void findByType() {
        // AssetDTO dtos[] = controller.findByType(2, null);
        // List<String> names = Arrays.asList(new String[] { "ANTIESPUMANTE.ACC",
        // "ENT_AGITADOR", "ENT_FCV101" });
        // assertEquals("Request size", 3, dtos.length);
        // for (AssetDTO assetDTO : dtos) {
        // assertTrue(names.contains(assetDTO.name));
        // }
    }
}
