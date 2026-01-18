document.addEventListener('DOMContentLoaded', function() {
    // Select All Users Logic
    const selectAllCheckbox = document.getElementById('selectAllUsers');
    if (selectAllCheckbox) {
        selectAllCheckbox.addEventListener('change', function() {
            const userCheckboxes = document.querySelectorAll('.user-checkbox');
            userCheckboxes.forEach(cb => {
                cb.checked = this.checked;
            });
        });
    }

    // Individual User Checkbox Logic (to update "Select All" state)
    const userCheckboxes = document.querySelectorAll('.user-checkbox');
    userCheckboxes.forEach(cb => {
        cb.addEventListener('change', function() {
            if (!this.checked) {
                if (selectAllCheckbox) selectAllCheckbox.checked = false;
            } else {
                const allChecked = Array.from(userCheckboxes).every(c => c.checked);
                if (selectAllCheckbox) selectAllCheckbox.checked = allChecked;
            }
        });
    });

    // --- LIVE PREVIEW FOR MODIFY MODAL ---
    setupLivePreview('modifyNome', 'modifyColore', 'rolePreviewBadge', 'modifyModalColorStrip', 'modifyColoreHex', 'modifyModalIcon');
    
    // --- LIVE PREVIEW FOR ADD MODAL ---
    setupLivePreview('addNome', 'addColore', 'addRolePreviewBadge', 'addModalColorStrip', 'addColoreHex', 'addModalIcon');
});

/**
 * Sets up live preview for a modal's role badge
 */
function setupLivePreview(nomeId, coloreId, badgeId, stripId, hexId, iconId) {
    const nomeInput = document.getElementById(nomeId);
    const coloreInput = document.getElementById(coloreId);
    const badge = document.getElementById(badgeId);
    const strip = document.getElementById(stripId);
    const hexInput = document.getElementById(hexId);
    const icon = document.getElementById(iconId);

    if (nomeInput && badge) {
        nomeInput.addEventListener('input', function() {
            badge.textContent = this.value || (nomeId.startsWith('add') ? 'Nuovo Ruolo' : 'Ruolo');
        });
    }

    if (coloreInput) {
        coloreInput.addEventListener('input', function() {
            const color = this.value;
            updateModalVisuals(color, badge, strip, hexInput, icon);
        });
        
        // Initial sync for add modal or if values are pre-filled
        if (coloreInput.value) {
            updateModalVisuals(coloreInput.value, badge, strip, hexInput, icon);
        }
    }
}

/**
 * Updates the visual elements of a modal based on a color
 */
function updateModalVisuals(color, badge, strip, hexInput, icon) {
    if (badge) {
        badge.style.backgroundColor = color;
        badge.style.color = getContrastYIQ(color);
    }
    if (strip) strip.style.backgroundColor = color;
    if (hexInput) hexInput.value = color.toUpperCase();
    if (icon) {
        icon.style.borderColor = color;
        icon.style.color = color;
    }
}

/**
 * Helper to determine if text should be black or white based on background color
 */
function getContrastYIQ(hexcolor){
    hexcolor = hexcolor.replace("#", "");
    var r = parseInt(hexcolor.substr(0,2),16);
    var g = parseInt(hexcolor.substr(2,2),16);
    var b = parseInt(hexcolor.substr(4,2),16);
    var yiq = ((r*299)+(g*587)+(b*114))/1000;
    return (yiq >= 128) ? 'black' : 'white';
}

/**
 * Opens the "Modify Role" modal and populates it with data.
 * @param {HTMLElement} element - The button that triggered the modal.
 */
function openModifyRoleModal(element) {
    const id = element.getAttribute('data-id');
    const nome = element.getAttribute('data-nome');
    const colore = element.getAttribute('data-colore') || '#634BFF';
    const descrizione = element.getAttribute('data-descrizione');

    document.getElementById('modifyId').value = id;
    document.getElementById('modifyNome').value = nome;
    document.getElementById('modifyColore').value = colore;
    document.getElementById('modifyColoreHex').value = colore.toUpperCase();
    document.getElementById('modifyDescrizione').value = descrizione;

    // Update Live Preview Initial State
    const badge = document.getElementById('rolePreviewBadge');
    const strip = document.getElementById('modifyModalColorStrip');
    const hexInput = document.getElementById('modifyColoreHex');
    const icon = document.getElementById('modifyModalIcon');

    if (badge) badge.textContent = nome;
    updateModalVisuals(colore, badge, strip, hexInput, icon);

    const modal = new bootstrap.Modal(document.getElementById('modifyRoleModal'));
    modal.show();
}

/**
 * Opens the "Delete Role" modal and populates it with data.
 * @param {HTMLElement} element - The button that triggered the modal.
 */
function openDeleteRoleModal(element) {
    const id = element.getAttribute('data-id');
    const nome = element.getAttribute('data-nome');

    document.getElementById('deleteIdInput').value = id;
    document.getElementById('deleteTargetName').textContent = nome;

    const modal = new bootstrap.Modal(document.getElementById('deleteRoleModal'));
    modal.show();
}

/**
 * Opens the "Assign Users" modal and pre-selects assigned users.
 * @param {HTMLElement} element - The button that triggered the modal.
 */
function openAssignModal(element) {
    const id = element.getAttribute('data-id');
    const nome = element.getAttribute('data-nome');
    // data-assigned-users comes as a string like "[1, 2, 3]" or "[]"
    let assignedUsersRaw = element.getAttribute('data-assigned-users');

    document.getElementById('assignRoleId').value = id;
    document.getElementById('assignRoleName').textContent = nome;

    // Reset all checkboxes first
    const userCheckboxes = document.querySelectorAll('.user-checkbox');
    userCheckboxes.forEach(cb => cb.checked = false);
    const selectAllCheckbox = document.getElementById('selectAllUsers');
    if (selectAllCheckbox) selectAllCheckbox.checked = false;

    // Parse and check assigned users
    if (assignedUsersRaw && assignedUsersRaw !== '[]') {
        // Remove brackets and split by comma
        const cleanString = assignedUsersRaw.replace(/^\[|\]$/g, '');
        if (cleanString.trim() !== '') {
            const assignedIds = cleanString.split(',').map(s => s.trim());
            
            assignedIds.forEach(userId => {
                const checkbox = document.getElementById('user-' + userId);
                if (checkbox) {
                    checkbox.checked = true;
                }
            });

            // Update "Select All" state if all are checked
            const allChecked = Array.from(userCheckboxes).every(c => c.checked);
            if (selectAllCheckbox && userCheckboxes.length > 0) {
                selectAllCheckbox.checked = allChecked;
            }
        }
    }

    const modal = new bootstrap.Modal(document.getElementById('assignUsersModal'));
    modal.show();
}
