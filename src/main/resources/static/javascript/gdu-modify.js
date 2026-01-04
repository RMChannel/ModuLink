function openModifyModal(button) {
    const email = button.getAttribute('data-email');
    const nome = button.getAttribute('data-nome');
    const cognome = button.getAttribute('data-cognome');
    const telefono = button.getAttribute('data-telefono');

    // Fill the form fields
    document.getElementById('modifyOldEmail').value = email; // Hidden field for identification
    document.getElementById('modifyNome').value = nome;
    document.getElementById('modifyCognome').value = cognome;
    document.getElementById('modifyEmail').value = email;
    document.getElementById('modifyTelefono').value = telefono || '';

    // Reset password fields
    document.getElementById('modifyPassword').value = '';
    document.getElementById('modifyConfirmPassword').value = '';
    
    // Reset validation state
    const confirmInput = document.getElementById('modifyConfirmPassword');
    confirmInput.classList.remove('is-invalid', 'is-valid');

    // Show the modal
    const modal = new bootstrap.Modal(document.getElementById('modifyUserModal'));
    modal.show();
}

document.addEventListener('DOMContentLoaded', function() {
    const password = document.getElementById('modifyPassword');
    const confirmPassword = document.getElementById('modifyConfirmPassword');
    const form = document.getElementById('modifyUserForm');

    function validatePassword() {
        if (confirmPassword.value === '') {
            confirmPassword.classList.remove('is-invalid', 'is-valid');
            return;
        }
        
        if (password.value !== confirmPassword.value) {
            confirmPassword.classList.add('is-invalid');
            confirmPassword.classList.remove('is-valid');
        } else {
            confirmPassword.classList.remove('is-invalid');
            confirmPassword.classList.add('is-valid');
        }
    }

    if (password && confirmPassword) {
        password.addEventListener('input', validatePassword);
        confirmPassword.addEventListener('input', validatePassword);
    }

    if (form) {
        form.addEventListener('submit', function(event) {
            if (password.value !== confirmPassword.value) {
                event.preventDefault();
                confirmPassword.classList.add('is-invalid');
            }
        });
    }
});
