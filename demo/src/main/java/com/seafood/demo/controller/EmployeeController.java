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

@Controller
@RequestMapping("/employees")
public class EmployeeController {

    @Autowired
    private UserService userService;

    @GetMapping
    public String listEmployees(Model model) {
        List<User> employees = userService.getUsersByRole(Role.EMPLOYEE);
        model.addAttribute("employees", employees);
        return "employees/list";
    }

    @GetMapping("/create")
    public String showCreateForm(Model model) {
        User employee = new User();
        employee.setRole(Role.EMPLOYEE);
        model.addAttribute("employee", employee);
        return "employees/form";
    }

    @PostMapping("/save")
    public String saveEmployee(@ModelAttribute("employee") User employee) {
        employee.setRole(Role.EMPLOYEE); // Ensure role is always EMPLOYEE
        userService.saveUser(employee);
        return "redirect:/employees";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable("id") Long id, Model model) {
        Optional<User> employeeOptional = userService.getUserById(id);
        if (employeeOptional.isPresent() && employeeOptional.get().getRole() == Role.EMPLOYEE) {
            model.addAttribute("employee", employeeOptional.get());
            return "employees/form";
        }
        return "redirect:/employees";
    }

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
            if (employeeDetails.getPassword() != null && !employeeDetails.getPassword().isEmpty()) {
                existing.setPassword(employeeDetails.getPassword());
            }
            // Role remains EMPLOYEE
            userService.updateUser(id, existing);
        }
        return "redirect:/employees";
    }

    @GetMapping("/delete/{id}")
    public String deleteEmployee(@PathVariable("id") Long id) {
        Optional<User> employeeOptional = userService.getUserById(id);
        if (employeeOptional.isPresent() && employeeOptional.get().getRole() == Role.EMPLOYEE) {
            userService.deleteUser(id);
        }
        return "redirect:/employees";
    }

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
            List<User> employees = ExcelHelper.excelToEmployees(file.getInputStream());
            for (User employee : employees) {
                userService.saveUser(employee);
            }
            redirectAttributes.addFlashAttribute("successMessage", "Import thành công " + employees.size() + " nhân viên!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Lỗi import: " + e.getMessage());
        }
        return "redirect:/employees";
    }
}
