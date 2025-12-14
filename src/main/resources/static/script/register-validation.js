document.addEventListener("DOMContentLoaded", () => {

    const form = document.querySelector("form");

    form.addEventListener("submit", function (e) {
        clearErrors();

        /* =====================
           NOME
        ===================== */
        const nome = getValue("nome");

        // NBN
        if (!nome) {
            return error("nome", "Il nome non può essere vuoto");
        }

        // LNEN
        if (nome.length < 2 || nome.length > 50) {
            return error("nome", "Il nome deve avere tra 2 e 50 caratteri");
        }

        /* =====================
           COGNOME
        ===================== */
        const cognome = getValue("cognome");

        // NBC
        if (!cognome) {
            return error("cognome", "Il cognome non può essere vuoto");
        }

        // LNEC
        if (cognome.length < 2 || cognome.length > 50) {
            return error("cognome", "Il cognome deve avere tra 2 e 50 caratteri");
        }

        /* =====================
           EMAIL
        ===================== */
        const email = getValue("email");

        // NBE
        if (!email) {
            return error("email", "L'email non può essere vuota");
        }

        // LNEE
        if (email.length < 2 || email.length > 50) {
            return error("email", "L'email deve avere tra 2 e 50 caratteri");
        }

        // FORE
        const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
        if (!emailRegex.test(email)) {
            return error("email", "Formato email non valido");
        }

        // ESIE → solo placeholder (va fatto lato server)
        // if (emailExists) { ERR }

        /* =====================
           TELEFONO
        ===================== */
        const telefono = getValue("telefono");

        if (telefono) {
            const phoneRegex = /^[0-9+\s]{6,20}$/;
            if (!phoneRegex.test(telefono)) {
                return error("telefono", "Formato numero di telefono non valido");
            }
        }

        /* =====================
           PASSWORD
        ===================== */
        const password = getValue("password");

        // NBP
        if (!password) {
            return error("password", "La password non può essere vuota");
        }

        // LNEP
        if (password.length < 2 || password.length > 50) {
            return error("password", "La password deve avere tra 2 e 50 caratteri");
        }

        /* =====================
           CONFERMA PASSWORD
        ===================== */
        const confermaPassword = getValue("confermaPassword");

        // NBCP
        if (!confermaPassword) {
            return error("confermaPassword", "Conferma la password");
        }

        // LNECP
        if (confermaPassword.length < 2 || confermaPassword.length > 50) {
            return error("confermaPassword", "La conferma password deve avere tra 2 e 50 caratteri");
        }

        // UGU
        if (password !== confermaPassword) {
            return error("confermaPassword", "Le password non coincidono");
        }

        /* =====================
           IMMAGINE
        ===================== */
        const fileInput = document.getElementById("immagineProfilo");
        const file = fileInput.files[0];

        if (file) {
            // NUF
            if (!file.type.startsWith("image/")) {
                return error("immagineProfilo", "Il file caricato non è un'immagine");
            }

            // DIM (MB)
            const sizeMB = file.size / (1024 * 1024);
            if (sizeMB > 12) {
                return error("immagineProfilo", "L'immagine supera i 12 MB");
            }
        }

        // Se arriva qui → OK
        form.submit();
    });

    /* =====================
       FUNZIONI DI SUPPORTO
    ===================== */

    function getValue(id) {
        return document.getElementById(id).value.trim();
    }

    function error(fieldId, message) {
        const field = document.getElementById(fieldId);
        field.classList.add("is-invalid");

        let feedback = field.nextElementSibling;
        if (!feedback || !feedback.classList.contains("invalid-feedback")) {
            feedback = document.createElement("div");
            feedback.className = "invalid-feedback";
            field.after(feedback);
        }

        feedback.textContent = message;
        field.focus();
        return false;
    }

    function clearErrors() {
        document.querySelectorAll(".is-invalid").forEach(el => el.classList.remove("is-invalid"));
        document.querySelectorAll(".invalid-feedback").forEach(el => el.remove());
    }

});
