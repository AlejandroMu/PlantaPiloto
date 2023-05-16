package icesi.plantapiloto.common.mappers;

import icesi.plantapiloto.common.dtos.ActionDTO;
import icesi.plantapiloto.common.model.Action;

/**
 * ActionMapper
 */
public class ActionMapper implements Maper<Action, ActionDTO> {

    private static ActionMapper instance;

    public static ActionMapper getInstance() {
        if (instance == null) {
            instance = new ActionMapper();
        }
        return instance;
    }

    private ActionMapper() {

    }

    @Override
    public ActionDTO asEntityDTO(Action entity) {
        ActionDTO dto = new ActionDTO(entity.getId(), entity.getNameTech(), entity.getNameUser(),
                entity.getFunctionAct());
        return dto;
    }
}