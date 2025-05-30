document.getElementById("logo").addEventListener("click", function() {
    window.location.href = "/web/index"; // Điều hướng về trang /index
});

// Load cart from localStorage
document.addEventListener("DOMContentLoaded", function () {
    const cartItems = JSON.parse(localStorage.getItem("cart")) || [];
    const tbody = document.getElementById("cart-items");
    const totalPriceElement = document.getElementById("total-price");

    let total = 0;
    tbody.innerHTML = "";

    cartItems.forEach((item, index) => {
        const row = document.createElement("tr");

        const itemTotal = item.price * item.quantity;
        total += itemTotal;

        row.innerHTML = `
            <td>${item.name}</td>
            <td>${item.price.toLocaleString()}₫</td>
            <td>${item.quantity}</td>
            <td>${itemTotal.toLocaleString()}₫</td>
            <td><span class="delete-btn" data-index="${index}">Xóa</span></td>
        `;
        tbody.appendChild(row);
    });

    totalPriceElement.textContent = `${total.toLocaleString()}₫`;

    // Delete item from cart
    document.querySelectorAll('.delete-btn').forEach(btn => {
        btn.addEventListener('click', function () {
            const index = this.getAttribute("data-index");
            cartItems.splice(index, 1);
            localStorage.setItem("cart", JSON.stringify(cartItems));
            location.reload();
        });
    });
});

document.querySelector(".checkout-btn").addEventListener("click", function () {
    const cartItems = JSON.parse(localStorage.getItem("cart")) || [];

    if (cartItems.length === 0) {
        alert("Giỏ hàng của bạn đang trống!");
        return;
    }

    const orderItems = cartItems.map(item => ({
        productId: item.id,
        quantity: item.quantity
    }));

    const orderRequest = {
        orderItems
    };

    const token = document.querySelector('meta[name="_csrf"]').getAttribute('content');
    const header = document.querySelector('meta[name="_csrf_header"]').getAttribute('content');

    fetch("/api/v1/orders", {
    method: "POST",
    headers: {
        "Content-Type": "application/json",
        [header]: token // Thêm CSRF token
    },
    body: JSON.stringify(orderRequest)
    })

    .then(response => {
        if (!response.ok) {
            throw new Error("Đặt hàng thất bại.");
        }
        return response.json();
    })
    .then(data => {
        alert("Đặt hàng thành công!");
        localStorage.removeItem("cart");
        window.location.href = "/web/index"; // Hoặc redirect về trang lịch sử đơn hàng
    })
    .catch(error => {
        console.error(error);
        alert("Có lỗi xảy ra khi đặt hàng!");
    });
});
