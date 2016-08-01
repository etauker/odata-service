package com.etauker.java.persistence.jpa.beans;

import static javax.ejb.TransactionManagementType.CONTAINER;

import java.util.List;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionManagement;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import com.etauker.java.persistence.jpa.entities.Employee;

/**
 * Bean encapsulating all operations for an Employee
 */
@Stateless
@LocalBean
@TransactionManagement(value=CONTAINER)
public class EmployeeBean {
	private static final String PERSISTENCE_UNIT_NAME = "persistence";
	private EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);

	
	@PersistenceContext(unitName=PERSISTENCE_UNIT_NAME)
	private EntityManager em = entityManagerFactory.createEntityManager();
	
	/**
	 * Get all Employees from the table
	 */
	@TransactionAttribute
	public List<Employee> getAllEmployees() throws Exception {
		if (em.createNamedQuery("AllEmplyees", Employee.class) != null) {
			return em.createNamedQuery("AllEmplyees", Employee.class).getResultList();
		}
		else {
			return null;
		}
	}
	
	/**
	 * Get single Employee by it's id
	 */
	@TransactionAttribute
	public Employee getById(String id) throws Exception {
		if (em.createNamedQuery("ByEmplyeeId", Employee.class) != null) {
			TypedQuery<Employee> query = em.createNamedQuery("ByEmplyeeId", Employee.class);
			return query.setParameter("id", id).getSingleResult();
		}
		else {
			return null;
		}
	}
	
	/**
	 * Add a new Employee to the table
	 */
	@TransactionAttribute
	public void addEmployee(Employee employee) throws Exception {
		em.getTransaction().begin();
		em.persist(employee);
		//em.flush();
	    em.getTransaction().commit();
	    em.close();
	}

}
