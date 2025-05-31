document.getElementById("logo").addEventListener("click", function() {
    window.location.href = "/web/index"; // Điều hướng về trang /index
});

// Load cart from localStorage
function renderCart() {
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
            <td class="product-info">
                <img src="${item.imgUrl}" alt="${item.name}" class="product-thumb">
                <span>${item.name}</span>
            </td>
            <td>${item.price.toLocaleString()}₫</td>
            <td>
                <button class="qty-btn" data-action="decrease" data-index="${index}">-</button>
                <span class="qty-display">${item.quantity}</span>
                <button class="qty-btn" data-action="increase" data-index="${index}">+</button>
            </td>
            <td>${itemTotal.toLocaleString()}₫</td>
            <td><span class="delete-btn" data-index="${index}">Xóa</span></td>
        `;
        tbody.appendChild(row);
    });

    totalPriceElement.textContent = `${total.toLocaleString()}₫`;

    // Gắn lại các sự kiện cho nút
    attachEventListeners();
}

function attachEventListeners() {
    // Tăng/giảm số lượng
    document.querySelectorAll('.qty-btn').forEach(button => {
        button.addEventListener('click', function () {
            const action = this.getAttribute('data-action');
            const index = this.getAttribute('data-index');
            const cartItems = JSON.parse(localStorage.getItem("cart")) || [];

            if (action === 'increase') {
                cartItems[index].quantity += 1;
            } else if (action === 'decrease' && cartItems[index].quantity > 1) {
                cartItems[index].quantity -= 1;
            }

            localStorage.setItem("cart", JSON.stringify(cartItems));
            renderCart(); // Thay vì reload
        });
    });

    // Xóa sản phẩm
    document.querySelectorAll('.delete-btn').forEach(btn => {
        btn.addEventListener('click', function () {
            const index = this.getAttribute("data-index");
            const cartItems = JSON.parse(localStorage.getItem("cart")) || [];

            cartItems.splice(index, 1);
            localStorage.setItem("cart", JSON.stringify(cartItems));
            renderCart(); // Thay vì reload
        });
    });
}

// khởi chạy
document.addEventListener("DOMContentLoaded", function () {
    renderCart(); // Tự động vẽ giỏ hàng lần đầu
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
