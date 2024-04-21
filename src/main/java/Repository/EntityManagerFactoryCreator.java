package Repository;

import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class EntityManagerFactoryCreator {
	private static final String PERSISTENCE_UNIT_NAME = "myPersistence";
    private static EntityManagerFactory entityManagerFactory;
    
    private EntityManagerFactoryCreator() {
    }

    public static EntityManagerFactory obtainOrCreateEntityManagerFactory() {
        if (entityManagerFactory == null || !entityManagerFactory.isOpen()) {
            createEntityManagerFactory();
        }
        return entityManagerFactory;
    }

    private static synchronized void createEntityManagerFactory() {
        if (entityManagerFactory == null || !entityManagerFactory.isOpen()) {
            entityManagerFactory = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
        }
    }

    public static void closeEntityManagerFactory() {
        if (entityManagerFactory != null && entityManagerFactory.isOpen()) {
            entityManagerFactory.close();
        }
    }
}
