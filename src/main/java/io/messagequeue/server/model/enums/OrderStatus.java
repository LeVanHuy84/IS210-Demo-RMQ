package io.messagequeue.server.model.enums;

public enum OrderStatus {
    PENDING,     // 🌱 Mới tạo, chưa xử lý gì cả
    PROCESSING,  // ⚙️ Đang xử lý (trừ kho, tạo hóa đơn,...)
    COMPLETED,   // ✅ Đã hoàn tất
    CANCELLED,   // ❌ Đã bị hủy
    FAILED       // ⛔ Gặp lỗi (ví dụ: thanh toán thất bại)
}
