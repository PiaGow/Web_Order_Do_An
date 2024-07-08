// scripts.js
$(document).ready(function () {
    // Load categories on page load
    loadCategories();

    // Function to load categories
    function loadCategories() {
        $.ajax({
            url: '/api/v1/categories',
            type: 'GET',
            dataType: 'json',
            success: function (data) {
                var categoriesHtml = '';
                categoriesHtml += '<li class="active" data-filter="*">All</li>';
                $.each(data, function (index, category) {
                    categoriesHtml += '<li data-filter=".' + category.name.toLowerCase() + '">' + category.name + '</li>';
                });
                $('.filters_menu').html(categoriesHtml);

                // Add click event handler for filter menu items
                $('.filters_menu li').on('click', function () {
                    var filter = $(this).attr('data-filter');
                    $('.filters_menu li').removeClass('active');
                    $(this).addClass('active');
                    loadFoods(filter);
                });

                // Load all foods initially
                loadFoods('*');
            },
            error: function (xhr, status, error) {
                console.error('Error loading categories:', status, error);
            }
        });
    }

    // Function to load foods based on filter
    function loadFoods(filter) {
        $.ajax({
            url: '/api/v1/foods',
            type: 'GET',
            dataType: 'json',
            data: { category: filter },
            success: function (data) {
                var foodsHtml = '';
                $.each(data, function (index, food) {
                    foodsHtml += '<div class="col-sm-6 col-lg-4 all ' + food.category.toLowerCase() + '">' +
                        '<div class="box">' +
                        '<div class="img-box">' +
                        '<img src="' + food.imageUrl + '" alt="' + food.name + '">' +
                        '</div>' +
                        '<div class="detail-box">' +
                        '<h5>' + food.name + '</h5>' +
                        '<p>' + food.description + '</p>' +
                        '<div class="options">' +
                        '<h6>$' + food.price + '</h6>' +
                        '<a href="#"><svg>...</svg></a>' +
                        '</div>' +
                        '</div>' +
                        '</div>' +
                        '</div>';
                });
                $('.filters-content .grid').html(foodsHtml);
            },
            error: function (xhr, status, error) {
                console.error('Error loading foods:', status, error);
            }
        });
    }
});
