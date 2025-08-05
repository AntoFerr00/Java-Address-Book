import java.util.Vector;

/**
 * GestorePersistenzaIbrido.java
 *
 * Questa classe agisce come un coordinatore per un sistema di persistenza ibrido.
 * Ogni operazione di scrittura (aggiunta, modifica, eliminazione) viene eseguita
 * sia sul database MySQL che sul file system locale.
 * La lettura dei dati (all'avvio) considera il database come fonte primaria di verità.
 */
public class GestorePersistenzaIbrido {

    private GestoreDatabase gestoreDB;
    private GestorePersistenza gestoreFile;

    /**
     * Costruttore che inizializza entrambi i gestori di persistenza.
     */
    public GestorePersistenzaIbrido() {
        this.gestoreDB = new GestoreDatabase();
        this.gestoreFile = new GestorePersistenza();
    }

    /**
     * Carica i contatti. La fonte di verità primaria è il database.
     * @return Un Vector di oggetti Persona.
     */
    public Vector<Persona> caricaContatti() {
        // Carichiamo sempre dal database, che è considerato la fonte più affidabile.
        return gestoreDB.caricaContatti();
    }

    /**
     * Aggiunge una persona a entrambi i sistemi di persistenza.
     * @param p La persona da aggiungere.
     */
    public void aggiungiPersona(Persona p) {
        // 1. Aggiungi al database
        gestoreDB.aggiungiPersona(p);
        
        // 2. Aggiorna i file locali
        // Per semplicità, ricarichiamo tutti i contatti dal DB e li salviamo su file
        // per garantire la coerenza (inclusi gli ID corretti).
        Vector<Persona> tuttiContatti = gestoreDB.caricaContatti();
        gestoreFile.salvaContatti(tuttiContatti);
    }

    /**
     * Modifica una persona in entrambi i sistemi di persistenza.
     * @param p L'oggetto Persona con i dati aggiornati e l'ID originale.
     */
    public void modificaPersona(Persona p) {
        // 1. Modifica nel database
        gestoreDB.modificaPersona(p);

        // 2. Aggiorna i file locali
        Vector<Persona> tuttiContatti = gestoreDB.caricaContatti();
        gestoreFile.salvaContatti(tuttiContatti);
    }

    /**
     * Elimina una persona da entrambi i sistemi di persistenza.
     * @param p La persona da eliminare.
     */
    public void eliminaPersona(Persona p) {
        // 1. Elimina dal database
        gestoreDB.eliminaPersona(p);

        // 2. Aggiorna i file locali
        Vector<Persona> tuttiContatti = gestoreDB.caricaContatti();
        gestoreFile.salvaContatti(tuttiContatti);
    }
}
