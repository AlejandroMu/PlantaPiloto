package icesi.plantapiloto.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(schema = "icesi_bionic_plantapiloto")
public class Value implements Serializable {

    @Id()
    @Column
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "gen_value")
    @SequenceGenerator(name = "gen_value", sequenceName = "seq_value", schema = "icesi_bionic_plantapiloto", allocationSize = 1)
    private Long id;
    @Column
    private Date timeStamp;
    @Column
    private Float value;

    @ManyToOne
    @JoinColumn
    private Channel channel;

    public Value() {

    }

    public Value(Date timeStamp, Float value, Channel channel) {
        this.timeStamp = timeStamp;
        this.value = value;
        this.channel = channel;
    }

    /**
     * @return the id
     */
    public Long getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * @return the timeStamp
     */
    public Date getTimeStamp() {
        return timeStamp;
    }

    /**
     * @param timeStamp the timeStamp to set
     */
    public void setTimeStamp(Date timeStamp) {
        this.timeStamp = timeStamp;
    }

    /**
     * @return the value
     */
    public Float getValue() {
        return value;
    }

    /**
     * @param value the value to set
     */
    public void setValue(Float value) {
        this.value = value;
    }

    /**
     * @return the channel
     */
    public Channel getChannel() {
        return channel;
    }

    /**
     * @param channel the channel to set
     */
    public void setChannel(Channel channel) {
        this.channel = channel;
    }

}
