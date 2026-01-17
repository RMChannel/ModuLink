document.addEventListener('DOMContentLoaded', function() {
    // --- Global Variables for this module ---
    const searchInput = document.getElementById('assignUserSearch');
    const userListContainer = document.getElementById('assignUserList');
    const selectAllBtn = document.getElementById('btnSelectAll');
    const deselectAllBtn = document.getElementById('btnDeselectAll');
    const counterSpan = document.getElementById('selectedCount');
    
    // --- Event Listeners ---
    if (searchInput) {
        searchInput.addEventListener('input', function(e) {
            filterUsers(e.target.value);
        });
    }

    if (selectAllBtn) {
        selectAllBtn.addEventListener('click', function() {
            toggleAllVisible(true);
        });
    }

    if (deselectAllBtn) {
        deselectAllBtn.addEventListener('click', function() {
            toggleAllVisible(false);
        });
    }

    // Delegate click event for user items
    if (userListContainer) {
        userListContainer.addEventListener('click', function(e) {
            const item = e.target.closest('.assign-user-item');
            if (item) {
                toggleUserSelection(item);
            }
        });
    }
});

/**
 * Filter users based on search text
 */
function filterUsers(searchText) {
    const filter = searchText.toLowerCase();
    const items = document.querySelectorAll('.assign-user-item');
    
    items.forEach(item => {
        const name = item.getAttribute('data-name').toLowerCase();
        const email = item.getAttribute('data-email').toLowerCase();
        
        if (name.includes(filter) || email.includes(filter)) {
            item.style.display = 'flex';
        } else {
            item.style.display = 'none';
        }
    });
}

/**
 * Toggle selection state of a single user item
 */
function toggleUserSelection(item) {
    const checkbox = item.querySelector('input[type="checkbox"]');
    if (!checkbox) return;

    // Toggle Checkbox
    checkbox.checked = !checkbox.checked;
    
    // Toggle Visual Class
    if (checkbox.checked) {
        item.classList.add('selected');
    } else {
        item.classList.remove('selected');
    }

    updateCounter();
}

/**
 * Toggle all currently VISIBLE users
 */
function toggleAllVisible(select) {
    const items = document.querySelectorAll('.assign-user-item');
    
    items.forEach(item => {
        // Only affect visible items (respect search filter)
        if (item.style.display !== 'none') {
            const checkbox = item.querySelector('input[type="checkbox"]');
            if (checkbox) {
                checkbox.checked = select;
                if (select) {
                    item.classList.add('selected');
                } else {
                    item.classList.remove('selected');
                }
            }
        }
    });
    updateCounter();
}

/**
 * Update the selected count text
 */
function updateCounter() {
    const checkedCount = document.querySelectorAll('.assign-user-item input[type="checkbox"]:checked').length;
    const counterSpan = document.getElementById('selectedCount');
    if (counterSpan) {
        counterSpan.textContent = checkedCount + ' selezionati';
    }
}

/**
 * Custom function to open the modal (overrides/replaces logic if called directly)
 * This is called from the onclick in the HTML
 */
function openAssignUsersModal(element) {
    const id = element.getAttribute('data-id');
    const nome = element.getAttribute('data-nome');
    let assignedUsersRaw = element.getAttribute('data-assigned-users');

    document.getElementById('assignRoleId').value = id;
    document.getElementById('assignRoleName').textContent = nome;

    // Reset UI
    const searchInput = document.getElementById('assignUserSearch');
    if (searchInput) {
        searchInput.value = '';
        filterUsers(''); // Reset filter
    }

    // Reset All Items
    const allItems = document.querySelectorAll('.assign-user-item');
    allItems.forEach(item => {
        item.classList.remove('selected');
        const cb = item.querySelector('input[type="checkbox"]');
        if (cb) cb.checked = false;
    });

    // Parse assigned users
    if (assignedUsersRaw && assignedUsersRaw !== '[]') {
        const cleanString = assignedUsersRaw.replace(/^\[|\]$/g, '');
        if (cleanString.trim() !== '') {
            const assignedIds = cleanString.split(',').map(s => s.trim());
            
            assignedIds.forEach(userId => {
                const item = document.getElementById('user-item-' + userId);
                if (item) {
                    item.classList.add('selected');
                    const cb = item.querySelector('input[type="checkbox"]');
                    if (cb) cb.checked = true;
                }
            });
        }
    }

    updateCounter();

    const modal = new bootstrap.Modal(document.getElementById('assignUsersModal'));
    modal.show();
}
