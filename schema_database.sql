-- Script per la creazione dello schema del database per l'applicazione Rubrica.

-- 1. Creare il database (se non esiste già)
CREATE DATABASE IF NOT EXISTS rubrica_db;

-- 2. Selezionare il database appena creato
USE rubrica_db;

-- 3. Creare la tabella per memorizzare le persone
CREATE TABLE IF NOT EXISTS persone (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    cognome VARCHAR(100) NOT NULL,
    indirizzo VARCHAR(255),
    telefono VARCHAR(50),
    eta INT
);

-- (Opzionale) Creare la tabella per gli utenti se si vuole memorizzare anche quelli
-- CREATE TABLE IF NOT EXISTS utenti (
--     id INT AUTO_INCREMENT PRIMARY KEY,
--     username VARCHAR(50) NOT NULL UNIQUE,
--     password VARCHAR(255) NOT NULL
-- );
