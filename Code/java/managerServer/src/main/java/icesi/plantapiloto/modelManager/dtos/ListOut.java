package icesi.plantapiloto.modelManager.dtos;

import java.util.List;

public class ListOut<T> {
    private List<T> ellements;

    /**
     * @return the ellements
     */
    public List<T> getEllements() {
        return ellements;
    }

    /**
     * @param ellements the ellements to set
     */
    public void setEllements(List<T> ellements) {
        this.ellements = ellements;
    }

}
