// ========================================
// THEME MANAGEMENT (Immediate Execution)
// ========================================

// Function to determine the preferred theme
function getPreferredTheme() {
    // 1. Check if user has manually saved a preference
    const savedTheme = localStorage.getItem('theme');
    if (savedTheme) {
        return savedTheme;
    }
    // 2. Check system preference
    if (window.matchMedia && window.matchMedia('(prefers-color-scheme: dark)').matches) {
        return 'dark';
    }
    // 3. Default fallback
    return 'light';
}

// Apply the theme immediately (prevents flash of wrong theme)
const initialTheme = getPreferredTheme();
document.documentElement.setAttribute('data-theme', initialTheme);
document.documentElement.setAttribute('data-bs-theme', initialTheme);

// Optional: Listen for system theme changes if user hasn't locked a preference
if (window.matchMedia) {
    window.matchMedia('(prefers-color-scheme: dark)').addEventListener('change', e => {
        if (!localStorage.getItem('theme')) {
            const newSystemTheme = e.matches ? 'dark' : 'light';
            document.documentElement.setAttribute('data-theme', newSystemTheme);
            document.documentElement.setAttribute('data-bs-theme', newSystemTheme);
        }
    });
}

document.addEventListener("DOMContentLoaded", function() {

    // ========================================
    // GESTIONE SIDEBAR (Collapse/Expand)
    // ========================================
    const sidebar = document.getElementById('sidebar');
    const sidebarCollapseBtn = document.getElementById('sidebarCollapse');

    if (sidebar) {
        // 1. Restore Sidebar State from LocalStorage
        const isCollapsed = localStorage.getItem('sidebarCollapsed') === 'true';
        if (isCollapsed) {
            sidebar.classList.add('collapsed');
        }

        // 2. Toggle Logic
        if (sidebarCollapseBtn) {
            sidebarCollapseBtn.addEventListener('click', function () {
                sidebar.classList.toggle('collapsed');
                
                // Save state to LocalStorage
                const isNowCollapsed = sidebar.classList.contains('collapsed');
                localStorage.setItem('sidebarCollapsed', isNowCollapsed);
            });
        }
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
            document.documentElement.setAttribute('data-bs-theme', newTheme);
            localStorage.setItem('theme', newTheme);
        });
    }
});


