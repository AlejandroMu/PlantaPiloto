package icesi.plantapiloto.common.model;

import java.io.Serializable;
import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;

/**
 * The persistent class for the execution database table.
 * 
 */
@Entity
@NamedQuery(name = "Execution.findAll", query = "SELECT e FROM Execution e")
public class Execution implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name = "execution_seq", sequenceName = "execution_seq", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "execution_seq")
	private Integer id;

	@Column(name = "end_date")
	private Timestamp endDate;

	@Column(name = "oper_username")
	private String operUsername;

	@Column(name = "start_date")
	private Timestamp startDate;

	// bi-directional many-to-one association to Process
	@ManyToOne
	@JoinColumn(name = "process")
	private Process processBean;

	// bi-directional many-to-one association to ExecutionInstruction
	@OneToMany(mappedBy = "executionBean")
	private List<ExecutionInstruction> executionInstructions;

	// bi-directional many-to-one association to Measurement
	@OneToMany(mappedBy = "executionBean")
	private List<Measurement> measurements;

	public Execution() {
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Timestamp getEndDate() {
		return this.endDate;
	}

	public void setEndDate(Timestamp endDate) {
		this.endDate = endDate;
	}

	public String getOperUsername() {
		return this.operUsername;
	}

	public void setOperUsername(String operUsername) {
		this.operUsername = operUsername;
	}

	public Timestamp getStartDate() {
		return this.startDate;
	}

	public void setStartDate(Timestamp startDate) {
		this.startDate = startDate;
	}

	public Process getProcessBean() {
		return this.processBean;
	}

	public void setProcessBean(Process processBean) {
		this.processBean = processBean;
	}

	public List<ExecutionInstruction> getExecutionInstructions() {
		return this.executionInstructions;
	}

	public void setExecutionInstructions(List<ExecutionInstruction> executionInstructions) {
		this.executionInstructions = executionInstructions;
	}

	public ExecutionInstruction addExecutionInstruction(ExecutionInstruction executionInstruction) {
		getExecutionInstructions().add(executionInstruction);
		executionInstruction.setExecutionBean(this);

		return executionInstruction;
	}

	public ExecutionInstruction removeExecutionInstruction(ExecutionInstruction executionInstruction) {
		getExecutionInstructions().remove(executionInstruction);
		executionInstruction.setExecutionBean(null);

		return executionInstruction;
	}

	public List<Measurement> getMeasurements() {
		return this.measurements;
	}

	public void setMeasurements(List<Measurement> measurements) {
		this.measurements = measurements;
	}

	public Measurement addMeasurement(Measurement measurement) {
		getMeasurements().add(measurement);
		measurement.setExecutionBean(this);

		return measurement;
	}

	public Measurement removeMeasurement(Measurement measurement) {
		getMeasurements().remove(measurement);
		measurement.setExecutionBean(null);

		return measurement;
	}

}