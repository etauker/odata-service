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

import com.etauker.java.persistence.jpa.entities.Department;

/**
 * Bean encapsulating all operations for a Department
 */
@Stateless
@LocalBean
@TransactionManagement(value=CONTAINER)
public class DepartmentBean {
	private static final String PERSISTENCE_UNIT_NAME = "persistence";
	private EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
	
	@PersistenceContext(unitName=PERSISTENCE_UNIT_NAME)
	private EntityManager em = entityManagerFactory.createEntityManager();
	
	/**
	 * Get all Departments from the table
	 */
	@TransactionAttribute
	public List<Department> getAllDepartments() throws Exception {
		if (em.createNamedQuery("AllDepartments", Department.class) != null) {
			return em.createNamedQuery("AllDepartments", Department.class).getResultList();
		}
		else {
			return null;
		}
	}
	
	/**
	 * Get single Department by it's id
	 */
	@TransactionAttribute
	public Department getById(String id) throws Exception {
		if (em.createNamedQuery("ByDepartmentId", Department.class) != null) {
			TypedQuery<Department> query = em.createNamedQuery("ByDepartmentId", Department.class);
			return query.setParameter("id", id).getSingleResult();
		}
		else {
			return null;
		}
	}
	
	/**
	 * Add a new Department to the table
	 */
	@TransactionAttribute
	public void addDepartment(Department department) throws Exception {
		em.getTransaction().begin();
		em.persist(department);
		//em.flush();
	    em.getTransaction().commit();
	    em.close();
	}

}
