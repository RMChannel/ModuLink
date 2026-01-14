document.addEventListener('DOMContentLoaded', function() {
    // Select All Roles Logic
    const selectAllCheckbox = document.getElementById('selectAllRoles');
    if (selectAllCheckbox) {
        selectAllCheckbox.addEventListener('change', function() {
            const roleCheckboxes = document.querySelectorAll('.role-checkbox');
            roleCheckboxes.forEach(cb => {
                cb.checked = this.checked;
            });
        });
    }

    // Individual Role Checkbox Logic
    const roleCheckboxes = document.querySelectorAll('.role-checkbox');
    roleCheckboxes.forEach(cb => {
        cb.addEventListener('change', function() {
            if (!this.checked) {
                if (selectAllCheckbox) selectAllCheckbox.checked = false;
            } else {
                const allChecked = Array.from(roleCheckboxes).every(c => c.checked);
                if (selectAllCheckbox) selectAllCheckbox.checked = allChecked;
            }
        });
    });
});

/**
 * Opens the "Manage Roles" modal and populates it with data.
 * @param {HTMLElement} element - The button that triggered the modal.
 */
function openManageRolesModal(element) {
    const id = element.getAttribute('data-id');
    const nome = element.getAttribute('data-nome');
    // data-assigned-roles comes as a string like "[1, 2, 3]" or "[]"
    let assignedRolesRaw = element.getAttribute('data-assigned-roles');

    document.getElementById('editModuloId').value = id;
    document.getElementById('editModuloName').textContent = nome;

    // Reset all checkboxes first
    const roleCheckboxes = document.querySelectorAll('.role-checkbox');
    roleCheckboxes.forEach(cb => cb.checked = false);
    const selectAllCheckbox = document.getElementById('selectAllRoles');
    if (selectAllCheckbox) selectAllCheckbox.checked = false;

    // Parse and check assigned roles
    if (assignedRolesRaw && assignedRolesRaw !== '[]') {
        // Remove brackets and split by comma
        const cleanString = assignedRolesRaw.replace(/^\[|\]$/g, '');
        if (cleanString.trim() !== '') {
            const assignedIds = cleanString.split(',').map(s => s.trim());
            
            assignedIds.forEach(roleId => {
                const checkbox = document.getElementById('role-' + roleId);
                if (checkbox) {
                    checkbox.checked = true;
                }
            });

            // Update "Select All" state if all are checked
            const allChecked = Array.from(roleCheckboxes).every(c => c.checked);
            if (selectAllCheckbox && roleCheckboxes.length > 0) {
                selectAllCheckbox.checked = allChecked;
            }
        }
    }

    const modal = new bootstrap.Modal(document.getElementById('manageRolesModal'));
    modal.show();
}
