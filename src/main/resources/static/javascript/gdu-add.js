document.addEventListener('DOMContentLoaded', function() {
    const addUserModalElement = document.getElementById('addUserModal');
    if (addUserModalElement) {
        addUserModalElement.addEventListener('show.bs.modal', function (event) {
            // Optional: Clear form fields when opening the modal
            // const form = addUserModalElement.querySelector('form');
            // if(form) form.reset();
        });
    }
});
