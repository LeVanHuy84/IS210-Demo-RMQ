    const modal = document.getElementById("productModal");
    const openBtn = document.getElementById("openModalBtn");
    if (openBtn) {
        openBtn.onclick = () => modal.classList.remove("hidden");
    }
    document.querySelector(".close").onclick = () => modal.classList.add("hidden");

    async function uploadAndCreateProduct() {
        const file = document.getElementById("imageFile").files[0];
        const formData = new FormData();
        formData.append("file", file);

        const uploadResponse = await fetch('/api/v1/upload', {
            method: 'POST',
            body: formData
        });

        if (!uploadResponse.ok) return alert("Upload hình thất bại!");

        const imageUrl = await uploadResponse.text();

        const product = {
            name: document.getElementById("name").value,
            description: document.getElementById("description").value,
            price: parseFloat(document.getElementById("price").value),
            imgUrl: imageUrl
        };

        const response = await fetch('/api/v1/products', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(product)
        });

        if (response.ok) {
            alert("Tạo sản phẩm thành công!");
            modal.classList.add("hidden");
            loadProducts();
        } else {
            alert("Lỗi khi tạo sản phẩm");
        }
    }

    async function loadProducts() {
        const response = await fetch('/api/v1/products');
        const products = await response.json();

        const container = document.getElementById('product-list');
        container.innerHTML = '';

        const userRoleMeta = document.querySelector('meta[name="user-role"]');
        const userRoles = userRoleMeta?.content || "";

        const isCustomer = userRoles.includes("ROLE_CUSTOMER");

        products.forEach(p => {
            const card = document.createElement("div");
            card.className = "product-card";
            card.innerHTML = `
                <img src="${p.imgUrl}" alt="${p.name}">
                <div class="product-name">${p.name}</div>
                <div class="product-price">₫${p.price.toLocaleString()}</div>
                ${isCustomer ? '<button>Thêm vào giỏ hàng</button>' : ''}
            `;
            card.onclick = () => showProductDetail(p);
            container.appendChild(card);
        });
    }

    function showProductDetail(product) {
        document.getElementById("detailName").innerText = product.name;
        document.getElementById("detailImage").src = product.imgUrl;
        document.getElementById("detailDescription").innerText = product.description;
        document.getElementById("detailPrice").innerText = `₫${product.price.toLocaleString()}`;
        document.getElementById("detailModal").classList.remove("hidden");

        const addBtn = document.querySelector(".add-to-cart-btn");
        if (addBtn) {
            addBtn.onclick = () => addToCart(product);
        }
    }


    document.querySelector(".close-detail").onclick = () => {
        document.getElementById("detailModal").classList.add("hidden");
    };

    function getCart() {
        return JSON.parse(localStorage.getItem("cart")) || [];
    }

    function saveCart(cart) {
        localStorage.setItem("cart", JSON.stringify(cart));
    }

    function updateCartCount() {
        const cart = getCart();
        const count = cart.reduce((sum, item) => sum + item.quantity, 0);
        document.getElementById("cart-count").innerText = count;
    }

    function addToCart(product) {
        const cart = getCart();
        const existing = cart.find(item => item.id === product.id);
        if (existing) {
            existing.quantity += 1;
        } else {
            cart.push({
                id: product.id,
                name: product.name,
                price: product.price,
                imgUrl: product.imgUrl,
                quantity: 1
            });
        }
        saveCart(cart);
        updateCartCount();
        alert("Đã thêm vào giỏ hàng!");
    }


    function goToOrderPage() {
        window.location.href = '/web/orders-ajax';
    }

    // Gọi hàm khi load trang
    window.onload = () => {
        loadProducts();
        updateCartCount();  // 👈 cập nhật số lượng khi reload
    };

