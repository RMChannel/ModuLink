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
});

/**
 * Opens the "Modify Role" modal and populates it with data.
 * @param {HTMLElement} element - The button that triggered the modal.
 */
function openModifyRoleModal(element) {
    const id = element.getAttribute('data-id');
    const nome = element.getAttribute('data-nome');
    const colore = element.getAttribute('data-colore');
    const descrizione = element.getAttribute('data-descrizione');

    document.getElementById('modifyId').value = id;
    document.getElementById('modifyNome').value = nome;
    document.getElementById('modifyColore').value = colore;
    document.getElementById('modifyDescrizione').value = descrizione;

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
