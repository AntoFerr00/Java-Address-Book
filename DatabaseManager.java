import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * DatabaseManager.java
 *
 * Gestisce la connessione al database. Legge i dettagli della connessione
 * dal file credenziali_database.properties e fornisce un metodo per ottenere
 * una nuova connessione al database.
 */
public class DatabaseManager {

    private static Properties props = new Properties();

    static {
        try (FileInputStream fis = new FileInputStream("credenziali_database.properties")) {
            props.load(fis);
            // Registra il driver MySQL
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
     * Stabilisce e restituisce una connessione al database.
     * @return Un oggetto Connection al database.
     * @throws SQLException se si verifica un errore di accesso al database.
     */
    public static Connection getConnection() throws SQLException {
        String url = "jdbc:mysql://" + props.getProperty("db.ip") + ":" + props.getProperty("db.port") + "/" + props.getProperty("db.name");
        return DriverManager.getConnection(url, props.getProperty("db.user"), props.getProperty("db.password"));
    }
}
