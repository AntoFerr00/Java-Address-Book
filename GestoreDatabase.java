import java.sql.*;
import java.util.Vector;

/**
 * GestoreDatabase.java (Corretto)
 *
 * Le query di modifica ed eliminazione ora usano l'ID univoco della persona,
 * risolvendo il bug dell'aggiornamento.
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
                p.setId(rs.getInt("id")); // <-- LEGGIAMO L'ID
                p.setNome(rs.getString("nome"));
                p.setCognome(rs.getString("cognome"));
                p.setIndirizzo(rs.getString("indirizzo"));
                p.setTelefono(rs.getString("telefono"));
                p.setEta(rs.getInt("eta"));
                contatti.add(p);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return contatti;
    }

    public void aggiungiPersona(Persona p) {
        // La query di inserimento non cambia
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

    // --- METODO MODIFICATO ---
    public void modificaPersona(Persona persona) {
        String sql = "UPDATE persone SET nome=?, cognome=?, indirizzo=?, telefono=?, eta=? WHERE id=?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, persona.getNome());
            pstmt.setString(2, persona.getCognome());
            pstmt.setString(3, persona.getIndirizzo());
            pstmt.setString(4, persona.getTelefono());
            pstmt.setInt(5, persona.getEta());
            pstmt.setInt(6, persona.getId()); // <-- USA L'ID NELLA CLAUSOLA WHERE
            
            pstmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // --- METODO MODIFICATO ---
    public void eliminaPersona(Persona p) {
        String sql = "DELETE FROM persone WHERE id=?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, p.getId()); // <-- USA L'ID NELLA CLAUSOLA WHERE
            pstmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
