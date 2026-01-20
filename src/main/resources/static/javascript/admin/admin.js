/**
 * Admin Dashboard Scripts
 */

let aziendaModal;

document.addEventListener('DOMContentLoaded', function() {
    const modalEl = document.getElementById('aziendaModal');
    if (modalEl) {
        aziendaModal = new bootstrap.Modal(modalEl);
    }
});


function editAzienda(btn) {
    if (!aziendaModal) return;

    const modalTitle = document.getElementById('aziendaModalLabel');
    
    modalTitle.textContent = 'Modifica Azienda';
    
    document.getElementById('id_azienda').value = btn.getAttribute('data-id');
    document.getElementById('nome').value = btn.getAttribute('data-nome');
    document.getElementById('piva').value = btn.getAttribute('data-piva');
    document.getElementById('indirizzo').value = btn.getAttribute('data-indirizzo');
    document.getElementById('citta').value = btn.getAttribute('data-citta');
    document.getElementById('cap').value = btn.getAttribute('data-cap');
    document.getElementById('telefono').value = btn.getAttribute('data-telefono');
    document.getElementById('logo').value = btn.getAttribute('data-logo');

    aziendaModal.show();
}

