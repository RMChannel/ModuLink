# 🔷 ModuLink - Modular Management System

![Version](https://img.shields.io/badge/version-3.6.3-blue)
![Java](https://img.shields.io/badge/Java-17-orange)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.x-green)
![Build](https://img.shields.io/badge/build-passing-brightgreen)
![License](https://img.shields.io/badge/license-MIT-lightgrey)

**ModuLink** è una piattaforma **SaaS (Software as a Service)** modulare e multi-tenant progettata per la gestione integrata dei processi aziendali.
Il sistema permette alle aziende di creare il proprio ambiente di lavoro su misura, installando e configurando solo i moduli necessari per le proprie attività operative.

> [!IMPORTANT]
> **Stato del Progetto:** In seguito alla consegna dell'esame, la demo live è stata disabilitata. Inoltre, è stato rimosso dalla pipeline di CI/CD il passaggio di pubblicazione automatica dell'immagine su Docker Hub.

---

#### 🔗 Link Utili
* **Sito Web & Documentazione:** [rmchannel.github.io/ModuLink](https://rmchannel.github.io/ModuLink/)
* **Demo Live:** ~~modulink.run.place~~ (**Disabilitata** - Esame consegnato)

---

#### 📑 Indice
*   [Caratteristiche Principali](#-caratteristiche-principali)
*   [Architettura Modulare](#-architettura-modulare-moduli)
*   [Tech Stack](#-tech-stack)
*   [Struttura del Progetto](#-struttura-del-progetto)
*   [Installazione e Configurazione (Java)](#-installazione-e-configurazione-java)
*   [Deployment con Docker](#-deployment-con-docker)
*   [Utilizzo e Credenziali](#-utilizzo-e-credenziali)
*   [Modulink Team](#-modulink-team-gruppo-nc08)

---

#### 🚀 Caratteristiche Principali
*   **Multi-Tenancy:** Gestione sicura di molteplici aziende con isolamento dei dati.
*   **Onboarding Guidato:** Procedura wizard per la registrazione di nuove aziende e del Responsabile.
*   **Role-Based Access Control (RBAC):** Gestione granulare dei permessi tramite ruoli personalizzabili (GDU/GRU).
*   **Modularità Dinamica:** Possibilità di installare/disinstallare funzionalità tramite uno Store interno (GMA).
*   **Sicurezza:** Autenticazione robusta, hashing delle password (BCrypt) e gestione sessioni.

---

#### 📦 Architettura Modulare (Moduli)

Il sistema è composto dai seguenti moduli funzionali:

##### 🛠️ Moduli Operativi
| Acronimo | Nome Modulo | Descrizione |
| :--- | :--- | :--- |
| **GTM** | **Gestione Task Manager** | Creazione, assegnazione e monitoraggio di task con priorità e scadenze. Supporta assegnazione a singoli utenti o ruoli. |
| **GDM** | **Gestione Magazzino** | Inventario prodotti, gestione giacenze, tracciamento prezzi e operazioni di carico/scarico merce. |
| **GDE/GCA**| **Gestione Eventi/Calendario**| Calendario aziendale condiviso per la pianificazione di riunioni ed eventi, con gestione dei partecipanti,. |

##### ⚙️ Moduli Amministrativi
| Acronimo | Nome Modulo | Descrizione |
| :--- | :--- | :--- |
| **GDR** | **Gestione Responsabile** | Gestione della registrazione aziendale e del profilo del responsabile (utente Master). |
| **GDU** | **Gestione Utenti** | Amministrazione delle anagrafiche dipendenti, creazione account e gestione accessi. |
| **GMA** | **Gestione Moduli** | Accesso allo Store per installare o rimuovere moduli dalla dashboard aziendale. |
| **GRU** | **Gestione Ruoli** | Creazione e modifica dei ruoli e dei permessi associati agli utenti. |

---

#### 💻 Tech Stack

##### Backend
*   **Java 17**
*   **Spring Boot 3** (Web, Security, Data JPA, Validation, Mail)
*   **Maven** (Dependency Management)

##### Frontend
*   **Thymeleaf** (Server-side Java Template Engine)
*   **HTML5 / CSS3** (Custom Stylesheet in `src/main/resources/static/css`)
*   **JavaScript** (Logica client-side in `src/main/resources/static/javascript`)
*   **Bootstrap Icons** (Iconografia)

##### Database & Storage
*   **MySQL 8.0**
*   **FileSystem** (Gestione upload loghi e immagini profilo)

---

#### 📂 Struttura del Progetto

La struttura del codice sorgente segue l'architettura dei sottosistemi,,,:

```text
src
└── main
    └── java
        └── com
            └── modulink
                ├── Controller
                │   ├── AdminModules  (Manage, News, Support)
                │   ├── Dashboard
                │   ├── HomePage
                │   ├── Login / Register / EditUser
                │   └── UserModules
                │       ├── GDE (Eventi)
                │       ├── GDM (Magazzino)
                │       ├── GDR (Responsabile)
                │       ├── GDU (Utenti)
                │       ├── GMA (Store & Ruoli)
                │       ├── GRU (Gestione Ruoli)
                │       └── GTM (Task Manager)
                ├── DatabasePopulator
                └── Model
                    ├── Azienda / Utente / Ruolo
                    ├── Modulo / Task / Prodotto / Eventi
                    └── Relazioni (Assegnazione, Partecipazione, ecc.)
```

---

#### ☕ Installazione e Configurazione (Java)

Questa sezione guida alla configurazione manuale dell'ambiente di sviluppo.

**Requisiti:**
*   Java SDK (JDK) 17 o superiore.
*   Database MySQL attivo.
*   Server SMTP/IMAP/POP per le notifiche mail.
*   Apache Maven.
*   Git.

**Passaggi:**

1.  **Clonare il repository:**
    ```bash
    git clone https://github.com/RMChannel/ModuLink
    ```
    Oppure scaricare lo ZIP dal repository.

2.  **Compilazione:**
    Posizionarsi nella cartella radice (dove si trova `pom.xml`) ed eseguire:
    ```bash
    mvn clean package
    ```
    Questo comando scarica le dipendenze e genera il file `.jar` nella cartella `target/`,.

3.  **Esecuzione:**
    Avviare il server tramite il comando java, assicurandosi di aver configurato le variabili d'ambiente necessarie (vedi sezione Docker per la lista delle variabili):
    ```bash
    java -jar target/modulink-3.6.3-RELEASE.jar
    ```
    L'applicazione sarà raggiungibile all'indirizzo `http://localhost:80`.

---

#### 🐳 Deployment con Docker

Il progetto dispone di una pipeline automatica che pubblica l'immagine su Docker Hub (`rc82/modulink`).

**Docker Run da terminale:**

È possibile avviare l'applicazione senza compilare il codice, scaricando l'immagine e passando le variabili d'ambiente per Database e Mail,:

```bash
docker pull rc82/modulink

docker run -it --rm \
-e DB_URL="jdbc:mysql://localhost:3306/modulink" \
-e DB_USER="root" \
-e DB_PASS="tua_password" \
-e MAIL="noreply@tuodominio.it" \
-e PASSWORD_MAIL="password_smtp" \
-p 80:8080 \
rc82/modulink
```

*Nota: Il database non è incluso nell'immagine e va configurato separatamente o tramite un container dedicato (es. MySQL o PostgreSQL)*.

---

#### 🔑 Utilizzo e Credenziali

Il server genera automaticamente delle aziende di base per il testing e un account amministratore globale (azienda admin).

**Credenziali di Default:**

| Email | Password |
| :--- | :--- |
| **admin@modulink.run.place** | **Admin1234@** |

⚠️ **Importante:** Si consiglia di cambiare la password immediatamente dopo il primo accesso per motivi di sicurezza.

---

#### 👥 ModuLink Team (Gruppo NC08)

*   **Roberto Cito** (RC) - Team Member
*   **Daniele Carpentieri** (DC) - Team Member
*   **Aleksandre Chikviladze** (AC) - Team Member
*   **Arjel Buzi** (AB) - Team Member
