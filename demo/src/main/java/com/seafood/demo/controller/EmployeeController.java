package com.seafood.demo.controller;

import com.seafood.demo.entity.Role;
import com.seafood.demo.entity.User;
import com.seafood.demo.service.UserService;
import com.seafood.demo.util.ExcelHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

/**
 * Controller xử lý các chức năng quản lý riêng biệt cho Nhân viên (Role = EMPLOYEE).
 * Chức năng tương tự UserController nhưng chỉ thao tác với record có quyền Nhân viên.
 */
@Controller
@RequestMapping("/employees")
public class EmployeeController {

    @Autowired
    private UserService userService;

    /**
     * Hiển thị danh sách nhân viên.
     * Chỉ lấy những user có role là EMPLOYEE.
     */
    @GetMapping
    public String listEmployees(Model model) {
        List<User> employees = userService.getUsersByRole(Role.EMPLOYEE);
        model.addAttribute("employees", employees);
        return "employees/list";
    }

    /**
     * Hiển thị form tạo mới nhân viên.
     * Mặc định set role là EMPLOYEE.
     */
    @GetMapping("/create")
    public String showCreateForm(Model model) {
        User employee = new User();
        employee.setRole(Role.EMPLOYEE);
        model.addAttribute("employee", employee);
        return "employees/form";
    }

    /**
     * Lưu thông tin Nhân viên mới từ form.
     */
    @PostMapping("/save")
    public String saveEmployee(@ModelAttribute("employee") User employee) {
        employee.setRole(Role.EMPLOYEE); // Đảm bảo role luôn được gán là EMPLOYEE
        userService.saveUser(employee);
        return "redirect:/employees";
    }

    /**
     * Hiển thị form chỉnh sửa thông tin của một nhân viên đã tồn tại.
     * Kiểm tra đảm bảo tài khoản đang sửa có role là EMPLOYEE.
     */
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable("id") Long id, Model model) {
        Optional<User> employeeOptional = userService.getUserById(id);
        if (employeeOptional.isPresent() && employeeOptional.get().getRole() == Role.EMPLOYEE) {
            model.addAttribute("employee", employeeOptional.get());
            return "employees/form";
        }
        return "redirect:/employees";
    }

    /**
     * Cập nhật thông tin chi tiết nhân viên vào hệ thống.
     * Chỉ cập nhật nếu user tồn tại và là EMPLOYEE.
     */
    @PostMapping("/update/{id}")
    public String updateEmployee(@PathVariable("id") Long id, @ModelAttribute("employee") User employeeDetails) {
        Optional<User> existingEmployeeOpt = userService.getUserById(id);
        if (existingEmployeeOpt.isPresent() && existingEmployeeOpt.get().getRole() == Role.EMPLOYEE) {
            User existing = existingEmployeeOpt.get();
            existing.setFullName(employeeDetails.getFullName());
            existing.setUsername(employeeDetails.getUsername());
            existing.setPhone(employeeDetails.getPhone());
            existing.setAddress(employeeDetails.getAddress());
            existing.setPosition(employeeDetails.getPosition());
            
            // Xử lý đổi mật khẩu (nếu có nhập)
            if (employeeDetails.getPassword() != null && !employeeDetails.getPassword().isEmpty()) {
                existing.setPassword(employeeDetails.getPassword());
            }
            // Role luôn được giữ nguyên là EMPLOYEE
            userService.updateUser(id, existing);
        }
        return "redirect:/employees";
    }

    /**
     * Xóa nhân viên theo ID.
     */
    @GetMapping("/delete/{id}")
    public String deleteEmployee(@PathVariable("id") Long id) {
        Optional<User> employeeOptional = userService.getUserById(id);
        // Kiểm tra an toàn trước khi xóa (phải là role EMPLOYEE để tránh xóa nhầm ADMIN)
        if (employeeOptional.isPresent() && employeeOptional.get().getRole() == Role.EMPLOYEE) {
            userService.deleteUser(id);
        }
        return "redirect:/employees";
    }

    /**
     * Chức năng Import danh sách nhân viên hàng loạt từ tệp Excel.
     */
    @PostMapping("/import")
    public String importExcel(@RequestParam("file") MultipartFile file, RedirectAttributes redirectAttributes) {
        if (file.isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Vui lòng chọn file Excel để import!");
            return "redirect:/employees";
        }
        if (!ExcelHelper.hasExcelFormat(file)) {
            redirectAttributes.addFlashAttribute("errorMessage", "Chỉ hỗ trợ file định dạng .xlsx!");
            return "redirect:/employees";
        }
        try {
            // Lấy danh sách nhân viên từ file Excel thông qua tiện ích ExcelHelper
            List<User> employees = ExcelHelper.excelToEmployees(file.getInputStream());
            int imported = 0;
            int skipped = 0;
            
            // Lấy danh sách nhân viên hiện có để check trùng lặp username
            List<User> existingEmployees = userService.getUsersByRole(Role.EMPLOYEE);
            
            for (User employee : employees) {
                boolean exists = existingEmployees.stream().anyMatch(e -> e.getUsername().equalsIgnoreCase(employee.getUsername()));
                if (exists) {
                    skipped++;
                    continue;
                }
                userService.saveUser(employee);
                imported++;
            }
            
            String msg = "Import thành công " + imported + " nhân viên!";
            if (skipped > 0) {
                msg += " (Bỏ qua " + skipped + " nhân viên đã tồn tại tài khoản)";
            }
            redirectAttributes.addFlashAttribute("successMessage", msg);
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Lỗi import: " + e.getMessage());
        }
        return "redirect:/employees";
    }
}
