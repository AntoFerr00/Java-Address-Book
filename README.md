# Rubrica Telefonica Avanzata in Java

Questo progetto è un'applicazione desktop per la gestione di una rubrica telefonica, realizzata in Java con l'interfaccia grafica Swing. È stata sviluppata come esercizio completo che include la gestione dei dati, un'interfaccia utente e diverse funzionalità avanzate di persistenza e sicurezza.

## Funzionalità Principali

- **Gestione Contatti (CRUD):** Permette di creare, visualizzare, modificare ed eliminare i contatti della rubrica.
- **Interfaccia Grafica Swing:** Un'interfaccia utente intuitiva con una tabella per visualizzare i contatti e finestre di dialogo per l'inserimento e la modifica dei dati.
- **Sistema di Login:** L'accesso all'applicazione è protetto da una schermata di login. Le credenziali di default sono:
  - **Utente:** `admin`
  - **Password:** `password`
- **Persistenza Ibrida:** Ogni contatto salvato, modificato o eliminato viene gestito contemporaneamente su due sistemi di archiviazione per massima robustezza:
  1.  **Database MySQL:** I dati vengono salvati in una tabella dedicata.
  2.  **File System Locale:** Ogni contatto viene salvato come file di testo separato (`NOME-COGNOME.txt`) all'interno di una cartella `informazioni/`.
- **UI Moderna:** I pulsanti di azione sono organizzati in una `JToolBar` per un aspetto più pulito.

---

## Prerequisiti

Prima di eseguire l'applicazione, assicurati di avere installato il seguente software:

1.  **Java Development Kit (JDK):** Versione 8 o successiva.
2.  **MySQL Server:** Un'istanza del server di database MySQL in esecuzione.
3.  **MySQL Workbench (Consigliato):** Uno strumento grafico per gestire facilmente il database.
4.  **MySQL Connector/J:** La libreria (file `.jar`) per la connessione a MySQL da Java. **Questo progetto è stato configurato per usare `mysql-connector-j-9.4.0.jar`**.

---

## Istruzioni per l'Installazione e l'Esecuzione

Per far funzionare il progetto, segui attentamente questi passaggi.

### 1. Preparazione del Database

- Apri MySQL Workbench e connettiti al tuo server di database.
- Apri il file `schema_database.sql` fornito con il progetto.
- Esegui l'intero script. Questo creerà il database `rubrica_db` e la tabella `persone` necessaria all'applicazione.

### 2. Configurazione delle Credenziali

- Trova il file `credenziali_database.properties` nella cartella del progetto.
- Aprilo con un editor di testo e modifica i valori di `db.user` e `db.password` con il nome utente e la password del tuo server MySQL.

### 3. Posizionamento del Driver MySQL

- **Questo è il passo più importante.** Prendi il file del driver che hai scaricato, **`mysql-connector-j-9.4.0.jar`**, e copialo direttamente nella cartella principale del progetto, accanto a tutti i file `.java`.

### 4. Compilazione ed Esecuzione da Terminale

Apri un terminale o un prompt dei comandi e naviga fino alla cartella principale del progetto.

**A) Compila tutti i file Java:**
```bash
javac *.java
java -cp ".;mysql-connector-j-9.4.0.jar" Main
```