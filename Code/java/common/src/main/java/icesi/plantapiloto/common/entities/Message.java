package icesi.plantapiloto.common.entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Message implements Serializable {

    private String sourceData;

    private String type;

    private Date time;

    private List<Measure> measures;

    private String topic;

    public Message() {
        measures = new ArrayList<>();
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
     * @return the topic
     */
    public String getTopic() {
        return topic;
    }

    /**
     * @param topic the topic to set
     */
    public Message setTopic(String topic) {
        this.topic = topic;
        return this;
    }

    /**
     * @return the measures
     */
    public List<Measure> getMeasures() {
        return measures;
    }

    /**
     * @param measures the measures to set
     */
    public Message setMeasures(List<Measure> measures) {
        this.measures = measures;
        return this;
    }

    public void addMeasure(Measure measure) {
        this.measures.add(measure);
    }

}
