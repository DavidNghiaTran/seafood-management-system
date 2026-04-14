package com.seafood.demo.controller;

import com.seafood.demo.entity.Customer;
import com.seafood.demo.service.CustomerService;
import com.seafood.demo.util.ExcelHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

/**
 * Controller xử lý các chức năng liên quan đến Khách hàng thân thiết.
 * Bao gồm: Xem danh sách, Thêm mới, Sửa, Xóa và Import danh sách khách hàng từ Excel.
 */
@Controller
@RequestMapping("/customers")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    /**
     * Hiển thị danh sách khách hàng thân thiết.
     */
    @GetMapping
    public String listCustomers(Model model) {
        model.addAttribute("customers", customerService.getAllCustomers());
        return "customers/list";
    }

    /**
     * Hiển thị giao diện form thêm khách hàng mới.
     */
    @GetMapping("/create")
    public String showCreateForm(Model model) {
        model.addAttribute("customer", new Customer());
        return "customers/form";
    }

    /**
     * Thu thập dữ liệu từ form và lưu thông tin khách hàng vào cơ sở dữ liệu.
     */
    @PostMapping("/save")
    public String saveCustomer(@ModelAttribute("customer") Customer customer) {
        customerService.saveCustomer(customer);
        return "redirect:/customers";
    }

    /**
     * Hiển thị form thay đổi thông tin của một khách hàng đã tồn tại.
     * 
     * @param id ID của khách hàng
     */
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable("id") Long id, Model model) {
        Customer customer = customerService.getCustomerById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid customer Id:" + id));
        model.addAttribute("customer", customer);
        return "customers/form";
    }

    /**
     * Xóa thông tin khách hàng khỏi hệ thống dựa trên ID.
     */
    @GetMapping("/delete/{id}")
    public String deleteCustomer(@PathVariable("id") Long id) {
        customerService.deleteCustomer(id);
        return "redirect:/customers";
    }

    /**
     * Xử lý chức năng Import hàng loạt khách hàng thân thiết từ tệp Excel.
     * 
     * @param file File Excel được tải lên
     * @param redirectAttributes Dùng để trả về thông báo trạng thái import
     */
    @PostMapping("/import")
    public String importExcel(@RequestParam("file") MultipartFile file, RedirectAttributes redirectAttributes) {
        // 1. Kiểm tra xem người dùng có tải lên file hay không
        if (file.isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Vui lòng chọn file Excel để import!");
            return "redirect:/customers";
        }
        // 2. Validate đúng định dạng là file .xlsx
        if (!ExcelHelper.hasExcelFormat(file)) {
            redirectAttributes.addFlashAttribute("errorMessage", "Chỉ hỗ trợ file định dạng .xlsx!");
            return "redirect:/customers";
        }
        
        try {
            // 3. Sử dụng ExcelHelper để chuyển đổi dữ liệu từ file Excel thành danh sách đối tượng Customer
            List<Customer> customers = ExcelHelper.excelToCustomers(file.getInputStream());
            int imported = 0;
            int skipped = 0;
            
            // 4. Lặp qua danh sách và tiến hành lưu từng Khách hàng
            for (Customer customer : customers) {
                // Bỏ qua khách hàng đã tồn tại trong hệ thống (Kiểm tra tránh trùng lặp bằng số điện thoại)
                if (customerService.existsByPhone(customer.getPhone())) {
                    skipped++;
                    continue;
                }
                customerService.saveCustomer(customer);
                imported++; // Tăng biến đếm thành công
            }
            
            // 5. Cấu hình câu thông báo hoàn tất quá trình import
            String msg = "Import thành công " + imported + " khách hàng thân thiết!";
            if (skipped > 0) {
                msg += " (Bỏ qua " + skipped + " khách hàng đã tồn tại)";
            }
            redirectAttributes.addFlashAttribute("successMessage", msg);
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Lỗi import: " + e.getMessage());
        }
        return "redirect:/customers";
    }
}
