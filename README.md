# 🔷 ModuLink - Modular Management System

![Version](https://img.shields.io/badge/version-1.0.0-blue)
![Java](https://img.shields.io/badge/Java-17-orange)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.x-green)
![Build](https://img.shields.io/badge/build-passing-brightgreen)
![License](https://img.shields.io/badge/license-MIT-lightgrey)

**ModuLink** è una piattaforma **SaaS (Software as a Service)** modulare e multi-tenant progettata per la gestione integrata dei processi aziendali.
Il sistema permette alle aziende di creare il proprio ambiente di lavoro su misura, installando e configurando solo i moduli necessari per le proprie attività operative.

---

## 📑 Indice

- [Caratteristiche Principali](#-caratteristiche-principali)
- [Architettura Modulare](#-architettura-modulare-moduli)
- [Tech Stack](#-tech-stack)
- [Struttura del Progetto](#-struttura-del-progetto)
- [Installazione e Configurazione](#-installazione-e-configurazione)
- [Esecuzione con Docker](#-esecuzione-con-docker)
- [Documentazione](#-documentazione)
- [Team](#-team)

---

## 🚀 Caratteristiche Principali

* **Multi-Tenancy:** Gestione sicura di molteplici aziende con isolamento dei dati.
* **Onboarding Guidato:** Procedura wizard per la registrazione di nuove aziende e del Responsabile.
* **Role-Based Access Control (RBAC):** Gestione granulare dei permessi tramite ruoli personalizzabili (GDU/GRU).
* **Modularità Dinamica:** Possibilità di installare/disinstallare funzionalità tramite uno Store interno (GMA).
* **Sicurezza:** Autenticazione robusta, hashing delle password (BCrypt) e gestione sessioni.

---

## 📦 Architettura Modulare (Moduli)

Il sistema è composto dai seguenti moduli funzionali, identificati dai rispettivi acronimi:

### 🛠️ Moduli Operativi
| Acronimo | Nome Modulo | Descrizione |
| :--- | :--- | :--- |
| **GTM** | **Gestione Task Manager** | Creazione, assegnazione e monitoraggio di task con priorità e scadenze. Supporta assegnazione a singoli utenti o ruoli. |
| **GDM** | **Gestione Magazzino** | Inventario prodotti, gestione giacenze, tracciamento prezzi e operazioni di carico/scarico merce. |
| **GDE** | **Gestione Eventi** | Calendario aziendale condiviso per la pianificazione di riunioni ed eventi, con gestione dei partecipanti. |

### ⚙️ Moduli Amministrativi
| Acronimo | Nome Modulo | Descrizione |
| :--- | :--- | :--- |
| **GDU** | **Gestione Utenti** | CRUD completo dei dipendenti, gestione anagrafiche e recupero credenziali. |
| **GRU** | **Gestione Ruoli** | Creazione di ruoli personalizzati (es. "Magazziniere", "Sviluppatore") e assegnazione permessi. |
| **GMA** | **Gestione Moduli** | Store interno per installare/rimuovere moduli e definire quali ruoli possono accedervi. |
| **GDR** | **Gestione Responsabile** | Configurazione dei dati aziendali, logo e impostazioni del tenant. |

### 📢 Moduli di Supporto
* **News:** Sistema di bacheca per comunicazioni interne o globali dalla piattaforma.
* **Supporto:** Sistema di ticketing integrato per l'assistenza tecnica.

---

## 💻 Tech Stack

### Backend
* **Java 17**
* **Spring Boot 3** (Web, Security, Data JPA, Validation, Mail)
* **Maven** (Dependency Management)

### Frontend
* **Thymeleaf** (Server-side Java Template Engine)
* **HTML5 / CSS3** (Custom Stylesheet in `src/main/resources/static/css`)
* **JavaScript** (Logica client-side in `src/main/resources/static/javascript`)
* **Bootstrap Icons** (Iconografia)

### Database & Storage
* **MySQL 8.0**
* **FileSystem** (Gestione upload loghi e immagini profilo)

---

## 📂 Struttura del Progetto

```text
ModuLink-main/
├── src/
│   ├── main/
│   │   ├── java/com/modulink/
│   │   │   ├── Controller/       # Gestione richieste HTTP (diviso per Moduli)
│   │   │   ├── Model/            # Entità JPA, Repository e Service
│   │   │   ├── DatabasePopulator/# Script inizializzazione dati
│   │   │   └── ...
│   │   ├── resources/
│   │   │   ├── static/           # CSS, JS, Immagini
│   │   │   ├── templates/        # Viste HTML (Thymeleaf)
│   │   │   └── application.properties
├── docs/                         # Documentazione (RAD, SDD, Manuali)
├── Dockerfile                    # Configurazione Container
├── pom.xml                       # Configurazione Maven
└── ...
