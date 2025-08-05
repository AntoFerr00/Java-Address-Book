import javax.swing.*;
import java.awt.*;

/**
 * FinestraLogin.java
 *
 * Una finestra di dialogo modale che richiede all'utente di inserire username e password.
 * Blocca il resto dell'applicazione finché l'utente non effettua il login con successo
 * o chiude la finestra.
 */
public class FinestraLogin extends JDialog {

    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JButton annullaButton;
    private boolean loginRiuscito = false;

    private AuthService authService;

    public FinestraLogin(Frame owner) {
        super(owner, "Login", true); // true per renderla modale
        this.authService = new AuthService();

        setLayout(new BorderLayout(10, 10));
        initComponents();
        pack(); // Adatta la dimensione della finestra ai componenti
        setLocationRelativeTo(owner); // Centra la finestra rispetto al proprietario
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
    }

    private void initComponents() {
        // --- Pannello del Form ---
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(new JLabel("Utente:"), gbc);

        gbc.gridx = 1;
        usernameField = new JTextField(15);
        panel.add(usernameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(new JLabel("Password:"), gbc);

        gbc.gridx = 1;
        passwordField = new JPasswordField(15);
        panel.add(passwordField, gbc);

        add(panel, BorderLayout.CENTER);

        // --- Pannello dei Bottoni ---
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        loginButton = new JButton("LOGIN");
        annullaButton = new JButton("Annulla");
        buttonPanel.add(loginButton);
        buttonPanel.add(annullaButton);

        add(buttonPanel, BorderLayout.SOUTH);

        // --- Action Listeners ---
        loginButton.addActionListener(e -> onLogin());
        annullaButton.addActionListener(e -> dispose());
        // Permette di premere Invio nel campo password per avviare il login
        passwordField.addActionListener(e -> onLogin());
    }

    private void onLogin() {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());

        if (authService.autentica(username, password)) {
            loginRiuscito = true;
            dispose(); // Chiude la finestra di login
        } else {
            JOptionPane.showMessageDialog(this,
                    "Username o password errati.",
                    "Login Errato",
                    JOptionPane.ERROR_MESSAGE);
            // Pulisce i campi per un nuovo tentativo
            passwordField.setText("");
            usernameField.requestFocusInWindow();
        }
    }

    /**
     * Mostra la finestra di dialogo e restituisce l'esito del login.
     * @return true se il login ha avuto successo, altrimenti false.
     */
    public boolean showDialog() {
        setVisible(true);
        return loginRiuscito;
    }
}
