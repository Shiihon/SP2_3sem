package app;

import app.config.AppConfig;
import app.config.HibernateConfig;
import jakarta.persistence.EntityManagerFactory;

public class Main {
    private static EntityManagerFactory emf;

    public static void main(String[] args) {

        EntityManagerFactory emf = HibernateConfig.getEntityManagerFactory("country");
        AppConfig.startServer(emf);

    }
}