package icesi.plantapiloto.common.model;

import java.io.Serializable;
import javax.persistence.*;

/**
 * The persistent class for the meta_data database table.
 * 
 */
@Entity
@Table(name = "meta_data")
@NamedQuery(name = "MetaData.findAll", query = "SELECT m FROM MetaData m")
public class MetaData implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name = "meta_data_seq", sequenceName = "meta_data_seq", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "meta_data_seq")
	private Long id;

	private String name;

	private String value;

	private String description;

	// bi-directional many-to-one association to Asset
	@ManyToOne
	@JoinColumn(name = "asset")
	private Asset assetBean;

	public MetaData() {
	}

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
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

	public String getValue() {
		return this.value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public Asset getAssetBean() {
		return this.assetBean;
	}

	public void setAssetBean(Asset assetBean) {
		this.assetBean = assetBean;
	}

}