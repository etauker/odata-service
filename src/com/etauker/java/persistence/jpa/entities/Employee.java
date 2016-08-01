package com.etauker.java.persistence.jpa.entities;

import static javax.persistence.GenerationType.AUTO;

import java.io.Serializable;
import java.lang.String;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;


/**
 * Entity implementation class for Entity: Employee
 *
 */
@Entity
@Table(name = "Employee")
@NamedQueries({
	@NamedQuery(name = "AllEmployees", query = "SELECT e FROM Employee e"),
	@NamedQuery(name = "ByEmployeeId", query = "SELECT e FROM Employee e WHERE e.id = :id")
})
public class Employee implements Serializable {

	@Id
	@GeneratedValue(strategy = AUTO)
	@NotNull
	@Column(name = "Employee_Id")
	private String id;
	
	@Column(name = "Employee_First_Name")
	private String firstName;   
	
	@Column(name = "Employee_Last_Name")
	private String lastName;   
	
	@Column(name = "Employee_Date_Of_Birth")
	private Date dateOfBirth;
	
	@ManyToOne(optional=false)
	@JoinColumn(name="Department_Id", nullable=false)
	private Department department;
	
	
	
	private static final long serialVersionUID = 1L;

	public Employee() {
		super();
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public Date getDateOfBirth() {
		return dateOfBirth;
	}

	public void setDateOfBirth(Date dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}

	public Department getDepartment() {
		return department;
	}

	public void setDepartment(Department department) {
		this.department = department;
	}

	@Override
	public String toString() {
		return "Employee [id=" + id + ", firstName=" + firstName + ", lastName=" + lastName + ", dateOfBirth="
				+ dateOfBirth + ", department=" + department + "]";
	}
  
   
}
