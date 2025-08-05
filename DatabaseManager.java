import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * DatabaseManager.java
 *
 * Manages the database connection. It reads connection details from the
 * credenziali_database.properties file and provides a method to get a
 * new connection to the database.
 */
public class DatabaseManager {

    private static Properties props = new Properties();

    static {
        try (FileInputStream fis = new FileInputStream("credenziali_database.properties")) {
            props.load(fis);
            // Register the MySQL driver
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (IOException e) {
            System.err.println("ERRORE: File 'credenziali_database.properties' non trovato o illeggibile.");
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            System.err.println("ERRORE: Driver MySQL (Connector/J) non trovato. Assicurarsi che il JAR sia nel build path.");
            e.printStackTrace();
        }
    }

    /**
     * Establishes and returns a connection to the database.
     * @return A Connection object to the database.
     * @throws SQLException if a database access error occurs.
     */
    public static Connection getConnection() throws SQLException {
        String url = "jdbc:mysql://" + props.getProperty("db.ip") + ":" + props.getProperty("db.port") + "/" + props.getProperty("db.name");
        return DriverManager.getConnection(url, props.getProperty("db.user"), props.getProperty("db.password"));
    }
}
