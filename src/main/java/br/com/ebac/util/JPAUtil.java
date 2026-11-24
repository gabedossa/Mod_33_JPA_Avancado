package br.com.ebac.util;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

import java.util.HashMap;
import java.util.Map;

public final class JPAUtil {

    private static final String PERSISTENCE_UNIT = "ExemploJPA";
    private static EntityManagerFactory factory;

    private JPAUtil() {
    }

    /**
     * A conexão pode ser sobrescrita pelas variáveis de ambiente DB_URL, DB_USER e DB_PASSWORD,
     * sem precisar alterar o persistence.xml.
     */
    public static synchronized EntityManagerFactory getFactory() {
        if (factory == null) {
            Map<String, String> props = new HashMap<>();
            putIfPresent(props, "jakarta.persistence.jdbc.url", "DB_URL");
            putIfPresent(props, "jakarta.persistence.jdbc.user", "DB_USER");
            putIfPresent(props, "jakarta.persistence.jdbc.password", "DB_PASSWORD");
            factory = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT, props);
        }
        return factory;
    }

    public static EntityManager getEntityManager() {
        return getFactory().createEntityManager();
    }

    public static synchronized void close() {
        if (factory != null && factory.isOpen()) {
            factory.close();
        }
        factory = null;
    }

    private static void putIfPresent(Map<String, String> props, String key, String env) {
        String value = System.getenv(env);
        if (value != null && !value.isBlank()) {
            props.put(key, value);
        }
    }
}
