/**
 * Persona.java
 * * This class represents the data model for a single contact (Person) in the address book.
 * It's a simple Plain Old Java Object (POJO) with fields for name, surname,
 * address, phone number, and age, along with their respective getters and setters.
 */
public class Persona {

    private String nome;
    private String cognome;
    private String indirizzo;
    private String telefono;
    private int eta;

    /**
     * Default constructor.
     */
    public Persona() {
    }

    /**
     * Constructor with all fields.
     * @param nome The first name of the person.
     * @param cognome The last name of the person.
     * @param indirizzo The address of the person.
     * @param telefono The phone number of the person.
     * @param eta The age of the person.
     */
    public Persona(String nome, String cognome, String indirizzo, String telefono, int eta) {
        this.nome = nome;
        this.cognome = cognome;
        this.indirizzo = indirizzo;
        this.telefono = telefono;
        this.eta = eta;
    }

    // --- Getters and Setters ---

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCognome() {
        return cognome;
    }

    public void setCognome(String cognome) {
        this.cognome = cognome;
    }

    public String getIndirizzo() {
        return indirizzo;
    }

    public void setIndirizzo(String indirizzo) {
        this.indirizzo = indirizzo;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public int getEta() {
        return eta;
    }

    public void setEta(int eta) {
        this.eta = eta;
    }
}
