import javax.swing.*;
import java.awt.*;

/**
 * EditorPersona.java
 * * This class defines the dialog window for adding a new person or editing an
 * existing one. It contains a form with fields for all of a person's attributes.
 * It is a modal JDialog, meaning it blocks interaction with the main window
 * until it is closed.
 */
public class EditorPersona extends JDialog {

    // GUI Components
    private JTextField nomeField;
    private JTextField cognomeField;
    private JTextField indirizzoField;
    private JTextField telefonoField;
    private JTextField etaField;
    private JButton salvaButton;
    private JButton annullaButton;

    // The person object being edited. If it's a new person, this will be populated
    // with the data from the fields. If it's an existing person, the fields will be
    // populated from this object.
    private Persona persona;

    // A flag to indicate if the dialog was closed by clicking "Salva".
    private boolean salvato = false;

    /**
     * Constructor for the editor window.
     * @param owner The parent frame (the main window).
     * @param persona The person to be edited. If this is null, the dialog will be for creating a new person.
     */
    public EditorPersona(Frame owner, Persona persona) {
        super(owner, true); // true for modal dialog
        this.persona = persona;

        setTitle(persona == null ? "Nuova Persona" : "Modifica Persona");
        initComponents();
        
        // If we are editing an existing person, populate the fields with their data.
        if (persona != null) {
            nomeField.setText(persona.getNome());
            cognomeField.setText(persona.getCognome());
            indirizzoField.setText(persona.getIndirizzo());
            telefonoField.setText(persona.getTelefono());
            etaField.setText(String.valueOf(persona.getEta()));
        }

        pack(); // Adjust window size to fit components
        setLocationRelativeTo(owner); // Center the dialog relative to the parent
    }

    /**
     * Initializes all the GUI components and sets up the layout.
     */
    private void initComponents() {
        setLayout(new BorderLayout(10, 10));

        // --- Form Panel ---
        // A panel with a grid layout to hold the labels and text fields.
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        // Row 0: Nome
        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(new JLabel("Nome:"), gbc);
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        nomeField = new JTextField(20);
        formPanel.add(nomeField, gbc);

        // Row 1: Cognome
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        formPanel.add(new JLabel("Cognome:"), gbc);
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        cognomeField = new JTextField(20);
        formPanel.add(cognomeField, gbc);

        // Row 2: Indirizzo
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        formPanel.add(new JLabel("Indirizzo:"), gbc);
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        indirizzoField = new JTextField(20);
        formPanel.add(indirizzoField, gbc);

        // Row 3: Telefono
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        formPanel.add(new JLabel("Telefono:"), gbc);
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        telefonoField = new JTextField(20);
        formPanel.add(telefonoField, gbc);

        // Row 4: Età
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        formPanel.add(new JLabel("Età:"), gbc);
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        etaField = new JTextField(20);
        formPanel.add(etaField, gbc);

        add(formPanel, BorderLayout.CENTER);

        // --- Button Panel ---
        // A panel for the Salva and Annulla buttons.
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        salvaButton = new JButton("Salva");
        annullaButton = new JButton("Annulla");
        buttonPanel.add(salvaButton);
        buttonPanel.add(annullaButton);
        add(buttonPanel, BorderLayout.SOUTH);

        // --- Action Listeners ---
        salvaButton.addActionListener(e -> onSalva());
        annullaButton.addActionListener(e -> onAnnulla());
    }

    /**
     * Action performed when the "Salva" button is clicked.
     */
    private void onSalva() {
        // Basic validation: check for empty fields and valid age.
        if (nomeField.getText().trim().isEmpty() || cognomeField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Nome e Cognome non possono essere vuoti.", "Errore di Validazione", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int eta;
        try {
            eta = Integer.parseInt(etaField.getText().trim());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "L'età deve essere un numero valido.", "Errore di Validazione", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // If it was a new person, create a new Persona object.
        if (this.persona == null) {
            this.persona = new Persona();
        }

        // Update the persona object with data from the fields.
        persona.setNome(nomeField.getText().trim());
        persona.setCognome(cognomeField.getText().trim());
        persona.setIndirizzo(indirizzoField.getText().trim());
        persona.setTelefono(telefonoField.getText().trim());
        persona.setEta(eta);

        // Set the flag and close the dialog.
        this.salvato = true;
        setVisible(false);
    }

    /**
     * Action performed when the "Annulla" button is clicked.
     */
    private void onAnnulla() {
        this.salvato = false;
        setVisible(false);
    }

    /**
     * Public method to show the dialog and get the result.
     * @return The edited/created Persona object if saved, otherwise null.
     */
    public Persona showDialog() {
        setVisible(true);
        if (salvato) {
            return persona;
        }
        return null;
    }
}