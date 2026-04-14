package com.seafood.demo.controller;

import com.seafood.demo.entity.Category;
import com.seafood.demo.entity.Dish;
import com.seafood.demo.service.CategoryService;
import com.seafood.demo.service.DishService;
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
 * Controller xử lý các chức năng quản lý Thực đơn / Món ăn.
 * Cung cấp API hiển thị danh sách, thêm, sửa, xóa và import món ăn từ file Excel.
 */
@Controller
@RequestMapping("/dishes")
public class DishController {

    @Autowired
    private DishService dishService;

    @Autowired
    private CategoryService categoryService;

    /**
     * Hiển thị danh sách tất cả các món ăn có trong hệ thống.
     */
    @GetMapping
    public String listDishes(Model model) {
        model.addAttribute("dishes", dishService.getAllDishes());
        return "dishes/list";
    }

    /**
     * Hiển thị form tạo món ăn mới.
     * Cần load danh sách Danh mục (Categories) để người dùng chọn.
     */
    @GetMapping("/create")
    public String showCreateForm(Model model) {
        model.addAttribute("dish", new Dish());
        model.addAttribute("categories", categoryService.getAllCategories());
        return "dishes/form";
    }

    /**
     * Xử lý lưu món ăn vào CSDL khi submit form.
     */
    @PostMapping("/save")
    public String saveDish(@ModelAttribute("dish") Dish dish) {
        dishService.saveDish(dish);
        return "redirect:/dishes";
    }

    /**
     * Hiển thị form thay đổi thông tin của món ăn dựa trên ID.
     */
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable("id") Long id, Model model) {
        Dish dish = dishService.getDishById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid dish Id:" + id));
        model.addAttribute("dish", dish);
        model.addAttribute("categories", categoryService.getAllCategories());
        return "dishes/form";
    }

    /**
     * Xóa món ăn khỏi hệ thống. Xử lý ngoại lệ nếu món ăn đã nằm trong Đơn hàng nào đó.
     */
    @GetMapping("/delete/{id}")
    public String deleteDish(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
        try {
            dishService.deleteDish(id);
            redirectAttributes.addFlashAttribute("successMessage", "Xóa món ăn thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Không thể xóa món ăn này do món ăn đã được sử dụng trong đơn hàng!");
        }
        return "redirect:/dishes";
    }

    /**
     * Xử lý Import hàng loạt món ăn từ tệp Excel.
     */
    @PostMapping("/import")
    public String importExcel(@RequestParam("file") MultipartFile file, RedirectAttributes redirectAttributes) {
        if (file.isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Vui lòng chọn file Excel để import!");
            return "redirect:/dishes";
        }
        if (!ExcelHelper.hasExcelFormat(file)) {
            redirectAttributes.addFlashAttribute("errorMessage", "Chỉ hỗ trợ file định dạng .xlsx!");
            return "redirect:/dishes";
        }
        try {
            // 1. Phân tích file tải lên thành danh sách các record thô (Object[])
            List<Object[]> dishDataList = ExcelHelper.excelToDishData(file.getInputStream());
            int imported = 0;
            int skipped = 0;
            
            // 2. Lấy toàn bộ danh mục hiện có để map với cột "Tên danh mục" trong Excel
            List<Category> allCategories = categoryService.getAllCategories();
            // Lấy toàn bộ món để check duplicate
            List<Dish> allDishes = dishService.getAllDishes();

            for (Object[] data : dishDataList) {
                String name = (String) data[0];
                
                // Bỏ qua nếu món ăn đã tồn tại (kiểm tra theo tên món)
                boolean exists = allDishes.stream().anyMatch(d -> d.getName().equalsIgnoreCase(name));
                if (exists) {
                    skipped++;
                    continue;
                }

                double price = (double) data[1];
                String categoryName = (String) data[2];
                String description = (String) data[3];
                boolean status = (boolean) data[4];

                // Tìm category theo tên (Không phân biệt hoa thường)
                Optional<Category> categoryOpt = allCategories.stream()
                        .filter(c -> c.getName().equalsIgnoreCase(categoryName))
                        .findFirst();

                if (categoryOpt.isPresent()) {
                    Dish dish = new Dish();
                    dish.setName(name);
                    dish.setPrice(price);
                    dish.setCategory(categoryOpt.get());
                    dish.setDescription(description);
                    dish.setStatus(status);
                    
                    dishService.saveDish(dish);
                    imported++;
                } else {
                    skipped++; // Bỏ qua nếu không tìm thấy danh mục hợp lệ
                }
            }
            String msg = "Import thành công " + imported + " món ăn!";
            if (skipped > 0) {
                msg += " (Bỏ qua " + skipped + " dữ liệu đã tồn tại hoặc danh mục không hợp lệ)";
            }
            redirectAttributes.addFlashAttribute("successMessage", msg);
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Lỗi import: " + e.getMessage());
        }
        return "redirect:/dishes";
    }
}
