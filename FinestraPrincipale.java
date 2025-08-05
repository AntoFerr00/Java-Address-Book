import javax.swing.*;
import java.awt.*;
import java.util.Vector;

/**
 * FinestraPrincipale.java (Versione Ibrida)
 *
 * Questa versione utilizza il GestorePersistenzaIbrido per salvare i dati
 * dei contatti sia su database che su file system contemporaneamente.
 */
public class FinestraPrincipale extends JFrame {

    // Il gestore ibrido che coordina il salvataggio su DB e file.
    private GestorePersistenzaIbrido gestorePersistenza;

    private Vector<Persona> contatti;
    private JTable tabellaContatti;
    private RubricaTableModel tableModel;
    private JButton nuovoButton, modificaButton, eliminaButton;

    public FinestraPrincipale() {
        setTitle("Rubrica Telefonica (Ibrida: DB + File)");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 400);
        setLocationRelativeTo(null);

        // Inizializza il nuovo gestore ibrido
        gestorePersistenza = new GestorePersistenzaIbrido();

        // Carica i contatti (il gestore ibrido li prenderà dal database)
        contatti = gestorePersistenza.caricaContatti();

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
            gestorePersistenza.aggiungiPersona(nuovaPersona);
            aggiornaTabellaDaDB();
        }
    }

    private void onModifica() {
        int selectedRow = tabellaContatti.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Per modificare è necessario prima selezionare una persona.", "Errore", JOptionPane.ERROR_MESSAGE);
            return;
        }
        int modelRow = tabellaContatti.convertRowIndexToModel(selectedRow);
        Persona personaDaModificare = tableModel.getPersonaAt(modelRow);
        
        EditorPersona editor = new EditorPersona(this, personaDaModificare);
        editor.showDialog();
        
        gestorePersistenza.modificaPersona(personaDaModificare);
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
            gestorePersistenza.eliminaPersona(personaDaEliminare);
            aggiornaTabellaDaDB();
        }
    }

    /**
     * Metodo helper per ricaricare i dati dal database e aggiornare la JTable.
     */
    private void aggiornaTabellaDaDB() {
        // Chiediamo al gestore ibrido di ricaricare i dati (li prenderà dal DB)
        Vector<Persona> contattiAggiornati = gestorePersistenza.caricaContatti();
        contatti.clear();
        contatti.addAll(contattiAggiornati);
        tableModel.fireTableDataChanged();
    }
}
