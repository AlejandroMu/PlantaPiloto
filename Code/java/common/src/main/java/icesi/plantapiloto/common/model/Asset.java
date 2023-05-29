package icesi.plantapiloto.common.model;

import java.io.Serializable;
import javax.persistence.*;

import java.util.ArrayList;
import java.util.List;

/**
 * The persistent class for the asset database table.
 * 
 */
@Entity
@NamedQuery(name = "Asset.findAll", query = "SELECT a FROM Asset a")
public class Asset implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name = "asset_seq", sequenceName = "asset_seq", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "asset_seq")
	private Integer id;

	private String name;

	private String description;

	// bi-directional many-to-one association to Type
	@ManyToOne
	@JoinColumn(name = "type")
	private Type typeBean;

	// bi-directional many-to-one association to WorkSpace
	@ManyToOne
	@JoinColumn(name = "work_space")
	private WorkSpace workSpace;

	// bi-directional many-to-one association to Asset
	@ManyToOne
	@JoinColumn(name = "asset_sup")
	private Asset asset;

	@Column(name = "asset_state")
	private String assetState;

	// bi-directional many-to-one association to Asset
	@OneToMany(mappedBy = "asset")
	private List<Asset> assets;

	// bi-directional many-to-one association to Measurement
	@OneToMany(mappedBy = "assetBean")
	private List<Measurement> measurements;

	// bi-directional many-to-one association to MetaData
	@OneToMany(mappedBy = "assetBean", cascade = { CascadeType.REMOVE, CascadeType.PERSIST, CascadeType.MERGE })
	private List<MetaData> metaData;

	// bi-directional many-to-one association to ProcessAsset
	@OneToMany(mappedBy = "asset")
	private List<ProcessAsset> processAssets;

	public Asset() {
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getAssetState() {
		return this.assetState;
	}

	public void setAssetState(String assetState) {
		this.assetState = assetState;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Asset getAsset() {
		return this.asset;
	}

	public void setAsset(Asset asset) {
		this.asset = asset;
	}

	public List<Asset> getAssets() {
		return this.assets;
	}

	public void setAssets(List<Asset> assets) {
		this.assets = assets;
	}

	public Asset addAsset(Asset asset) {
		getAssets().add(asset);
		asset.setAsset(this);

		return asset;
	}

	public Asset removeAsset(Asset asset) {
		getAssets().remove(asset);
		asset.setAsset(null);

		return asset;
	}

	public WorkSpace getWorkSpace() {
		return this.workSpace;
	}

	public void setWorkSpace(WorkSpace driverBean) {
		this.workSpace = driverBean;
	}

	public Type getTypeBean() {
		return this.typeBean;
	}

	public void setTypeBean(Type typeBean) {
		this.typeBean = typeBean;
	}

	public List<Measurement> getMeasurements() {
		return this.measurements;
	}

	public void setMeasurements(List<Measurement> measurements) {
		this.measurements = measurements;
	}

	public Measurement addMeasurement(Measurement measurement) {
		getMeasurements().add(measurement);
		measurement.setAssetBean(this);

		return measurement;
	}

	public Measurement removeMeasurement(Measurement measurement) {
		getMeasurements().remove(measurement);
		measurement.setAssetBean(null);

		return measurement;
	}

	public List<MetaData> getMetaData() {
		if (this.metaData == null) {
			this.metaData = new ArrayList<>();
		}
		return this.metaData;
	}

	public void setMetaData(List<MetaData> metaData) {
		this.metaData = metaData;
	}

	public MetaData addMetaData(MetaData metaData) {
		getMetaData().add(metaData);
		metaData.setAssetBean(this);

		return metaData;
	}

	public MetaData removeMetaData(MetaData metaData) {
		getMetaData().remove(metaData);
		metaData.setAssetBean(null);

		return metaData;
	}

	public List<ProcessAsset> getProcessAssets() {
		if (this.processAssets == null) {
			this.processAssets = new ArrayList<>();
		}
		return this.processAssets;
	}

	public void setProcessAssets(List<ProcessAsset> processAssets) {
		this.processAssets = processAssets;
	}

	public ProcessAsset addProcessAsset(ProcessAsset processAsset) {
		getProcessAssets().add(processAsset);
		processAsset.setAsset(this);

		return processAsset;
	}

	public ProcessAsset removeProcessAsset(ProcessAsset processAsset) {
		getProcessAssets().remove(processAsset);
		processAsset.setAsset(null);

		return processAsset;
	}

}