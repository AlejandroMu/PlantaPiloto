package icesi.plantapiloto.common.model;

import java.io.Serializable;
import javax.persistence.*;
import java.sql.Timestamp;

/**
 * The persistent class for the measurement database table.
 * 
 */
@Entity
@NamedQuery(name = "Measurement.findAll", query = "SELECT m FROM Measurement m")
public class Measurement implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name = "measure_seq", sequenceName = "measure_seq", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "measure_seq")
	private Integer id;

	private Timestamp time;

	private double value;

	// bi-directional many-to-one association to Asset
	@ManyToOne
	@JoinColumn(name = "asset")
	private Asset assetBean;

	// bi-directional many-to-one association to Execution
	@ManyToOne
	@JoinColumn(name = "execution")
	private Execution executionBean;

	public Measurement() {
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Timestamp getTime() {
		return this.time;
	}

	public void setTime(Timestamp time) {
		this.time = time;
	}

	public double getValue() {
		return this.value;
	}

	public void setValue(double value) {
		this.value = value;
	}

	public Asset getAssetBean() {
		return this.assetBean;
	}

	public void setAssetBean(Asset assetBean) {
		this.assetBean = assetBean;
	}

	public Execution getExecutionBean() {
		return this.executionBean;
	}

	public void setExecutionBean(Execution executionBean) {
		this.executionBean = executionBean;
	}

}