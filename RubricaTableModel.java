import javax.swing.table.AbstractTableModel;
import java.util.List;
import java.util.Vector;

/**
 * RubricaTableModel.java
 * * This class serves as the model for the JTable in the main window.
 * It extends AbstractTableModel to provide a clean way to feed data from our
 * list of Persona objects to the JTable. It also defines the column names
 * and specifies which data attribute corresponds to which column.
 */
public class RubricaTableModel extends AbstractTableModel {

    // A list to hold the contacts (Persona objects). Using Vector as suggested,
    // though ArrayList is also a common choice. Vector is thread-safe.
    private List<Persona> contatti;
    
    // The column headers for the table.
    private final String[] columnNames = {"Nome", "Cognome", "Telefono"};

    /**
     * Constructor for the table model.
     * @param contatti The list of contacts to be displayed.
     */
    public RubricaTableModel(List<Persona> contatti) {
        this.contatti = contatti;
    }

    @Override
    public int getRowCount() {
        return contatti.size();
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public String getColumnName(int column) {
        return columnNames[column];
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        // Get the person for the given row
        Persona persona = contatti.get(rowIndex);
        
        // Return the appropriate attribute based on the column index
        switch (columnIndex) {
            case 0: // "Nome" column
                return persona.getNome();
            case 1: // "Cognome" column
                return persona.getCognome();
            case 2: // "Telefono" column
                return persona.getTelefono();
            default:
                return null; // Should not happen
        }
    }

    /**
     * Returns the Persona object at a specific row.
     * This is a helper method to easily retrieve the full object for editing or deleting.
     * @param rowIndex The index of the row.
     * @return The Persona object at that row.
     */
    public Persona getPersonaAt(int rowIndex) {
        return contatti.get(rowIndex);
    }
}