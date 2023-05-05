package icesi.plantapiloto.common.model;

import java.io.Serializable;
import javax.persistence.*;
import java.sql.Timestamp;

/**
 * The persistent class for the execution_instruction database table.
 * 
 */
@Entity
@Table(name = "execution_instruction")
@NamedQuery(name = "ExecutionInstruction.findAll", query = "SELECT e FROM ExecutionInstruction e")
public class ExecutionInstruction implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name = "execution_inst_seq", sequenceName = "execution_inst_seq", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "execution_inst_seq")
	private Integer id;

	@Column(name = "exc_time")
	private Timestamp excTime;

	// bi-directional many-to-one association to Execution
	@ManyToOne
	@JoinColumn(name = "execution")
	private Execution executionBean;

	// bi-directional many-to-one association to Instruction
	@ManyToOne
	@JoinColumn(name = "instruction")
	private Instruction instructionBean;

	public ExecutionInstruction() {
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Timestamp getExcTime() {
		return this.excTime;
	}

	public void setExcTime(Timestamp excTime) {
		this.excTime = excTime;
	}

	public Execution getExecutionBean() {
		return this.executionBean;
	}

	public void setExecutionBean(Execution executionBean) {
		this.executionBean = executionBean;
	}

	public Instruction getInstructionBean() {
		return this.instructionBean;
	}

	public void setInstructionBean(Instruction instructionBean) {
		this.instructionBean = instructionBean;
	}

}