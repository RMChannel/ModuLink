// Recupera il tema salvato dal localStorage (default: light)
const savedTheme = localStorage.getItem('theme') || 'light';
// Applica il tema immediatamente prima del render della pagina
document.documentElement.setAttribute('data-theme', savedTheme);


document.addEventListener("DOMContentLoaded", function() {

    // ========================================
    // GESTIONE SIDEBAR (Collapse/Expand)
    // ========================================
    const sidebar = document.getElementById('sidebar');
    const sidebarCollapseBtn = document.getElementById('sidebarCollapse');
    const sidebarIcon = sidebarCollapseBtn ? sidebarCollapseBtn.querySelector('i') : null;

    if (sidebar && sidebarCollapseBtn) {
        // Logica Pin/Collapse
        sidebarCollapseBtn.addEventListener('click', function () {
            sidebar.classList.toggle('collapsed');

            // Opzionale: Cambia icona se necessario (es. pin fill vs pin vuoto o frecce)
            // Qui usiamo rotazione CSS
        });
    }

    // ========================================
    // GESTIONE TEMA (Light/Dark Mode)
    // ========================================
    const themeToggleBtn = document.getElementById('themeToggleBtn');

    if (themeToggleBtn) {
        themeToggleBtn.addEventListener('click', function() {
            let currentTheme = document.documentElement.getAttribute('data-theme');
            let newTheme = currentTheme === 'dark' ? 'light' : 'dark';

            document.documentElement.setAttribute('data-theme', newTheme);
            localStorage.setItem('theme', newTheme);
        });
    }
});


