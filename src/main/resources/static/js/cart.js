function formatCurrency(number) {
    return number.toLocaleString('vi-VN') + '₫';
}

function renderCart() {
    const cart = JSON.parse(localStorage.getItem('cart') || '[]');
    const tbody = document.getElementById('cart-items');
    const totalPriceEl = document.getElementById('total-price');

    tbody.innerHTML = '';
    let total = 0;

    cart.forEach((item, index) => {
        const subtotal = item.price * item.quantity;
        total += subtotal;

        const tr = document.createElement('tr');
        tr.innerHTML = `
            <td>
                <img src="${item.image}" alt="${item.name}">
                <div>${item.name}</div>
            </td>
            <td>${formatCurrency(item.price)}</td>
            <td>
                <input type="number" value="${item.quantity}" min="1" data-index="${index}" class="qty-input">
            </td>
            <td>${formatCurrency(subtotal)}</td>
            <td><button onclick="removeItem(${index})">Xoá</button></td>
        `;
        tbody.appendChild(tr);
    });

    totalPriceEl.textContent = formatCurrency(total);

    document.querySelectorAll('.qty-input').forEach(input => {
        input.addEventListener('change', (e) => {
            const idx = e.target.dataset.index;
            cart[idx].quantity = parseInt(e.target.value);
            localStorage.setItem('cart', JSON.stringify(cart));
            renderCart();
        });
    });
}

function removeItem(index) {
    const cart = JSON.parse(localStorage.getItem('cart') || '[]');
    cart.splice(index, 1);
    localStorage.setItem('cart', JSON.stringify(cart));
    renderCart();
}

document.addEventListener('DOMContentLoaded', renderCart);
