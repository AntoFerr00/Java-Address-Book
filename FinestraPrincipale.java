import javax.swing.*;
import java.awt.*;
import java.util.Vector;

/**
 * FinestraPrincipale.java (File Version with Extras)
 *
 * This version uses local file storage, saving each contact to a separate file
 * in the 'informazioni' directory. It includes the JToolbar and is designed to
 * be shown after a successful login. It does NOT require a database.
 */
public class FinestraPrincipale extends JFrame {

    // The persistence manager handles all file I/O
    private GestorePersistenza gestorePersistenza;

    // The list of contacts.
    private Vector<Persona> contatti;
    
    // The JTable and its model
    private JTable tabellaContatti;
    private RubricaTableModel tableModel;

    // Buttons in the toolbar
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
        setLocationRelativeTo(null);

        // Initialize the FILE-BASED persistence manager
        gestorePersistenza = new GestorePersistenza();

        // Load contacts from files at startup
        contatti = gestorePersistenza.caricaContatti();

        // Initialize GUI components
        initComponents();
    }

    /**
     * Initializes all GUI components and sets up the layout.
     */
    private void initComponents() {
        // --- Toolbar ---
        JToolBar toolBar = new JToolBar();
        toolBar.setFloatable(false);

        nuovoButton = new JButton("Nuovo");
        modificaButton = new JButton("Modifica");
        eliminaButton = new JButton("Elimina");
        
        nuovoButton.setToolTipText("Crea un nuovo contatto");
        modificaButton.setToolTipText("Modifica il contatto selezionato");
        eliminaButton.setToolTipText("Elimina il contatto selezionato");

        toolBar.add(nuovoButton);
        toolBar.add(modificaButton);
        toolBar.add(eliminaButton);

        add(toolBar, BorderLayout.NORTH);

        // --- Table Panel ---
        tableModel = new RubricaTableModel(contatti);
        tabellaContatti = new JTable(tableModel);
        tabellaContatti.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollPane = new JScrollPane(tabellaContatti);
        add(scrollPane, BorderLayout.CENTER);

        // --- Action Listeners for Buttons ---
        nuovoButton.addActionListener(e -> onNuovo());
        modificaButton.addActionListener(e -> onModifica());
        eliminaButton.addActionListener(e -> onElimina());
    }

    /**
     * Handles creating a new contact.
     */
    private void onNuovo() {
        EditorPersona editor = new EditorPersona(this, null);
        Persona nuovaPersona = editor.showDialog();

        if (nuovaPersona != null) {
            contatti.add(nuovaPersona);
            tableModel.fireTableDataChanged();
            // Save all contacts back to their files
            gestorePersistenza.salvaContatti(contatti);
        }
    }

    /**
     * Handles modifying an existing contact.
     */
    private void onModifica() {
        int selectedRow = tabellaContatti.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Per modificare è necessario prima selezionare una persona.", "Errore", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int modelRow = tabellaContatti.convertRowIndexToModel(selectedRow);
        Persona personaDaModificare = tableModel.getPersonaAt(modelRow);
        
        EditorPersona editor = new EditorPersona(this, personaDaModificare);
        Persona personaModificata = editor.showDialog();

        if (personaModificata != null) {
            tableModel.fireTableRowsUpdated(modelRow, modelRow);
            // Save all contacts back to their files
            gestorePersistenza.salvaContatti(contatti);
        }
    }

    /**
     * Handles deleting a contact.
     */
    private void onElimina() {
        int selectedRow = tabellaContatti.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Per eliminare è necessario prima selezionare una persona.", "Errore", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int modelRow = tabellaContatti.convertRowIndexToModel(selectedRow);
        Persona personaDaEliminare = tableModel.getPersonaAt(modelRow);
        
        int choice = JOptionPane.showConfirmDialog(this,
                "Eliminare la persona " + personaDaEliminare.getNome() + " " + personaDaEliminare.getCognome() + "?",
                "Conferma Eliminazione",
                JOptionPane.YES_NO_OPTION);

        if (choice == JOptionPane.YES_OPTION) {
            contatti.remove(personaDaEliminare);
            tableModel.fireTableDataChanged();
            // Save all contacts back to their files
            gestorePersistenza.salvaContatti(contatti);
        }
    }
}
