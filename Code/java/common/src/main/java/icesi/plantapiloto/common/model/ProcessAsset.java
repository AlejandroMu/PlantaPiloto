package icesi.plantapiloto.common.model;

import java.io.Serializable;
import javax.persistence.*;

/**
 * The persistent class for the process_asset database table.
 * 
 */
@Entity
@Table(name = "process_asset")
@NamedQuery(name = "ProcessAsset.findAll", query = "SELECT p FROM ProcessAsset p")
public class ProcessAsset implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private ProcessAssetPK id;

	@Column(name = "delay_read")
	private Long delayRead;

	// bi-directional many-to-one association to Asset
	@ManyToOne
	@JoinColumn(name = "asset_id", insertable = false, updatable = false)
	private Asset asset;

	// bi-directional many-to-one association to Process
	@ManyToOne
	@JoinColumn(name = "process_id", insertable = false, updatable = false)
	private Process process;

	public ProcessAsset() {
	}

	public ProcessAssetPK getId() {
		return this.id;
	}

	public void setId(ProcessAssetPK id) {
		this.id = id;
	}

	public Long getDelayRead() {
		return this.delayRead;
	}

	public void setDelayRead(Long delayRead) {
		this.delayRead = delayRead;
	}

	public Asset getAsset() {
		return this.asset;
	}

	public void setAsset(Asset asset) {
		this.asset = asset;
	}

	public Process getProcess() {
		return this.process;
	}

	public void setProcess(Process process) {
		this.process = process;
	}

}