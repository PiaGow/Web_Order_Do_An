document.addEventListener("DOMContentLoaded", function() {
    document.querySelectorAll("a").forEach(function(element) {
        element.addEventListener("click", function(event) {
            event.preventDefault();
            let url = this.getAttribute("href");

            fetch(url)
                .then(response => response.text())
                .then(html => {
                    document.getElementById("food-list").innerHTML = html;
                })
                .catch(error => console.warn("Error loading content: ", error));
        });
    });
});
