document.addEventListener('DOMContentLoaded', function() {
    // Initialize tooltips if any
    var tooltipTriggerList = [].slice.call(document.querySelectorAll('[data-bs-toggle="tooltip"]'))
    var tooltipList = tooltipTriggerList.map(function (tooltipTriggerEl) {
        return new bootstrap.Tooltip(tooltipTriggerEl)
    })
});

function openEditModal(button) {
    const id = button.getAttribute('data-id');
    const nome = button.getAttribute('data-nome');
    const quantita = button.getAttribute('data-quantita');
    const prezzo = button.getAttribute('data-prezzo');
    const descrizione = button.getAttribute('data-descrizione');
    const categoria = button.getAttribute('data-categoria');

    document.getElementById('editIdProdotto').value = id;
    document.getElementById('editNome').value = nome;
    document.getElementById('editQuantita').value = quantita;
    document.getElementById('editPrezzo').value = prezzo;
    document.getElementById('editDescrizione').value = descrizione;
    document.getElementById('editCategoria').value = categoria;

    const modal = new bootstrap.Modal(document.getElementById('editProductModal'));
    modal.show();
}

function openDeleteModal(button) {
    const id = button.getAttribute('data-id');
    const nome = button.getAttribute('data-nome');

    document.getElementById('deleteIdProdotto').value = id;
    document.getElementById('deleteProductName').textContent = nome;

    const modal = new bootstrap.Modal(document.getElementById('deleteProductModal'));
    modal.show();
}

function openBuyModal(button) {
    const id = button.getAttribute('data-id');
    const nome = button.getAttribute('data-nome');
    
    document.getElementById('buyIdProdotto').value = id;
    document.getElementById('buyProductName').textContent = nome;
    document.getElementById('buyQuantita').value = 1; // Default to 1

    const modal = new bootstrap.Modal(document.getElementById('buyProductModal'));
    modal.show();
}

function openSellModal(button) {
    const id = button.getAttribute('data-id');
    const nome = button.getAttribute('data-nome');
    const currentQty = button.getAttribute('data-quantita');
    
    document.getElementById('sellIdProdotto').value = id;
    document.getElementById('sellProductName').textContent = nome;
    document.getElementById('sellQuantita').value = 1; // Default to 1
    document.getElementById('sellQuantita').max = currentQty;

    const modal = new bootstrap.Modal(document.getElementById('sellProductModal'));
    modal.show();
}
