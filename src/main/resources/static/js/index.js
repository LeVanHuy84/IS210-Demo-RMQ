const modal = document.getElementById("productModal");
const openBtn = document.getElementById("openModalBtn");
const submitBtn = document.getElementById("submitBtn");
const productForm = document.getElementById("productForm");
const previewImage = document.getElementById("previewImage");
const modalTitle = document.getElementById("modalTitle");
const productIdInput = document.getElementById("productId");

let currentImgUrl = null;
let allProducts = [];

if (openBtn) {
    openBtn.onclick = () => {
        clearForm();
        modalTitle.innerText = "Thêm Sản Phẩm";
        submitBtn.textContent = "Tạo sản phẩm";
        modal.classList.remove("hidden");
    };
}

document.querySelector(".close").onclick = () => modal.classList.add("hidden");

productForm.addEventListener("submit", async function (event) {
    event.preventDefault();
    await handleSubmitProduct();
});

document.getElementById("imageFile").addEventListener("change", function () {
    const file = this.files[0];
    if (file) {
        const reader = new FileReader();
        reader.onload = function (e) {
            previewImage.src = e.target.result;
            previewImage.style.display = "block";
        };
        reader.readAsDataURL(file);
    } else {
        previewImage.src = "";
        previewImage.style.display = "none";
    }
});

async function handleSubmitProduct() {
    const token = document.querySelector('meta[name="_csrf"]').getAttribute('content');
    const header = document.querySelector('meta[name="_csrf_header"]').getAttribute('content');

    let imageUrl = null;
    const file = document.getElementById("imageFile").files[0];
    if (file) {
        const formData = new FormData();
        formData.append("file", file);

        const uploadResponse = await fetch('/api/v1/upload', {
            method: 'POST',
            headers: { [header]: token },
            body: formData
        });

        if (!uploadResponse.ok) {
            alert("Upload hình thất bại!");
            return;
        }
        imageUrl = await uploadResponse.text();
    }

    const product = {
        name: document.getElementById("name").value,
        description: document.getElementById("description").value,
        price: parseFloat(document.getElementById("price").value),
        quantity: parseInt(document.getElementById("quantity").value),
        imgUrl: imageUrl || currentImgUrl
    };

    const productId = productIdInput.value;
    const url = productId ? `/api/v1/products/${productId}` : '/api/v1/products';
    const method = productId ? 'PUT' : 'POST';

    const response = await fetch(url, {
        method,
        headers: {
            "Content-Type": "application/json",
            [header]: token
        },
        body: JSON.stringify(product)
    });

    if (response.ok) {
        alert(productId ? "Cập nhật thành công!" : "Tạo sản phẩm thành công!");
        modal.classList.add("hidden");
        loadProducts();
        clearForm();
    } else {
        alert("Đã xảy ra lỗi khi lưu sản phẩm.");
    }
}

function openEditModal(product) {
    document.getElementById("name").value = product.name;
    document.getElementById("description").value = product.description;
    document.getElementById("price").value = product.price;
    document.getElementById("quantity").value = product.quantity;
    productIdInput.value = product.id;
    currentImgUrl = product.imgUrl;

    if (product.imgUrl) {
        previewImage.src = product.imgUrl;
        previewImage.style.display = "block";
    } else {
        previewImage.src = "";
        previewImage.style.display = "none";
    }

    modalTitle.innerText = "Cập nhật Sản Phẩm";
    submitBtn.textContent = "Cập nhật sản phẩm";
    modal.classList.remove("hidden");
}

async function loadProducts() {
    const response = await fetch('/api/v1/products');
    allProducts = await response.json();
    renderProducts(allProducts);
}

