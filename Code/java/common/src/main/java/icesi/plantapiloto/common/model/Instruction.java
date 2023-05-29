package icesi.plantapiloto.common.model;

import java.io.Serializable;
import javax.persistence.*;

import java.util.ArrayList;
import java.util.List;

/**
 * The persistent class for the instruction database table.
 * 
 */
@Entity
@NamedQuery(name = "Instruction.findAll", query = "SELECT i FROM Instruction i")
public class Instruction implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name = "instruction_seq", sequenceName = "instruction_seq", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "instruction_seq")
	private Integer id;

	@Column(name = "name_tech")
	private String nameTech;

	private String predicate;

	private String type;

	// bi-directional many-to-many association to Action
	@ManyToMany(mappedBy = "instructions", cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	private List<Action> actions;

	// bi-directional many-to-one association to ExecutionInstruction
	@OneToMany(mappedBy = "instructionBean")
	private List<ExecutionInstruction> executionInstructions;

	// bi-directional many-to-many association to Process
	@ManyToMany
	@JoinTable(name = "instruction_process", joinColumns = {
			@JoinColumn(name = "instruction")
	}, inverseJoinColumns = {
			@JoinColumn(name = "process")
	})
	private List<Process> processes;

	public Instruction() {
	}

	public String getNameTech() {
		return this.nameTech;
	}

	public void setNameTech(String nameTech) {
		this.nameTech = nameTech;
	}

	public String getPredicate() {
		return this.predicate;
	}

	public void setPredicate(String predicate) {
		this.predicate = predicate;
	}

	public String getType() {
		return this.type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public List<Action> getActions() {
		if (this.actions == null) {
			this.actions = new ArrayList<>();
		}
		return this.actions;
	}

	public void setActions(List<Action> actions) {
		this.actions = actions;
	}

	public Action addAction(Action action) {
		getActions().add(action);
		action.addInstruction(this);
		return action;
	}

	public List<ExecutionInstruction> getExecutionInstructions() {
		return this.executionInstructions;
	}

	public void setExecutionInstructions(List<ExecutionInstruction> executionInstructions) {
		this.executionInstructions = executionInstructions;
	}

	public ExecutionInstruction addExecutionInstruction(ExecutionInstruction executionInstruction) {
		getExecutionInstructions().add(executionInstruction);
		executionInstruction.setInstructionBean(this);

		return executionInstruction;
	}

	public ExecutionInstruction removeExecutionInstruction(ExecutionInstruction executionInstruction) {
		getExecutionInstructions().remove(executionInstruction);
		executionInstruction.setInstructionBean(null);

		return executionInstruction;
	}

	public List<Process> getProcesses() {
		return this.processes;
	}

	public void setProcesses(List<Process> processes) {
		this.processes = processes;
	}

	public void addProcess(Process process) {
		getProcesses().add(process);
	}

}