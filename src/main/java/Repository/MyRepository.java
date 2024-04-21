package Repository;

import java.util.List;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class MyRepository<T> {
	
	private final Class<T> entityClass;
	private final EntityManagerFactory entityManagerFactory;
	
	public MyRepository(Class<T> entityClass, EntityManagerFactory entityManagerFactory) {
		this.entityClass = entityClass;
		this.entityManagerFactory = entityManagerFactory;
	}
	
	public void save(T entity) {
		EntityManager entityManager = entityManagerFactory.createEntityManager();
		try {
			entityManager.getTransaction().begin();
			entityManager.persist(entity);
			entityManager.getTransaction().commit();
		} finally {
			entityManager.close();
		}
	}
	
	public void update(T entity) {
		EntityManager entityManager = entityManagerFactory.createEntityManager();
		try {
			entityManager.getTransaction().begin();
			entityManager.merge(entity);
			entityManager.getTransaction().commit();
		} finally {
			entityManager.close();
		}
	}
	
	public void delete(Object id) {
		EntityManager entityManager = entityManagerFactory.createEntityManager();
	    try {
	        entityManager.getTransaction().begin();
	        T entity = entityManager.find(entityClass, id);
	        if (entity != null) {
	            entityManager.remove(entity);
	        }
	        entityManager.getTransaction().commit();
	    } finally {
	        entityManager.close();
	    }
	}
	
	public void deleteByEntity(T entity) {
		EntityManager entityManager = entityManagerFactory.createEntityManager();
		try {
			entityManager.getTransaction().begin();
			entityManager.remove(entity);
			entityManager.getTransaction().commit();
		} finally {
			entityManager.close();
		}
	}
	
	
	public T findById(Object id) {
		EntityManager entityManager = entityManagerFactory.createEntityManager();
		try {
			return entityManager.find(entityClass, id);
		} finally {
			entityManager.close();
		}
	}
	
	public List<T> findAll(){
		EntityManager entityManager = entityManagerFactory.createEntityManager();
		try {
            return entityManager.createQuery("SELECT e FROM " + entityClass.getSimpleName() + " e", entityClass).getResultList();
        } finally {
            entityManager.close();
        }
	}
	
	
}