function renderProducts(products) {
    const container = document.getElementById('product-list');
    container.innerHTML = '';

    const userRoleMeta = document.querySelector('meta[name="user-role"]');
    const userRoles = userRoleMeta?.content || "";
    const isAdmin = userRoles.includes("ROLE_ADMIN");
    const isCustomer = userRoles.includes("ROLE_CUSTOMER");
    products.forEach(product => {
        const productCard = document.createElement("div");
        productCard.classList.add("product-card");

        productCard.innerHTML = `
            <img src="${product.imgUrl}" alt="${product.name}" class="product-img" />
            <div class="product-name">${product.name}</div>
            <div class="product-price">${Number(product.price).toLocaleString("vi-VN")}₫</div>
            ${isAdmin ? `
                <div class="action-buttons">
                    <button class="edit-btn">Sửa</button>
                    <button class="delete-btn">Xoá</button>
                </div>
            ` : ''}
            ${isCustomer
                ? (product.quantity > 0
                    ? '<button class="add-to-cart-btn">Thêm vào giỏ hàng</button>'
                    : '<span class="out-of-stock">Hết hàng</span>')
                : ''
            }
        `;

        productCard.onclick = () => openDetailModal(product);

        // Sửa sản phẩm
        if (isAdmin) {
            
            productCard.querySelector(".edit-btn").onclick = (event) => {
                event.stopPropagation();
                openEditModal(product);
            }
            productCard.querySelector(".delete-btn").onclick = (event) => {
                event.stopPropagation();
                deleteProduct(product.id)
            }
        }

        const btn = productCard.querySelector(".add-to-cart-btn");
        if (isCustomer && btn) {
            
            btn.addEventListener("click", function (event) {
                event.stopPropagation();
                addToCart(product);
            });
        }

        container.appendChild(productCard);
    });
}

async function deleteProduct(productId) {
    if (!confirm("Bạn có chắc chắn muốn xóa sản phẩm này?")) return;

    const token = document.querySelector('meta[name="_csrf"]').getAttribute('content');
    const header = document.querySelector('meta[name="_csrf_header"]').getAttribute('content');

    const response = await fetch(`/api/v1/products/${productId}`, {
        method: "DELETE",
        headers: { [header]: token }
    });

    if (response.ok) {
        alert("Xóa thành công!");
        loadProducts();
    } else {
        alert("Lỗi khi xóa sản phẩm!");
    }
}

function clearForm() {
    productForm.reset();
    productIdInput.value = '';
    previewImage.src = '';
    previewImage.style.display = 'none';
    currentImgUrl = null;
}

// Modal chi tiết sản phẩm
const detailModal = document.getElementById("detailModal");
const closeDetailBtn = document.querySelector(".close-detail");

closeDetailBtn.onclick = () => detailModal.classList.add("hidden");

function openDetailModal(product) {
    document.getElementById("detailImage").src = product.imgUrl;
    document.getElementById("detailName").innerText = product.name;
    document.getElementById("detailDescription").innerText = product.description;
    document.getElementById("detailPrice").innerText = `${product.price.toLocaleString('vi-VN')}₫`;
    document.getElementById("detailQuantity").innerText = `Kho: ${product.quantity}`;

    const addToCartBtn = document.getElementById("detailAddToCartBtn");
    if (product.quantity > 0) {
        addToCartBtn.innerText = "Thêm vào giỏ";
        addToCartBtn.disabled = false;
        addToCartBtn.style.display = "inline-block";
        addToCartBtn.onclick = () => addToCart(product);
    } else {
        addToCartBtn.innerText = "Hết hàng";
        addToCartBtn.disabled = true;
        addToCartBtn.style.display = "inline-block";
        addToCartBtn.onclick = null;
    }

    detailModal.classList.remove("hidden");
}

function addToCart(product) {
    let cart = JSON.parse(localStorage.getItem("cart")) || [];
    const existing = cart.find(item => item.id === product.id);
    if (existing) {
        existing.quantity += 1;
    } else {
        cart.push({ ...product, quantity: 1 });
    }
    localStorage.setItem("cart", JSON.stringify(cart));
    updateCartCount();
    alert("Đã thêm vào giỏ!");
}

function updateCartCount() {
    const cart = JSON.parse(localStorage.getItem("cart")) || [];
    const count = cart.length; // ✅ chỉ cần đếm số item trong mảng (mỗi item là 1 loại sản phẩm)
    document.getElementById("cart-count").innerText = count;
    if (countSpan) countSpan.innerText = count;
}

function goToOrderPage() {
    window.location.href = "/web/carts";
}

// Tìm kiếm sản phẩm
document.getElementById("searchBox")?.addEventListener("input", function () {
    const keyword = this.value.toLowerCase();
    const filtered = allProducts.filter(p =>
        p.name.toLowerCase().includes(keyword) ||
        p.description.toLowerCase().includes(keyword)
    );
    renderProducts(filtered);
});

// Khởi tạo
window.onload = () => {
    loadProducts();
    updateCartCount();
};
