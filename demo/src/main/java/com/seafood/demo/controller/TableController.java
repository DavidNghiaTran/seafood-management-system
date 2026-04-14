package com.seafood.demo.controller;

import com.seafood.demo.entity.RestaurantTable;
import com.seafood.demo.service.TableService;
import com.seafood.demo.util.ExcelHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

/**
 * Controller xử lý các chức năng Quản lý Bàn nhà hàng.
 * Cung cấp khả năng hiển thị danh sách, thêm, sửa, xóa và import bàn từ dữ liệu Excel.
 */
@Controller
@RequestMapping("/tables")
public class TableController {

    @Autowired
    private TableService tableService;

    /**
     * Hiển thị giao diện danh sách tất cả các bàn trong hệ thống.
     */
    @GetMapping
    public String listTables(Model model) {
        model.addAttribute("tables", tableService.getAllTables());
        return "tables/list";
    }

    /**
     * Hiển thị form để thêm một số bàn mới.
     */
    @GetMapping("/create")
    public String showCreateForm(Model model) {
        model.addAttribute("table", new RestaurantTable());
        return "tables/form";
    }

    /**
     * Xử lý lưu thông tin bàn mới từ form.
     */
    @PostMapping("/save")
    public String saveTable(@ModelAttribute("table") RestaurantTable table) {
        tableService.saveTable(table);
        return "redirect:/tables";
    }

    /**
     * Hiển thị form thay đổi thông tin của một bàn.
     * 
     * @param id ID của bàn cần sửa
     */
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable("id") Long id, Model model) {
        RestaurantTable table = tableService.getTableById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid table Id:" + id));
        model.addAttribute("table", table);
        return "tables/form";
    }

    /**
     * Xóa một bàn dựa trên ID.
     */
    @GetMapping("/delete/{id}")
    public String deleteTable(@PathVariable("id") Long id) {
        tableService.deleteTable(id);
        return "redirect:/tables";
    }

    /**
     * Xử lý chức năng Import hàng loạt bàn từ tệp (file) Excel (.xlsx).
     * 
     * @param file File Excel được upload từ giao diện web
     * @param redirectAttributes Dùng để truyền thông báo trạng thái import (thành công/lỗi/bỏ qua bao nhiêu bàn)
     */
    @PostMapping("/import")
    public String importExcel(@RequestParam("file") MultipartFile file, RedirectAttributes redirectAttributes) {
        // 1. Kiểm tra xem file có bị rỗng hay không
        if (file.isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Vui lòng chọn file Excel để import!");
            return "redirect:/tables";
        }
        // 2. Validate định dạng extension của file (chỉ nhận .xlsx)
        if (!ExcelHelper.hasExcelFormat(file)) {
            redirectAttributes.addFlashAttribute("errorMessage", "Chỉ hỗ trợ file định dạng .xlsx!");
            return "redirect:/tables";
        }
        
        try {
            // 3. Đọc dữ liệu từ file ra danh sách đối tượng bàn
            List<RestaurantTable> tables = ExcelHelper.excelToTables(file.getInputStream());
            int imported = 0;
            int skipped = 0;
            
            // 4. Lưu từng bàn vào cơ sở dữ liệu
            for (RestaurantTable table : tables) {
                // Check trùng số bàn, nếu bàn (vd: Bàn 01) đã tồn tại thì bỏ qua không lưu đè
                RestaurantTable existing = tableService.findByTableNumber(table.getTableNumber());
                if (existing != null) {
                    skipped++;
                    continue;
                }
                tableService.saveTable(table);
                imported++; // Đếm số lượng import thành công
            }
            
            // 5. Cấu hình câu thông báo trả về
            String msg = "Import thành công " + imported + " bàn!";
            if (skipped > 0) {
                msg += " (Bỏ qua " + skipped + " bàn đã tồn tại)";
            }
            redirectAttributes.addFlashAttribute("successMessage", msg);
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Lỗi import: " + e.getMessage());
        }
        return "redirect:/tables";
    }

    /**
     * Tiện ích: Xoá tất cả bàn (Hỗ trợ người dùng xoá list dữ liệu lỗi khi import nhầm).
     */
    @GetMapping("/delete-all")
    public String deleteAllTables(RedirectAttributes redirectAttributes) {
        int deleted = 0;
        int failed = 0;
        for (RestaurantTable t : tableService.getAllTables()) {
            try {
                tableService.deleteTable(t.getId());
                deleted++;
            } catch (Exception e) {
                // Bỏ qua nếu bàn đang có hoá đơn tham chiếu không thể xoá
                failed++;
            }
        }
        String msg = "Đã xoá thành công " + deleted + " bàn.";
        if (failed > 0) {
            msg += " (Không thể xoá " + failed + " bàn vì đang có dữ liệu hoá đơn liên kết).";
        }
        redirectAttributes.addFlashAttribute("successMessage", msg);
        return "redirect:/tables";
    }
}
