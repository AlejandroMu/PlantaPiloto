package icesi.plantapiloto.common.model;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;

/**
 * The persistent class for the driver database table.
 * 
 */
@Entity
@NamedQuery(name = "Driver.findAll", query = "SELECT d FROM Driver d")
public class Driver implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name = "driver_seq", sequenceName = "driver_seq", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "driver_seq")
	private Integer id;

	private String name;

	@Column(name = "service_proxy")
	private String serviceProxy;

	// bi-directional many-to-one association to Asset
	@OneToMany(mappedBy = "driverBean")
	private List<Asset> assets;

	// bi-directional many-to-one association to WorkSpace
	@ManyToOne
	@JoinColumn(name = "work_space")
	private WorkSpace workSpaceBean;

	public Driver() {
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getServiceProxy() {
		return this.serviceProxy;
	}

	public void setServiceProxy(String serviceProxy) {
		this.serviceProxy = serviceProxy;
	}

	public List<Asset> getAssets() {
		return this.assets;
	}

	public void setAssets(List<Asset> assets) {
		this.assets = assets;
	}

	public Asset addAsset(Asset asset) {
		getAssets().add(asset);
		asset.setDriverBean(this);

		return asset;
	}

	public Asset removeAsset(Asset asset) {
		getAssets().remove(asset);
		asset.setDriverBean(null);

		return asset;
	}

	public WorkSpace getWorkSpaceBean() {
		return this.workSpaceBean;
	}

	public void setWorkSpaceBean(WorkSpace workSpaceBean) {
		this.workSpaceBean = workSpaceBean;
	}

}