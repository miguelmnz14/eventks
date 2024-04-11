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
    theme: 'snow',
    modules: {
        toolbar: [
            [{ 'header': [1, 2, false] }],
            ['bold', 'italic', 'underline', 'strike'],
            ['image']
        ]
    }
});

var contenido = quill.root.innerHTML;
console.log(contenido);

function addText() {
    var textEditor = quill.root.innerHTML;
    var textInserted = document.getElementById("text-inserted");


    var newDiv = document.createElement("div");
    newDiv.innerHTML = textEditor;
    newDiv.classList.add("text-inserted-div");
    textInserted.appendChild(newDiv);
}




