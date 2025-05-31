document.getElementById("logo").addEventListener("click", function() {
    window.location.href = "/web/index"; // Điều hướng về trang /index
});

document.addEventListener("DOMContentLoaded", () => {
    const roleMeta = document.querySelector('meta[name="user-role"]');
    const roles = roleMeta?.content || "";
    const isAdmin = roles.includes("ADMIN");

    const apiUrl = isAdmin ? "/api/v1/orders" : "/api/v1/orders/me";

    fetch(apiUrl)
        .then(res => res.json())
        .then(data => renderOrders(data))
        .catch(err => console.error("Lỗi khi lấy đơn hàng:", err));
});

function renderOrders(orders) {
    const orderList = document.getElementById("order-list");
    if (!orders.length) {
        orderList.innerHTML = "<p>Không có đơn hàng nào.</p>";
        return;
    }

    orderList.innerHTML = orders.map(order => {
        const itemsHtml = order.orderItems.map(item => `
            <div class="order-item">
                <img src="${item.imgUrl}" alt="${item.productName}" class="order-img">
                <div>
                    <p><strong>${item.productName}</strong></p>
                    <p>Số lượng: ${item.quantity}</p>
                    <p>Giá: ${item.price} đ</p>
                </div>
            </div>
        `).join("");

        return `
            <div class="order-card">
                <h3>Đơn #${order.id}</h3>
                <p>Trạng thái: <strong>${order.status}</strong></p>
                <p>Ngày tạo: ${new Date(order.createdAt).toLocaleString()}</p>
                ${itemsHtml}
            </div>
        `;
    }).join("");
}
