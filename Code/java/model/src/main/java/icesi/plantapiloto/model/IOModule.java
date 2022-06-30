package icesi.plantapiloto.model;

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
@Table
public class IOModule {
    
    @Id
    private Long id;

    @Column
    private String name;
    @Column
    private String type;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn
    private ControllableDevice device;
    @OneToMany(mappedBy = "module", cascade = CascadeType.ALL)
    private List<Channel> channels;
    /**
     * 
     */
    public IOModule() {
    }
    /**
     * @param id
     * @param name
     * @param type
     * @param device
     */
    public IOModule(Long id, String name, String type, ControllableDevice device) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.device = device;
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
     * @return the device
     */
    public ControllableDevice getDevice() {
        return device;
    }
    /**
     * @param device the device to set
     */
    public void setDevice(ControllableDevice device) {
        this.device = device;
    }
    /**
     * @return the channels
     */
    public List<Channel> getChannels() {
        return channels;
    }
    /**
     * @param channels the channels to set
     */
    public void setChannels(List<Channel> channels) {
        this.channels = channels;
    }
    

    
}
