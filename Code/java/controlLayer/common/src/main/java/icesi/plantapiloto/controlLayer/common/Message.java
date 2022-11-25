package icesi.plantapiloto.controlLayer.common;

import java.io.Serializable;
import java.util.Date;

public class Message implements Serializable {

    private String sourceData;

    private String type;

    private Date time;

    private String value;

    private String name;

    public Message() {
    }

    /**
     * @return the sourceData
     */
    public String getSourceData() {
        return sourceData;
    }

    /**
     * @param sourceData the sourceData to set
     */
    public Message setSourceData(String sourceData) {
        this.sourceData = sourceData;
        return this;
    }

    /**
     * @return the type
     */
    public String getType() {
        return type;
    }

    /**
     * @param type the type to set
     */
    public Message setType(String type) {
        this.type = type;
        return this;

    }

    /**
     * @return the time
     */
    public Date getTime() {
        return time;
    }

    /**
     * @param time the time to set
     */
    public Message setTime(Date time) {
        this.time = time;
        return this;

    }

    /**
     * @return the value
     */
    public String getValue() {
        return value;
    }

    /**
     * @param value the value to set
     */
    public Message setValue(String value) {
        this.value = value;
        return this;

    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public Message setName(String name) {
        this.name = name;
        return this;
    }

}
