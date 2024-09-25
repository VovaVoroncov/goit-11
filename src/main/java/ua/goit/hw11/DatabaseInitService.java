package ua.goit.hw11;

import org.flywaydb.core.Flyway;
import java.io.IOException;
import java.util.Properties;

public class DatabaseInitService {
    private static String databaseUrl = "";

    public String getProperties() {
        Properties properties = new Properties();
        try {
            ClassLoader classLoader = getClass().getClassLoader();
            properties.load(classLoader.getResourceAsStream("hibernate.properties"));
            databaseUrl = properties.getProperty("hibernate.connection.url");
        } catch (IOException e) {
            throw new RuntimeException("Failed to load database.", e);
        }
        return databaseUrl;
    }

    public void initDB() {
        String hibernatePath = getProperties();

        Flyway flyway = Flyway.configure()
                .dataSource(hibernatePath, null, null)
                .load();

        flyway.migrate();
    }

}
