import javax.swing.*;
import java.awt.*;
import java.util.Vector;

/**
 * FinestraPrincipale.java
 *
 * This is the main window of the application. It contains the JTable for
 * displaying contacts and the buttons for adding, editing, and deleting them.
 *
 * It now delegates all file loading and saving operations to the GestorePersistenza class.
 */
public class FinestraPrincipale extends JFrame {

    // The persistence manager handles all file I/O
    private GestorePersistenza gestorePersistenza;

    // The list of contacts. Vector is used as suggested in the project description.
    private Vector<Persona> contatti;
    
    // The JTable and its model
    private JTable tabellaContatti;
    private RubricaTableModel tableModel;

    // Buttons
    private JButton nuovoButton;
    private JButton modificaButton;
    private JButton eliminaButton;

    /**
     * Constructor for the main window.
     */
    public FinestraPrincipale() {
        setTitle("Rubrica Telefonica");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 400);
        setLocationRelativeTo(null); // Center the window on the screen

        // Initialize the persistence manager
        gestorePersistenza = new GestorePersistenza();

        // Load contacts using the persistence manager
        contatti = gestorePersistenza.caricaContatti();

        // Initialize GUI components
        initComponents();
    }

    /**
     * Initializes all GUI components and sets up the layout.
     */
    private void initComponents() {
        // --- Table Panel ---
        tableModel = new RubricaTableModel(contatti);
        tabellaContatti = new JTable(tableModel);
        tabellaContatti.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JScrollPane scrollPane = new JScrollPane(tabellaContatti);
        add(scrollPane, BorderLayout.CENTER);

        // --- Button Panel ---
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        nuovoButton = new JButton("Nuovo");
        modificaButton = new JButton("Modifica");
        eliminaButton = new JButton("Elimina");

        buttonPanel.add(nuovoButton);
        buttonPanel.add(modificaButton);
        buttonPanel.add(eliminaButton);

        add(buttonPanel, BorderLayout.SOUTH);

        // --- Action Listeners for Buttons ---
        nuovoButton.addActionListener(e -> onNuovo());
        modificaButton.addActionListener(e -> onModifica());
        eliminaButton.addActionListener(e -> onElimina());
    }

    /**
     * Handles the "Nuovo" button click.
     */
    private void onNuovo() {
        EditorPersona editor = new EditorPersona(this, null);
        Persona nuovaPersona = editor.showDialog();

        if (nuovaPersona != null) {
            contatti.add(nuovaPersona);
            tableModel.fireTableDataChanged();
            // Use the persistence manager to save
            gestorePersistenza.salvaContatti(contatti);
        }
    }

    /**
     * Handles the "Modifica" button click.
     */
    private void onModifica() {
        int selectedRow = tabellaContatti.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Per modificare è necessario prima selezionare una persona.", "Errore", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Convert view index to model index in case of sorting/filtering
        int modelRow = tabellaContatti.convertRowIndexToModel(selectedRow);
        Persona personaDaModificare = tableModel.getPersonaAt(modelRow);
        
        EditorPersona editor = new EditorPersona(this, personaDaModificare);
        Persona personaModificata = editor.showDialog();

        if (personaModificata != null) {
            tableModel.fireTableRowsUpdated(modelRow, modelRow);
            // Use the persistence manager to save
            gestorePersistenza.salvaContatti(contatti);
        }
    }

    /**
     * Handles the "Elimina" button click.
     */
    private void onElimina() {
        int selectedRow = tabellaContatti.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Per eliminare è necessario prima selezionare una persona.", "Errore", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Convert view index to model index
        int modelRow = tabellaContatti.convertRowIndexToModel(selectedRow);
        Persona personaDaEliminare = tableModel.getPersonaAt(modelRow);
        
        int choice = JOptionPane.showConfirmDialog(this,
                "Eliminare la persona " + personaDaEliminare.getNome() + " " + personaDaEliminare.getCognome() + "?",
                "Conferma Eliminazione",
                JOptionPane.YES_NO_OPTION);

        if (choice == JOptionPane.YES_OPTION) {
            contatti.remove(personaDaEliminare);
            tableModel.fireTableDataChanged();
            // Use the persistence manager to save
            gestorePersistenza.salvaContatti(contatti);
        }
    }
}
