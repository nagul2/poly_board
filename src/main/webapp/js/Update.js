
window.onload = function() {
    var form = document.getElementById('updateForm');
    if (form) {
        form.onsubmit = handleUpdateSubmit;
    }
};
