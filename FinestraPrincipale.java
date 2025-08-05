import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.Scanner;
import java.util.Vector;

/**
 * FinestraPrincipale.java
 *
 * This is the main window of the application. It contains the JTable for
 * displaying contacts and the buttons for adding, editing, and deleting them.
 * It also handles the logic for loading contacts from a file on startup and
 * saving them back to the file whenever a change is made.
 */
public class FinestraPrincipale extends JFrame {

    private static final String FILE_NAME = "informazioni.txt";

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

        // Load contacts from file before initializing components
        caricaContatti();

        // Initialize GUI components
        initComponents();
    }

    /**
     * Initializes all GUI components and sets up the layout.
     */
    private void initComponents() {
        // --- Table Panel ---
        // The table model is created with our list of contacts
        tableModel = new RubricaTableModel(contatti);
        tabellaContatti = new JTable(tableModel);
        tabellaContatti.setSelectionMode(ListSelectionModel.SINGLE_SELECTION); // Allow only one row to be selected at a time

        // Add the table to a JScrollPane to make it scrollable
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
     * Opens the editor dialog for a new person.
     */
    private void onNuovo() {
        EditorPersona editor = new EditorPersona(this, null);
        Persona nuovaPersona = editor.showDialog();

        if (nuovaPersona != null) {
            contatti.add(nuovaPersona);
            tableModel.fireTableDataChanged(); // Notify the table model that data has changed
            salvaContatti(); // Save changes to file
        }
    }

    /**
     * Handles the "Modifica" button click.
     * Opens the editor dialog for the selected person.
     */
    private void onModifica() {
        int selectedRow = tabellaContatti.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Per modificare è necessario prima selezionare una persona.", "Errore", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Persona personaDaModificare = tableModel.getPersonaAt(selectedRow);
        EditorPersona editor = new EditorPersona(this, personaDaModificare);
        Persona personaModificata = editor.showDialog();

        if (personaModificata != null) {
            // The persona object is modified by reference in the editor,
            // so we just need to update the table and save.
            tableModel.fireTableRowsUpdated(selectedRow, selectedRow);
            salvaContatti();
        }
    }

    /**
     * Handles the "Elimina" button click.
     * Deletes the selected person after confirmation.
     */
    private void onElimina() {
        int selectedRow = tabellaContatti.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Per eliminare è necessario prima selezionare una persona.", "Errore", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Persona personaDaEliminare = tableModel.getPersonaAt(selectedRow);
        int choice = JOptionPane.showConfirmDialog(this,
                "Eliminare la persona " + personaDaEliminare.getNome() + " " + personaDaEliminare.getCognome() + "?",
                "Conferma Eliminazione",
                JOptionPane.YES_NO_OPTION);

        if (choice == JOptionPane.YES_OPTION) {
            contatti.remove(personaDaEliminare);
            tableModel.fireTableDataChanged();
            salvaContatti();
        }
    }

    /**
     * Loads the list of contacts from the "informazioni.txt" file.
     * The file format is: nome;cognome;indirizzo;telefono;eta
     */
    private void caricaContatti() {
        contatti = new Vector<>();
        File file = new File(FILE_NAME);

        // If the file doesn't exist, just start with an empty list. No error needed.
        if (!file.exists()) {
            return;
        }

        try (Scanner scanner = new Scanner(file)) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] parts = line.split(";");
                if (parts.length == 5) {
                    try {
                        String nome = parts[0];
                        String cognome = parts[1];
                        String indirizzo = parts[2];
                        String telefono = parts[3];
                        int eta = Integer.parseInt(parts[4]);
                        contatti.add(new Persona(nome, cognome, indirizzo, telefono, eta));
                    } catch (NumberFormatException e) {
                        System.err.println("Skipping malformed line (invalid age): " + line);
                    }
                } else {
                    System.err.println("Skipping malformed line (incorrect number of fields): " + line);
                }
            }
        } catch (FileNotFoundException e) {
            // This should not happen because we check file.exists(), but it's good practice.
            JOptionPane.showMessageDialog(this, "File non trovato: " + FILE_NAME, "Errore di Caricamento", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Saves the entire list of contacts to the "informazioni.txt" file.
     * This method overwrites the file completely with the current data.
     */
    private void salvaContatti() {
        try (PrintStream ps = new PrintStream(FILE_NAME)) {
            for (Persona p : contatti) {
                ps.println(p.getNome() + ";" +
                           p.getCognome() + ";" +
                           p.getIndirizzo() + ";" +
                           p.getTelefono() + ";" +
                           p.getEta());
            }
        } catch (FileNotFoundException e) {
            JOptionPane.showMessageDialog(this, "Impossibile salvare il file: " + FILE_NAME, "Errore di Salvataggio", JOptionPane.ERROR_MESSAGE);
        }
    }
}
