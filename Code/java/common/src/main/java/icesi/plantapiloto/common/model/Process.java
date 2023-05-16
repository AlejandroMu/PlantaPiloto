package icesi.plantapiloto.common.model;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;

/**
 * The persistent class for the process database table.
 * 
 */
@Entity
@NamedQuery(name = "Process.findAll", query = "SELECT p FROM Process p")
public class Process implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name = "process_seq", sequenceName = "process_seq", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "process_seq")
	private Integer id;

	private String name;

	private String description;

	// bi-directional many-to-one association to WorkSpace
	@ManyToOne
	@JoinColumn(name = "work_space")
	private WorkSpace workSpaceBean;

	// bi-directional many-to-one association to Execution
	@OneToMany(mappedBy = "processBean")
	private List<Execution> executions;

	// bi-directional many-to-many association to Instruction
	@ManyToMany(mappedBy = "processes", cascade = { CascadeType.MERGE, CascadeType.PERSIST })
	private List<Instruction> instructions;

	// bi-directional many-to-one association to ProcessAsset
	@OneToMany(mappedBy = "process")
	private List<ProcessAsset> processAssets;

	public Process() {
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

	public List<Execution> getExecutions() {
		return this.executions;
	}

	public void setExecutions(List<Execution> executions) {
		this.executions = executions;
	}

	public Execution addExecution(Execution execution) {
		getExecutions().add(execution);
		execution.setProcessBean(this);

		return execution;
	}

	public Execution removeExecution(Execution execution) {
		getExecutions().remove(execution);
		execution.setProcessBean(null);

		return execution;
	}

	public List<Instruction> getInstructions() {
		return this.instructions;
	}

	public void setInstructions(List<Instruction> instructions) {
		this.instructions = instructions;
	}

	public Instruction addInstruction(Instruction instruction) {
		getInstructions().add(instruction);
		instruction.addProcess(this);
		return instruction;
	}

	public WorkSpace getWorkSpaceBean() {
		return this.workSpaceBean;
	}

	public void setWorkSpaceBean(WorkSpace workSpaceBean) {
		this.workSpaceBean = workSpaceBean;
	}

	public List<ProcessAsset> getProcessAssets() {
		return this.processAssets;
	}

	public void setProcessAssets(List<ProcessAsset> processAssets) {
		this.processAssets = processAssets;
	}

	public ProcessAsset addProcessAsset(ProcessAsset processAsset) {
		getProcessAssets().add(processAsset);
		processAsset.setProcess(this);

		return processAsset;
	}

	public ProcessAsset removeProcessAsset(ProcessAsset processAsset) {
		getProcessAssets().remove(processAsset);
		processAsset.setProcess(null);

		return processAsset;
	}

}