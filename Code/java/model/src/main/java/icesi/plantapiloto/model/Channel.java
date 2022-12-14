package icesi.plantapiloto.model;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(schema = "icesi_bionic_plantapiloto")
public class Channel implements Serializable {

    @Id
    @Column
    private Long id;

    @Column
    private String type;
    @Column
    private String name;
    @Column
    private String signal;
    @Column
    private String range;
    @Column
    private String unit;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn
    private IOModule module;

    @OneToMany(mappedBy = "channel", cascade = CascadeType.ALL)
    private List<Value> values;

    public Channel() {

    }

    /**
     * @param id
     * @param type
     * @param signal
     * @param range
     * @param unit
     * @param module
     */
    public Channel(Long id, String type, String name, String signal, String range, String unit, IOModule module) {
        this.id = id;
        this.type = type;
        this.signal = signal;
        this.range = range;
        this.unit = unit;
        this.module = module;
        this.name = name;
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
     * @return the type
     */
    public String getType() {
        return type;
    }

    /**
     * @param type the type to set
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * @return the signal
     */
    public String getSignal() {
        return signal;
    }

    /**
     * @param signal the signal to set
     */
    public void setSignal(String signal) {
        this.signal = signal;
    }

    /**
     * @return the range
     */
    public String getRange() {
        return range;
    }

    /**
     * @param range the range to set
     */
    public void setRange(String range) {
        this.range = range;
    }

    /**
     * @return the unit
     */
    public String getUnit() {
        return unit;
    }

    /**
     * @param unit the unit to set
     */
    public void setUnit(String unit) {
        this.unit = unit;
    }

    /**
     * @return the module
     */
    public IOModule getModule() {
        return module;
    }

    /**
     * @param module the module to set
     */
    public void setModule(IOModule module) {
        this.module = module;
    }

    /**
     * @return the values
     */
    public List<Value> getValues() {
        return values;
    }

    /**
     * @param values the values to set
     */
    public void setValues(List<Value> values) {
        this.values = values;
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
    public void setName(String name) {
        this.name = name;
    }

}
