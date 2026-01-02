# Guida all'Implementazione Moduli e Dashboard

Questo documento riassume le modifiche apportate al frontend della Dashboard e spiega come collegare la logica di backend per rendere dinamica la lista dei moduli accessibili all'utente.

## 1. Panoramica Modifiche Frontend

### Stile e Tema (CSS)
Il file `dashboard-layout.css` è stato completamente rifatto per supportare:
- **Design Moderno**: Utilizzo di variabili CSS (`:root`) per una palette colori coerente (Verde `#00b074` come colore primario).
- **Dark Mode**: Supporto nativo per il tema scuro tramite l'attributo `[data-theme="dark"]`. La preferenza dell'utente viene salvata nel `localStorage` del browser.
- **Transizioni**: Animazioni fluide per l'apertura/chiusura della sidebar e il cambio tema.

### Struttura (HTML/Thymeleaf)
I file `sidebar.html` e `dashboard.html` sono stati aggiornati.
- **Sidebar Dinamica**: È stato predisposto un blocco Thymeleaf che itera su una lista di moduli (`userModules`).
- **Toggle Tema**: Aggiunto un interruttore Light/Dark in basso nella sidebar.

---

## 2. Come Implementare la Logica Backend (Esercizio Moduli)

Per rendere operativa la lista dei moduli ("...se l'utente ha accesso alla classe modulo allora può gestire quella classe"), seguire questi passaggi nel codice Java.

### A. Aggiornare il Controller (`DashboardController.java`)

Attualmente il controller restituisce solo la vista. Bisogna iniettare nel `Model` la lista dei moduli a cui l'utente loggato ha accesso.

Esempio di implementazione suggerita:

```java
@Controller
public class DashboardController {

    @Autowired
    private UserService userService; // Ipotetico service
    
    @Autowired
    private ModuloService moduloService; // Ipotetico service

    @GetMapping("/dashboard")
    public String dashboard(Principal principal, Model model) {
        if (principal == null) {
            return "redirect:/";
        }

        String username = principal.getName();
        
        // 1. Recuperare l'utente dal database
        User user = userService.findByUsername(username);
        
        // 2. Recuperare i moduli associati all'utente (es. tramite ruolo o relazione diretta)
        // Questa logica dipende dalla tua architettura (es. User -> Roles -> Permissions -> Modules)
        List<ModuloEntity> moduliAccessibili = moduloService.findModulesForUser(user);
        
        // 3. Aggiungere la lista al model con il nome esatto usato nella view ('userModules')
        model.addAttribute("userModules", moduliAccessibili);
        
        // 4. Aggiungere altre info utente se necessario
        model.addAttribute("user", user);

        return "user/dashboard";
    }
}
```

### B. Struttura Dati (Entità)

Assicurati che l'oggetto `ModuloEntity` (o DTO equivalente) abbia i metodi getter per i campi usati nel template `sidebar.html`:

- `getId()`: Per costruire il link (es. `/modulo/1`).
- `getNome()`: Per visualizzare il nome del modulo.

Esempio in `sidebar.html`:
```html
<th:block th:if="${userModules != null}">
    <li class="nav-item" th:each="modulo : ${userModules}">
        <a th:href="@{'/modulo/' + ${modulo.id}}" class="nav-link">
            <i class="bi bi-box-seam"></i> 
            <span class="link-text" th:text="${modulo.nome}">Nome Modulo</span>
        </a>
    </li>
</th:block>
```

Se `userModules` è `null` o vuoto, quella sezione della sidebar non verrà renderizzata.

---

## 3. Gestione del Tema (Dark/Light)

Il sistema utilizza JavaScript puro per gestire il tema, senza bisogno di chiamate al server.

1. Al caricamento della pagina (`dashboard.html`), uno script nell'`<head>` legge `localStorage.getItem('theme')`.
2. Applica l'attributo `data-theme="dark"` (o light) al tag `<html>` prima che la pagina venga renderizzata, evitando sfarfallii.
3. Il bottone nella sidebar inverte il valore e aggiorna il `localStorage`.

---

## 4. Prossimi Passaggi

1. Implementare il metodo `findModulesForUser` nel Service layer.
2. Collegare i link generati (es. `/modulo/{id}`) a dei Controller specifici per gestire le singole funzionalità del modulo.
