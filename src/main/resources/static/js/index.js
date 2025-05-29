    const modal = document.getElementById("productModal");
    document.getElementById("openModalBtn").onclick = () => modal.classList.remove("hidden");
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
    console.log(products);

    const container = document.getElementById('product-list');
    container.innerHTML = '';

    products.forEach(p => {
        container.innerHTML += `
            <div class="product-card">
                <img src="${p.imgUrl}" alt="${p.name}">
                <div class="product-name">${p.name}</div>
                <div class="product-price">₫${Number(p.price).toLocaleString()}</div>
                <button class="add-to-cart" data-id="${p.id}">Thêm vào giỏ</button>
            </div>
        `;
    });

    // 👉 Thêm sự kiện click sau khi render xong
    document.querySelectorAll('.add-to-cart').forEach(button => {
        button.addEventListener('click', () => {
            const productId = button.getAttribute('data-id');
            addToCart(productId);
        });
    });
}

    // Hàm thêm vào giỏ hàng (bạn cần tự định nghĩa)
    function addToCart(productId) {
        console.log('Đã thêm sản phẩm ID:', productId);
        // Gửi request hoặc lưu vào localStorage/cart list tại đây
    }


    const addToCart = (productId, productName, productPrice) => {
        try {
            let cart = JSON.parse(localStorage.getItem("cart")) || [];

            const existingItem = cart.find(item => item.productId === productId);

            if (existingItem) {
                existingItem.quantity += 1;
            } else {
                cart.push({ productId, productName, productPrice, quantity: 1 });
            }

            localStorage.setItem("cart", JSON.stringify(cart));
            updateCartCount();
            alert(`Đã thêm "${productName}" vào giỏ hàng.`);
        } catch (error) {
            console.error("Lỗi ở addToCart:", error);
        }
    };



    document.querySelector('.cart-icon').addEventListener('click', () => {
        window.location.href = '/web/orders-ajax';
    });

    // Gọi hàm khi load trang
    window.onload = () => {
        updateCartCount();  // nếu có
        loadProducts();     // ✅ gọi thêm dòng này
    };
