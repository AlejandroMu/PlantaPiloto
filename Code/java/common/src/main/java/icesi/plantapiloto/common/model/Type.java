package icesi.plantapiloto.common.model;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;

/**
 * The persistent class for the type database table.
 * 
 */
@Entity
@NamedQuery(name = "Type.findAll", query = "SELECT t FROM Type t")
public class Type implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name = "type_seq", sequenceName = "type_seq", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "type_seq")
	private Integer id;

	private String name;

	private String description;

	@Column(name = "table_name")
	private String tableName;

	// bi-directional many-to-one association to Asset
	@OneToMany(mappedBy = "typeBean")
	private List<Asset> assets;

	public Type() {
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
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

	public String getTableName() {
		return this.tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public List<Asset> getAssets() {
		return this.assets;
	}

	public void setAssets(List<Asset> assets) {
		this.assets = assets;
	}

	public Asset addAsset(Asset asset) {
		getAssets().add(asset);
		asset.setTypeBean(this);

		return asset;
	}

	public Asset removeAsset(Asset asset) {
		getAssets().remove(asset);
		asset.setTypeBean(null);

		return asset;
	}

}