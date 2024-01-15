package ua.foxmided.foxstudent103852.schoolapp.util;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import ua.foxmided.foxstudent103852.schoolapp.exception.DataProcessingException;

public class ConnectionUtil {
    private static Properties properties;

    private ConnectionUtil() {
        throw new DataProcessingException("Utility class");
    }

    static {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            throw new DataProcessingException("PostgreSQL JDBC Driver is not found."
                    + " Include it in your library path", e);
        }

        try (InputStream inputStream = ConnectionUtil.class.getClassLoader()
                .getResourceAsStream("db.properties")) {
            properties = new Properties();
            properties.load(inputStream);
        } catch (IOException e) {
            throw new DataProcessingException("Can not get file db.properties", e);
        }
    }

    public static Connection getConnection() {
        try {
            return DriverManager.getConnection(properties.getProperty("url"), properties);
        } catch (SQLException e) {
            System.out.println("Unable to connect to database '" + properties.getProperty("database") + "'. "
                    + e.getMessage());
            throw new DataProcessingException(
                    "Unable to connect to database '" + properties.getProperty("database") + "': ", e);
        }
    }
}
