package icesi.plantapiloto.modelManager.controllers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import icesi.plantapiloto.common.dtos.output.AssetDTO;
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
        AssetDTO dtos[] = controller.findByType(2, null);
        List<String> names = Arrays.asList(new String[] { "ANTIESPUMANTE.ACC", "ENT_AGITADOR", "ENT_FCV101" });
        assertEquals("Request size", 3, dtos.length);
        for (AssetDTO assetDTO : dtos) {
            assertTrue(names.contains(assetDTO.name));
        }
    }
}
