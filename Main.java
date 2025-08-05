import javax.swing.SwingUtilities;

/**
 * Main.java
 * * This is the entry point of the Rubrica application.
 * Its sole responsibility is to create and show the main window.
 * The work is scheduled on the Event Dispatch Thread (EDT) using
 * SwingUtilities.invokeLater, which is the standard and safe way
 * to start a Swing application.
 */
public class Main {

    public static void main(String[] args) {
        // Schedule a job for the event-dispatching thread:
        // creating and showing this application's GUI.
        SwingUtilities.invokeLater(() -> {
            FinestraPrincipale finestra = new FinestraPrincipale();
            finestra.setVisible(true);
        });
    }
}
