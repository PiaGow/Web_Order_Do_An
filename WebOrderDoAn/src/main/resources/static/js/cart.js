$(document).ready(function () {
    $('.quantity').change(function () {
        let quantity = $(this).val();
        let id = $(this).attr('data-id');
        $.ajax({url: '/cart/updateCart/' + id + '/' + quantity,
            type: 'GET',

//            success: function () {

//            }
        });
        location.reload();
    });
});
//document.addEventListener('DOMContentLoaded', function () {
//    const quantityInputs = document.querySelectorAll('.quantity');
//
//    quantityInputs.forEach(input => {
//        input.addEventListener('change', function () {
//            const bookId = this.getAttribute('data-id');
//            const newQuantity = this.value;
//
//            // Gửi yêu cầu AJAX đến máy chủ để cập nhật số lượng
//            fetch(`/cart/updateCart/${bookId}/${newQuantity}`, {
//                method: 'GET',
//            })
//                .then(response => response.json())
//                .then(data => {
////                    // Cập nhật lại tổng giá tiền và tổng số lượng
////                    document.querySelector('#totalPrice').innerText = data.totalPrice;
////                    document.querySelector('#totalQuantity').innerText = data.totalQuantity;
////
////                    // Cập nhật lại giá tiền của từng sản phẩm
////                    const totalCell = this.parentElement.parentElement.nextElementSibling;
////                    totalCell.innerText = (data.itemPrice * newQuantity).toFixed(2);
//                     // Làm mới trang để cập nhật lại giá tiền và số lượng
//                      window.location.reload();
//                })
//                .catch(error => console.error('Error:', error));
//        });
//    });
//});
