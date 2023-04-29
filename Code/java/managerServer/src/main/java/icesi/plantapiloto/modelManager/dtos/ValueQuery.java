package icesi.plantapiloto.modelManager.dtos;

import java.util.List;

public class ValueQuery {
    private String plcName;
    private long initDate;
    private long finalDate;
    private List<String> tagsNames;

    /**
     * @return the plcName
     */
    public String getPlcName() {
        return plcName;
    }

    /**
     * @param plcName the plcName to set
     */
    public void setPlcName(String plcName) {
        this.plcName = plcName;
    }

    /**
     * @return the initDate
     */
    public long getInitDate() {
        return initDate;
    }

    /**
     * @param initDate the initDate to set
     */
    public void setInitDate(long initDate) {
        this.initDate = initDate;
    }

    /**
     * @return the finalDate
     */
    public long getFinalDate() {
        return finalDate;
    }

    /**
     * @param finalDate the finalDate to set
     */
    public void setFinalDate(long finalDate) {
        this.finalDate = finalDate;
    }

    /**
     * @return the tagsNames
     */
    public List<String> getTagsNames() {
        return tagsNames;
    }

    /**
     * @param tagsNames the tagsNames to set
     */
    public void setTagsNames(List<String> tagsNames) {
        this.tagsNames = tagsNames;
    }

}
