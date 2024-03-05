document.addEventListener("DOMContentLoaded", function() {
    var slides = document.querySelectorAll('.slide');
    var currentSlide = 0;
    var slideInterval = setInterval(nextSlide, 2000);

    function nextSlide() {
        slides[currentSlide].style.display = 'none';
        currentSlide = (currentSlide + 1) % slides.length;
        slides[currentSlide].style.display = 'block';
    }
});