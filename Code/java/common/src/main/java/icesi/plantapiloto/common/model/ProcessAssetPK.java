package icesi.plantapiloto.common.model;

import java.io.Serializable;
import javax.persistence.*;

/**
 * The primary key class for the process_asset database table.
 * 
 */
@Embeddable
public class ProcessAssetPK implements Serializable {
	// default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	@Column(name = "asset_id")
	private Integer assetId;

	@Column(name = "process_id")
	private Integer processId;

	public ProcessAssetPK() {
	}

	public Integer getAssetId() {
		return this.assetId;
	}

	public void setAssetId(Integer assetId) {
		this.assetId = assetId;
	}

	public Integer getProcessId() {
		return this.processId;
	}

	public void setProcessId(Integer processId) {
		this.processId = processId;
	}

	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof ProcessAssetPK)) {
			return false;
		}
		ProcessAssetPK castOther = (ProcessAssetPK) other;
		return this.assetId.equals(castOther.assetId)
				&& this.processId.equals(castOther.processId);
	}

	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.assetId.hashCode();
		hash = hash * prime + this.processId.hashCode();

		return hash;
	}
}