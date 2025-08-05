/**
 * Utente.java
 *
 * Rappresenta un utente dell'applicazione con un username e una password.
 * Questa è una semplice classe di "dominio" per contenere i dati.
 */
public class Utente {
    private String username;
    private String password;

    /**
     * Costruttore per creare un nuovo utente.
     * @param username Il nome utente.
     * @param password La password dell'utente.
     */
    public Utente(String username, String password) {
        this.username = username;
        this.password = password;
    }

    // --- Getters ---

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}
