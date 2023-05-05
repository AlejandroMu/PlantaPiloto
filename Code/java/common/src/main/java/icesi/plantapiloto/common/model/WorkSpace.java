package icesi.plantapiloto.common.model;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;

/**
 * The persistent class for the work_space database table.
 * 
 */
@Entity
@Table(name = "work_space")
@NamedQuery(name = "WorkSpace.findAll", query = "SELECT w FROM WorkSpace w")
public class WorkSpace implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name = "work_seq", sequenceName = "work_space_seq", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "work_seq")
	private Integer id;

	private String name;

	private String description;

	// bi-directional many-to-one association to Driver
	@OneToMany(mappedBy = "workSpaceBean")
	private List<Driver> drivers;

	// bi-directional many-to-one association to Process
	@OneToMany(mappedBy = "workSpaceBean")
	private List<Process> processes;

	// bi-directional many-to-one association to WorkSpace
	@ManyToOne
	@JoinColumn(name = "department")
	private WorkSpace workSpace;

	// bi-directional many-to-one association to WorkSpace
	@OneToMany(mappedBy = "workSpace")
	private List<WorkSpace> workSpaces;

	public WorkSpace() {
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

	public List<Driver> getDrivers() {
		return this.drivers;
	}

	public void setDrivers(List<Driver> drivers) {
		this.drivers = drivers;
	}

	public Driver addDriver(Driver driver) {
		getDrivers().add(driver);
		driver.setWorkSpaceBean(this);

		return driver;
	}

	public Driver removeDriver(Driver driver) {
		getDrivers().remove(driver);
		driver.setWorkSpaceBean(null);

		return driver;
	}

	public List<Process> getProcesses() {
		return this.processes;
	}

	public void setProcesses(List<Process> processes) {
		this.processes = processes;
	}

	public Process addProcess(Process process) {
		getProcesses().add(process);
		process.setWorkSpaceBean(this);

		return process;
	}

	public Process removeProcess(Process process) {
		getProcesses().remove(process);
		process.setWorkSpaceBean(null);

		return process;
	}

	public WorkSpace getWorkSpace() {
		return this.workSpace;
	}

	public void setWorkSpace(WorkSpace workSpace) {
		this.workSpace = workSpace;
	}

	public List<WorkSpace> getWorkSpaces() {
		return this.workSpaces;
	}

	public void setWorkSpaces(List<WorkSpace> workSpaces) {
		this.workSpaces = workSpaces;
	}

	public WorkSpace addWorkSpace(WorkSpace workSpace) {
		getWorkSpaces().add(workSpace);
		workSpace.setWorkSpace(this);

		return workSpace;
	}

	public WorkSpace removeWorkSpace(WorkSpace workSpace) {
		getWorkSpaces().remove(workSpace);
		workSpace.setWorkSpace(null);

		return workSpace;
	}

}