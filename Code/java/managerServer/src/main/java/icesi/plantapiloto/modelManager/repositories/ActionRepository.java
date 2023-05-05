package icesi.plantapiloto.modelManager.repositories;

import icesi.plantapiloto.common.model.Action;

public class ActionRepository implements Repository<Action, String> {
    @Override
    public Class<Action> getType() {
        return Action.class;
    }

}
