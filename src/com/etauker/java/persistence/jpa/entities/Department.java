package com.etauker.java.persistence.jpa.entities;

import static javax.persistence.GenerationType.AUTO;

import java.io.Serializable;
import java.lang.String;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;


/**
 * Entity implementation class for Entity: Department
 *
 */
@Entity
@Table(name = "Department")
@NamedQueries({
	@NamedQuery(name = "AllDepartments", query = "SELECT d FROM Department d"),
	@NamedQuery(name = "ByDepartmentId", query = "SELECT d FROM Department d WHERE d.id = :id")
})
public class Department implements Serializable {


	@Id
	@GeneratedValue(strategy = AUTO)
	@NotNull
	@Column(name = "Department_Id")
	private String id;
	
	@Column(name = "Department_Title")
	private String title;   

	@OneToMany(mappedBy="department")
	private Set<Employee> employees = new HashSet<Employee>();
	
	
	
	private static final long serialVersionUID = 1L;

	public Department() {
		super();
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Set<Employee> getEmployees() {
		return employees;
	}

	public void setEmployees(Set<Employee> employees) {
		this.employees = employees;
	}

	@Override
	public String toString() {
		return "Department [id=" + id + ", title=" + title + ", employees=" + employees + "]";
	}
   
}
