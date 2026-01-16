document.addEventListener('DOMContentLoaded', function() {
    const addUserModalElement = document.getElementById('addUserModal');
    if (addUserModalElement) {
        const form = addUserModalElement.querySelector('form');
        if (form) {
            form.addEventListener('submit', function() {
                const submitBtn = form.querySelector('button[type="submit"]');
                if (submitBtn) {
                    submitBtn.disabled = true;
                    submitBtn.innerHTML = '<span class="spinner-border spinner-border-sm" role="status" aria-hidden="true"></span> Creazione...';
                }
            });
        }
    }
});
