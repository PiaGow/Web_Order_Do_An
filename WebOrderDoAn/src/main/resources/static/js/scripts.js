document.addEventListener("DOMContentLoaded", function() {
    const links = document.querySelectorAll('.filters_menu a');

    links.forEach(link => {
        link.addEventListener('click', function(event) {
            event.preventDefault();

            // Remove active class from all links
            links.forEach(link => {
                link.classList.remove('active');
            });

            // Add active class to the clicked link
            this.classList.add('active');

            let url = this.getAttribute("href");
            fetch(url)
                .then(response => response.text())
                .then(html => {
                    document.getElementById("food-list").innerHTML = html;
                })
                .catch(error => console.error('Error fetching food list:', error));
        });
    });
});
