document.addEventListener("DOMContentLoaded", function() {
    var slides = document.querySelectorAll('.slide');
    var currentSlide = 0;
    var slideInterval = setInterval(nextSlide, 4000);
    function nextSlide() {
        slides[currentSlide].style.display = 'none';
        currentSlide = (currentSlide + 1) % slides.length;
        slides[currentSlide].style.display = 'block';
    }
});

var quill = new Quill('#editor', {
    theme: 'snow'  // Tema de estilo del editor (puedes cambiarlo a 'bubble' si prefieres un estilo de burbuja)
});

var contenido = quill.root.innerHTML;
console.log(contenido);

function actualizarTexto() {
    var texto = quill.root.innerHTML; // Obtiene el contenido HTML del editor
    document.getElementById('texto-actualizado').innerHTML = texto; // Actualiza el contenido del elemento en la página
}



