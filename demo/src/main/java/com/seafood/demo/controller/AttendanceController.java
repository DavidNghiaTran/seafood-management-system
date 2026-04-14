package com.seafood.demo.controller;

import com.seafood.demo.entity.Attendance;
import com.seafood.demo.entity.Role;
import com.seafood.demo.service.AttendanceService;
import com.seafood.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Controller xử lý các chức năng Điểm danh chấm công của nhân viên.
 * Cho phép xem lịch sử điểm danh, chấm công thủ công và chỉnh sửa thông tin.
 */
@Controller
@RequestMapping("/attendances")
public class AttendanceController {

    @Autowired
    private AttendanceService attendanceService;

    @Autowired
    private UserService userService;

    /**
     * Hiển thị danh sách lịch sử điểm danh của nhân viên.
     */
    @GetMapping
    public String listAttendances(Model model) {
        model.addAttribute("attendances", attendanceService.getAllAttendances());
        return "attendances/list";
    }

    /**
     * Hiển thị form tạo mới bản ghi điểm danh (Ví dụ: Thêm mới ngày công).
     */
    @GetMapping("/create")
    public String showCreateForm(Model model) {
        Attendance attendance = new Attendance();
        attendance.setWorkDate(LocalDate.now()); // Mặc định gán bằng ngày hiện tại
        
        model.addAttribute("attendance", attendance);
        // Tải danh sách tất cả các nhân viên (Role EMPLOYEE) để chọn người muốn điểm danh
        model.addAttribute("employees", userService.getUsersByRole(Role.EMPLOYEE));
        return "attendances/form";
    }

    /**
     * Nhận dữ liệu submit lên form và lưu trữ thông tin ngày công.
     */
    @PostMapping("/save")
    public String saveAttendance(@ModelAttribute("attendance") Attendance attendance) {
        // Tự động gán giờ check-in (hiện tại) khi tạo mới nếu có tình trạng Có mặt hoặc Đi trễ
        if (attendance.getId() == null && ("CÓ MẶT".equals(attendance.getStatus()) || "TRỄ".equals(attendance.getStatus()))) {
            attendance.setCheckInTime(LocalDateTime.now());
        }
        attendanceService.saveAttendance(attendance);
        return "redirect:/attendances";
    }

    /**
     * Hiển thị giao diện điều chỉnh thông tin 1 bản ghi điểm danh cụ thể.
     */
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable("id") Long id, Model model) {
        attendanceService.getAttendanceById(id).ifPresent(attendance -> {
            model.addAttribute("attendance", attendance);
            model.addAttribute("employees", userService.getUsersByRole(Role.EMPLOYEE));
        });
        return "attendances/form";
    }

    /**
     * Xoá lịch sử làm việc của một nhân viên dựa trên ID.
     */
    @GetMapping("/delete/{id}")
    public String deleteAttendance(@PathVariable("id") Long id) {
        attendanceService.deleteAttendance(id);
        return "redirect:/attendances";
    }
}
