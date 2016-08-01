package com.etauker.java.persistence.jpa.entities;

import java.util.Date;

import com.etauker.java.persistence.jpa.beans.DepartmentBean;
import com.etauker.java.persistence.jpa.beans.EmployeeBean;

class Test {
	public static void main(String[] args) throws Exception {
		
		Test test = new Test();
		test.addDepartment("12345", "Production");
		Department dept1 = test.getDepartment("12345");
		System.out.println(dept1.toString());
		
		test.addDepartment("23456", "Sales");
		Department dept2 = test.getDepartment("23456");
		System.out.println(dept2.toString());
		
		test.addDepartment("34567", "Marketing");
		Department dept3 = test.getDepartment("34567");
		System.out.println(dept3.toString());
		
		test.addEmployee("1", "John", "Doe", new Date(), dept1);
		test.addEmployee("2", "Jane", "Doe", new Date(), dept2);
		test.addEmployee("3", "Tom", "Cannon", new Date(), dept3);
		test.addEmployee("4", "James", "Smith", new Date(), dept1);
		test.addEmployee("5", "Liam", "O'Connor", new Date(), dept2);
		test.addEmployee("6", "Mary", "Johnson", new Date(), dept3);
		test.addEmployee("7", "Timothy", "Lee", new Date(), dept1);
		test.addEmployee("8", "Megan", "Simpson", new Date(), dept2);
		test.addEmployee("9", "John", "Tyrrel", new Date(), dept3);
		
		
		

	}
	
	public void addDepartment(String id, String title) throws Exception {
		
		Department dept = new Department();
		DepartmentBean departmentBean = new DepartmentBean();
		
		dept.setId(id);
		dept.setTitle(title);
		
		departmentBean.addDepartment(dept);
	}
	public Department getDepartment(String id) throws Exception {
		
		DepartmentBean departmentBean = new DepartmentBean();
		Department dept = departmentBean.getById(id);
		return dept;
		
	}
	public void addEmployee(String id, String fname, String lname, Date dob, Department dept) throws Exception {
		
		Employee emp = new Employee();
		EmployeeBean employeeBean = new EmployeeBean();
		
		emp.setId(id);
		emp.setFirstName(fname);
		emp.setLastName(lname);
		emp.setDateOfBirth(dob);
		emp.setDepartment(dept);
		
		employeeBean.addEmployee(emp);
	}
}
