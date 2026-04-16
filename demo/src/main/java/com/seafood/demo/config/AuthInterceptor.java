package com.seafood.demo.config;

import com.seafood.demo.entity.Role;
import com.seafood.demo.entity.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class AuthInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String uri = request.getRequestURI();

        // Bỏ qua các đường dẫn tĩnh và trang đăng nhập
        if (uri.startsWith("/css/") || uri.startsWith("/js/") || uri.startsWith("/images/") || uri.equals("/login")) {
            return true;
        }

        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("loggedInUser");

        if (user == null) {
            // Chưa đăng nhập thì chuyển hướng sang trang login
            response.sendRedirect("/login");
            return false;
        }

        // Nếu user có Role là ADMIN, luôn cho phép
        if (user.getRole() == Role.ADMIN) {
            return true;
        }

        // Quyền dùng chung cho mọi tài khoản đã đăng nhập (Ví dụ: Trang chủ / trang cá nhân)
        if (uri.equals("/") || uri.startsWith("/profile") || uri.startsWith("/logout") || uri.startsWith("/api/")) {
            return true;
        }

        // Quyền của ACCOUNTANT (Kế Toán)
        if (user.getRole() == Role.ACCOUNTANT) {
            // Kế toán chỉ được thao tác với Đơn Hàng và Báo cáo doanh thu
            if (uri.startsWith("/orders") || uri.startsWith("/reports")) {
                return true;
            }
            // Không có quyền thì chặn lại
            response.sendRedirect("/?error=access_denied");
            return false;
        }

        // Quyền của EMPLOYEE (Nhân Viên) - Phục vụ/Đầu bếp v.v.
        if (user.getRole() == Role.EMPLOYEE) {
            // Nhân viên chỉ được xem và sửa thông tin của mình ở /profile (đã cho phép ở trên)
            // Mọi truy cập vào chức năng quán (đơn hàng, người dùng, bàn...) đều bị cấm
            response.sendRedirect("/?error=access_denied");
            return false;
        }

        // Thu gọn các trường hợp không kiểm soát được
        response.sendRedirect("/?error=access_denied");
        return false;
    }
}
