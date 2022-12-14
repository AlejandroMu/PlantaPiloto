package icesi.plantapiloto.model;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * ControllableDevice
 */
@Entity
@Table(schema = "icesi_bionic_plantapiloto")
public class ControllableDevice implements Serializable {

    @Id
    private Long id;

    @Column
    private String type;

    @Column
    private String processor;

    @OneToMany(mappedBy = "device", cascade = CascadeType.ALL)
    private List<IOModule> modules;

    /**
     * 
     */
    public ControllableDevice() {
    }

    /**
     * @param id
     * @param type
     * @param processor
     */
    public ControllableDevice(Long id, String type, String processor) {
        this.id = id;
        this.type = type;
        this.processor = processor;
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
     * @return the processor
     */
    public String getProcessor() {
        return processor;
    }

    /**
     * @param processor the processor to set
     */
    public void setProcessor(String processor) {
        this.processor = processor;
    }

    /**
     * @return the modules
     */
    public List<IOModule> getModules() {
        return modules;
    }

    /**
     * @param modules the modules to set
     */
    public void setModules(List<IOModule> modules) {
        this.modules = modules;
    }

}