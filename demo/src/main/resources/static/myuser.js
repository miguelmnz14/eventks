document.addEventListener('DOMContentLoaded', function() {
    const showform = document.getElementById('mostrarFormulario');
    const form = document.getElementById('formulario');
    showform.addEventListener('click', function() {
        form.style.display = form.style.display === 'none' ? 'block' : 'none';
    });
});
