# Hướng dẫn tạo file Excel để Import

## 1. Danh Mục (Categories)
| Tên Danh Mục | Mô Tả |
|---|---|
| Hải Sản Nướng | Các món hải sản nướng BBQ |
| Hải Sản Hấp | Các món hải sản hấp gia vị |

## 2. Món Ăn (Dishes)
| Tên Món | Giá | Tên Danh Mục | Mô Tả | Trạng Thái |
|---|---|---|---|---|
| Tôm hùm nướng | 1250000 | Hải Sản Nướng | Tôm hùm nướng bơ tỏi | true |
| Cua hấp | 850000 | Hải Sản Hấp | Cua hấp bia | true |

**Lưu ý:** Tên Danh Mục phải khớp chính xác với danh mục đã có trong hệ thống.

## 3. Bàn (Tables)
| Số Bàn | Sức Chứa | Trạng Thái |
|---|---|---|
| Bàn 01 | 4 | true |
| Bàn 02 | 6 | true |

## 4. Khách Hàng (Customers)
| Họ Tên | Số Điện Thoại | Địa Chỉ |
|---|---|---|
| Nguyễn Văn A | 0901234567 | Hà Nội |
| Trần Thị B | 0912345678 | TP.HCM |

## 5. Nhân Viên (Employees)
| Họ Tên | Username | Password | SĐT | Địa Chỉ | Vị Trí |
|---|---|---|---|---|---|
| Lê Văn C | levanc | 123456 | 0923456789 | Đà Nẵng | Phục vụ |
| Phạm Thị D | phamthid | 123456 | 0934567890 | Hà Nội | Đầu bếp |

## Lưu ý chung
- File phải có định dạng `.xlsx` (Excel 2007+)
- Hàng đầu tiên là **tiêu đề** (sẽ bị bỏ qua khi import)
- Dữ liệu bắt đầu từ hàng thứ 2
- Các ô trống ở cột bắt buộc sẽ khiến dòng đó bị bỏ qua
