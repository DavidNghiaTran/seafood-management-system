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

@Controller
@RequestMapping("/dishes")
public class DishController {

    @Autowired
    private DishService dishService;

    @Autowired
    private CategoryService categoryService;

    @GetMapping
    public String listDishes(Model model) {
        model.addAttribute("dishes", dishService.getAllDishes());
        return "dishes/list";
    }

    @GetMapping("/create")
    public String showCreateForm(Model model) {
        model.addAttribute("dish", new Dish());
        model.addAttribute("categories", categoryService.getAllCategories());
        return "dishes/form";
    }

    @PostMapping("/save")
    public String saveDish(@ModelAttribute("dish") Dish dish) {
        dishService.saveDish(dish);
        return "redirect:/dishes";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable("id") Long id, Model model) {
        Dish dish = dishService.getDishById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid dish Id:" + id));
        model.addAttribute("dish", dish);
        model.addAttribute("categories", categoryService.getAllCategories());
        return "dishes/form";
    }

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
            List<Object[]> dishDataList = ExcelHelper.excelToDishData(file.getInputStream());
            int count = 0;
            List<Category> allCategories = categoryService.getAllCategories();

            for (Object[] data : dishDataList) {
                String name = (String) data[0];
                double price = (double) data[1];
                String categoryName = (String) data[2];
                String description = (String) data[3];
                boolean status = (boolean) data[4];

                // Tìm category theo tên
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
                    count++;
                }
            }
            redirectAttributes.addFlashAttribute("successMessage", "Import thành công " + count + " món ăn!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Lỗi import: " + e.getMessage());
        }
        return "redirect:/dishes";
    }
}
