/**
 * AuthService.java
 *
 * Gestisce la logica di business per l'autenticazione dell'utente.
 * In questo esempio, utilizziamo un utente predefinito per semplicità.
 */
public class AuthService {

    // Un utente predefinito a scopo dimostrativo.
    private final Utente utenteValido = new Utente("admin", "password");

    /**
     * Autentica un utente in base a username e password forniti.
     * @param username L'username da controllare.
     * @param password La password da controllare.
     * @return true se le credenziali sono valide, altrimenti false.
     */
    public boolean autentica(String username, String password) {
        if (username == null || password == null) {
            return false;
        }
        // Semplice confronto di stringhe per la demo.
        return utenteValido.getUsername().equals(username) && utenteValido.getPassword().equals(password);
    }
}
