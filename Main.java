import javax.swing.SwingUtilities;

/**
 * Main.java
 *
 * Questo è il punto di ingresso dell'applicazione Rubrica.
 *
 * Ora mostra per prima una finestra di login. Se il login ha successo,
 * procede a creare e mostrare la finestra principale dell'applicazione.
 * Altrimenti, l'applicazione termina.
 */
public class Main {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            // 1. Crea e mostra la finestra di login
            FinestraLogin loginDialog = new FinestraLogin(null);
            boolean loginSuccess = loginDialog.showDialog();

            // 2. Se il login ha avuto successo, mostra la finestra principale
            if (loginSuccess) {
                FinestraPrincipale finestra = new FinestraPrincipale();
                finestra.setVisible(true);
            } else {
                // Opzionale: puoi aggiungere un messaggio qui prima di uscire
                System.out.println("Login annullato o fallito. Uscita dall'applicazione.");
                System.exit(0);
            }
        });
    }
}
