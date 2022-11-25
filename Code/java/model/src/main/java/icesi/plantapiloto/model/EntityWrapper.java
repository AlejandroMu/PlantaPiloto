package icesi.plantapiloto.model;

import java.io.Serializable;

public class EntityWrapper<T extends Serializable> implements Serializable {
    private T object;

    /**
     * @return the object
     */
    public T getObject() {
        return object;
    }

    /**
     * @param object the object to set
     */
    public void setObject(T object) {
        this.object = object;
    }

}
