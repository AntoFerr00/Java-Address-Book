import javax.swing.*;
import java.awt.*;
import java.util.Vector;

/**
 * FinestraPrincipale.java (Corretto)
 *
 * Il metodo onModifica è stato leggermente semplificato per passare
 * solo l'oggetto persona necessario al metodo di aggiornamento del database.
 */
public class FinestraPrincipale extends JFrame {

    private GestoreDatabase gestoreDatabase;
    private Vector<Persona> contatti;
    private JTable tabellaContatti;
    private RubricaTableModel tableModel;
    private JButton nuovoButton, modificaButton, eliminaButton;

    public FinestraPrincipale() {
        setTitle("Rubrica Telefonica (Database)");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 400);
        setLocationRelativeTo(null);
        gestoreDatabase = new GestoreDatabase();
        contatti = gestoreDatabase.caricaContatti();
        initComponents();
    }

    private void initComponents() {
        JToolBar toolBar = new JToolBar();
        toolBar.setFloatable(false);
        nuovoButton = new JButton("Nuovo");
        modificaButton = new JButton("Modifica");
        eliminaButton = new JButton("Elimina");
        toolBar.add(nuovoButton);
        toolBar.add(modificaButton);
        toolBar.add(eliminaButton);
        add(toolBar, BorderLayout.NORTH);

        tableModel = new RubricaTableModel(contatti);
        tabellaContatti = new JTable(tableModel);
        tabellaContatti.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        add(new JScrollPane(tabellaContatti), BorderLayout.CENTER);

        nuovoButton.addActionListener(e -> onNuovo());
        modificaButton.addActionListener(e -> onModifica());
        eliminaButton.addActionListener(e -> onElimina());
    }

    private void onNuovo() {
        EditorPersona editor = new EditorPersona(this, null);
        Persona nuovaPersona = editor.showDialog();
        if (nuovaPersona != null) {
            gestoreDatabase.aggiungiPersona(nuovaPersona);
            aggiornaTabellaDaDB();
        }
    }

    // --- METODO MODIFICATO ---
    private void onModifica() {
        int selectedRow = tabellaContatti.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Per modificare è necessario prima selezionare una persona.", "Errore", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int modelRow = tabellaContatti.convertRowIndexToModel(selectedRow);
        Persona personaDaModificare = tableModel.getPersonaAt(modelRow);
        
        // La finestra di dialogo modifica direttamente l'oggetto 'personaDaModificare'
        EditorPersona editor = new EditorPersona(this, personaDaModificare);
        editor.showDialog(); // Non ci serve il valore di ritorno

        // Controlliamo se l'utente ha salvato (anche se qui non abbiamo modo di saperlo,
        // per sicurezza eseguiamo sempre l'aggiornamento se la finestra viene chiusa)
        // In un'app più complessa, showDialog restituirebbe un booleano.
        
        gestoreDatabase.modificaPersona(personaDaModificare); // Passiamo solo l'oggetto modificato
        aggiornaTabellaDaDB();
    }

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
                "Conferma Eliminazione", JOptionPane.YES_NO_OPTION);

        if (choice == JOptionPane.YES_OPTION) {
            gestoreDatabase.eliminaPersona(personaDaEliminare);
            aggiornaTabellaDaDB();
        }
    }

    /**
     * Metodo helper per ricaricare i dati dal database e aggiornare la JTable.
     */
    private void aggiornaTabellaDaDB() {
        contatti.clear();
        contatti.addAll(gestoreDatabase.caricaContatti());
        tableModel.fireTableDataChanged();
    }
}
