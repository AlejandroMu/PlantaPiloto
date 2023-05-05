package icesi.plantapiloto.common.model;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;

/**
 * The persistent class for the action database table.
 * 
 */
@Entity
@NamedQuery(name = "Action.findAll", query = "SELECT a FROM Action a")
public class Action implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name = "action_seq", sequenceName = "action_seq", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "action_seq")
	private Integer id;

	@Column(name = "name_user")
	private String nameUser;

	@Column(name = "name_tech")
	private String nameTech;

	@Column(name = "function_act")
	private String functionAct;

	// bi-directional many-to-many association to Instruction
	@ManyToMany
	@JoinTable(name = "action_instruction", joinColumns = {
			@JoinColumn(name = "action")
	}, inverseJoinColumns = {
			@JoinColumn(name = "instruction")
	})
	private List<Instruction> instructions;

	public Action() {
	}

	public String getNameTech() {
		return this.nameTech;
	}

	public void setNameTech(String nameTech) {
		this.nameTech = nameTech;
	}

	public String getFunctionAct() {
		return this.functionAct;
	}

	public void setFunctionAct(String functionAct) {
		this.functionAct = functionAct;
	}

	public String getNameUser() {
		return this.nameUser;
	}

	public void setNameUser(String nameUser) {
		this.nameUser = nameUser;
	}

	public List<Instruction> getInstructions() {
		return this.instructions;
	}

	public void setInstructions(List<Instruction> instructions) {
		this.instructions = instructions;
	}

}