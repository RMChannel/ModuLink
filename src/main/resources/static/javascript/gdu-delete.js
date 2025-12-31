document.addEventListener('DOMContentLoaded', function() {
    const confirmInput = document.getElementById('deleteConfirmInput');
    const deleteBtn = document.getElementById('confirmDeleteBtn');

    if (confirmInput && deleteBtn) {
        confirmInput.addEventListener('input', function() {
            const expectedName = this.getAttribute('data-expected');
            if (this.value === expectedName) {
                deleteBtn.disabled = false;
            } else {
                deleteBtn.disabled = true;
            }
        });
    }
});

function openDeleteModal(button) {
    const email = button.getAttribute('data-email');
    const nome = button.getAttribute('data-nome');
    const cognome = button.getAttribute('data-cognome');
    const fullName = `${nome} ${cognome}`;

    document.getElementById('deleteEmailInput').value = email;
    document.getElementById('deleteTargetName').textContent = fullName;

    const input = document.getElementById('deleteConfirmInput');

    input.value = '';
    input.setAttribute('data-expected', fullName);

    
    // Disable the confirm button initially
    document.getElementById('confirmDeleteBtn').disabled = true;

    // Show the modal
    const modal = new bootstrap.Modal(document.getElementById('deleteUserModal'));
    modal.show();
}
