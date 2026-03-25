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

        // Kiểm tra quyền truy cập theo đường dẫn (ví dụ đơn giản)
        if (uri.startsWith("/users")) {
            if (user.getRole() != Role.ADMIN) {
                // Không có quyền thì chuyển ra trang chủ kèm thông báo lỗi hoặc trang 403
                response.sendRedirect("/?error=access_denied");
                return false;
            }
        }

        // Ví dụ: Kế toán và Admin được vào /orders
        if (uri.startsWith("/orders") || uri.startsWith("/invoices")) {
             if (user.getRole() != Role.ADMIN && user.getRole() != Role.ACCOUNTANT) {
                 response.sendRedirect("/?error=access_denied");
                 return false;
             }
        }

        // Ví dụ: Nhân viên và Admin được vào /shifts
        if (uri.startsWith("/shifts") || uri.startsWith("/profile")) {
             if (user.getRole() != Role.ADMIN && user.getRole() != Role.EMPLOYEE) {
                 response.sendRedirect("/?error=access_denied");
                 return false;
             }
        }

        // Nếu hợp lệ thì cho phép handle tiếp
        return true;
    }
}
