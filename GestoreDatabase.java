import java.sql.*;
import java.util.Vector;

/**
 * GestoreDatabase.java
 *
 * Implements the persistence logic using a MySQL database via JDBC.
 * This class replaces GestorePersistenza for database-enabled versions.
 */
public class GestoreDatabase {

    public Vector<Persona> caricaContatti() {
        Vector<Persona> contatti = new Vector<>();
        String sql = "SELECT id, nome, cognome, indirizzo, telefono, eta FROM persone";

        try (Connection conn = DatabaseManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Persona p = new Persona();
                // Note: We are not storing the DB 'id' in the Persona object for this implementation,
                // but it's good practice to have it for update/delete operations.
                p.setNome(rs.getString("nome"));
                p.setCognome(rs.getString("cognome"));
                p.setIndirizzo(rs.getString("indirizzo"));
                p.setTelefono(rs.getString("telefono"));
                p.setEta(rs.getInt("eta"));
                contatti.add(p);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // In a real app, show a user-friendly error dialog.
        }
        return contatti;
    }

    public void aggiungiPersona(Persona p) {
        String sql = "INSERT INTO persone (nome, cognome, indirizzo, telefono, eta) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, p.getNome());
            pstmt.setString(2, p.getCognome());
            pstmt.setString(3, p.getIndirizzo());
            pstmt.setString(4, p.getTelefono());
            pstmt.setInt(5, p.getEta());
            pstmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void modificaPersona(Persona personaOriginale, Persona personaModificata) {
        // To update a record, we need a unique identifier. Name+Surname is not ideal
        // but will work for this example. A database ID would be better.
        String sql = "UPDATE persone SET nome=?, cognome=?, indirizzo=?, telefono=?, eta=? WHERE nome=? AND cognome=?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, personaModificata.getNome());
            pstmt.setString(2, personaModificata.getCognome());
            pstmt.setString(3, personaModificata.getIndirizzo());
            pstmt.setString(4, personaModificata.getTelefono());
            pstmt.setInt(5, personaModificata.getEta());
            // WHERE clause
            pstmt.setString(6, personaOriginale.getNome());
            pstmt.setString(7, personaOriginale.getCognome());
            
            pstmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void eliminaPersona(Persona p) {
        // Again, using name+surname to identify the record to delete.
        String sql = "DELETE FROM persone WHERE nome=? AND cognome=?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, p.getNome());
            pstmt.setString(2, p.getCognome());
            pstmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
