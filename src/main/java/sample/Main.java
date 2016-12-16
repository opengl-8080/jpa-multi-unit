package sample;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Main {
    public static void main(String[] args) throws Exception {
        createDatabase("db1", "ALPHA_TABLE");
        createDatabase("db2", "BETA_TABLE");

        unit("unit1").execute(
            em -> {
                Alpha alpha = new Alpha(LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME));
                em.persist(alpha);
            },
            em -> {
                TypedQuery<Alpha> query = em.createQuery("select a from Alpha a order by a.id asc", Alpha.class);
                query.getResultList().forEach(System.out::println);
            }
        );
        
        unit("unit2").execute(
            em -> {
                Beta beta = new Beta(LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME));
                em.persist(beta);
            },
            em -> {
                TypedQuery<Beta> query = em.createQuery("select b from Beta b order by b.id asc", Beta.class);
                query.getResultList().forEach(System.out::println);
            }
        );
    }
    
    private static JpaContext unit(String unitName) {
        return new JpaContext(unitName);
    }
    
    private static class JpaContext {
        private final String unitName;

        private JpaContext(String unitName) {
            this.unitName = unitName;
        }
        
        private void execute(EntityManagerProcessor... processors) throws Exception {
            EntityManagerFactory factory = Persistence.createEntityManagerFactory(this.unitName);
            EntityManager em = factory.createEntityManager();
            
            try {
                EntityTransaction tx = em.getTransaction();
                
                for (EntityManagerProcessor processor : processors) {
                    try {
                        tx.begin();
                        processor.process(em);
                        tx.commit();
                    } finally {
                        if (tx.isActive()) {
                            tx.rollback();
                        }
                    }
                }
            } finally {
                em.close();
                factory.close();
            }
        }
    }
    
    @FunctionalInterface
    private interface EntityManagerProcessor {
        void process(EntityManager em) throws Exception;
    }
    
    private static void createDatabase(String databaseName, String tableName) throws Exception {
        try (
            Connection connection = DriverManager.getConnection("jdbc:hsqldb:file:db/" + databaseName + ";hsqldb.write_delay=false", "SA", "");
            Statement st = connection.createStatement();
        ) {
            st.executeUpdate("CREATE TABLE IF NOT EXISTS " + tableName
                    + " (ID INT GENERATED BY DEFAULT AS IDENTITY (START WITH 1) PRIMARY KEY, CODE VARCHAR(256))");
        }
    }
}
