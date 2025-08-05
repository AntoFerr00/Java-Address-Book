import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.Scanner;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * GestorePersistenza.java
 *
 * This class handles the logic for saving and loading contacts.
 * EVOLUTION 1: Instead of a single file, this implementation saves each contact
 * in a separate file inside a dedicated directory ("informazioni").
 */
public class GestorePersistenza {

    private static final String DIRECTORY_NAME = "informazioni";

    /**
     * Loads contacts from individual files within the "informazioni" directory.
     * @return A Vector of Persona objects loaded from the files.
     */
    public Vector<Persona> caricaContatti() {
        Vector<Persona> contatti = new Vector<>();
        File directory = new File(DIRECTORY_NAME);

        // If the directory doesn't exist or isn't a directory, return an empty list.
        if (!directory.exists() || !directory.isDirectory()) {
            System.out.println("La cartella '" + DIRECTORY_NAME + "' non esiste. Verrà creata al primo salvataggio.");
            return contatti;
        }

        // List all files in the directory.
        File[] files = directory.listFiles();
        if (files == null) {
            return contatti; // Should not happen unless there's a permission issue.
        }

        // Process each file.
        for (File file : files) {
            // We only care about files, not subdirectories.
            if (file.isFile()) {
                try (Scanner scanner = new Scanner(file)) {
                    if (scanner.hasNextLine()) {
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
                                System.err.println("Skipping malformed data in file " + file.getName() + " (invalid age)");
                            }
                        } else {
                            System.err.println("Skipping malformed data in file " + file.getName());
                        }
                    }
                } catch (FileNotFoundException e) {
                    // This is unlikely to happen within this loop, but good practice.
                    System.err.println("Error reading file: " + file.getName());
                }
            }
        }
        return contatti;
    }

    /**
     * Saves the entire list of contacts to the "informazioni" directory.
     * It first clears the directory of old contact files and then writes a new file for each contact.
     * @param contatti The list of contacts to save.
     */
    public void salvaContatti(Vector<Persona> contatti) {
        File directory = new File(DIRECTORY_NAME);

        // 1. Create the directory if it doesn't exist.
        if (!directory.exists()) {
            directory.mkdir();
        }

        // 2. Clear the directory of old .txt files to handle deletions.
        File[] oldFiles = directory.listFiles((dir, name) -> name.toLowerCase().endsWith(".txt"));
        if (oldFiles != null) {
            for (File file : oldFiles) {
                file.delete();
            }
        }

        // 3. Write a new file for each contact.
        for (Persona p : contatti) {
            // Create a safe filename from the person's name and surname.
            String baseFilename = sanitizeFilename(p.getNome() + "-" + p.getCognome());
            String filename = baseFilename + ".txt";
            File file = new File(directory, filename);

            // Handle potential name collisions (e.g., two "Mario Rossi")
            int counter = 1;
            while(file.exists()) {
                filename = baseFilename + "-" + counter + ".txt";
                file = new File(directory, filename);
                counter++;
            }

            // Write the contact's data to their file.
            try (PrintStream ps = new PrintStream(file)) {
                ps.println(p.getNome() + ";" +
                           p.getCognome() + ";" +
                           p.getIndirizzo() + ";" +
                           p.getTelefono() + ";" +
                           p.getEta());
            } catch (FileNotFoundException e) {
                System.err.println("Failed to save contact: " + p.getNome() + " " + p.getCognome());
                e.printStackTrace();
            }
        }
    }

    /**
     * Removes characters that are invalid for file names.
     * @param input The original string for the filename.
     * @return A sanitized string safe for use as a filename.
     */
    private String sanitizeFilename(String input) {
        // Replace invalid characters with an underscore.
        return input.replaceAll("[^a-zA-Z0-9.-]", "_");
    }
}
