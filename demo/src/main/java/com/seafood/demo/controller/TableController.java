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

@Controller
@RequestMapping("/tables")
public class TableController {

    @Autowired
    private TableService tableService;

    @GetMapping
    public String listTables(Model model) {
        model.addAttribute("tables", tableService.getAllTables());
        return "tables/list";
    }

    @GetMapping("/create")
    public String showCreateForm(Model model) {
        model.addAttribute("table", new RestaurantTable());
        return "tables/form";
    }

    @PostMapping("/save")
    public String saveTable(@ModelAttribute("table") RestaurantTable table) {
        tableService.saveTable(table);
        return "redirect:/tables";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable("id") Long id, Model model) {
        RestaurantTable table = tableService.getTableById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid table Id:" + id));
        model.addAttribute("table", table);
        return "tables/form";
    }

    @GetMapping("/delete/{id}")
    public String deleteTable(@PathVariable("id") Long id) {
        tableService.deleteTable(id);
        return "redirect:/tables";
    }

    @PostMapping("/import")
    public String importExcel(@RequestParam("file") MultipartFile file, RedirectAttributes redirectAttributes) {
        if (file.isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Vui lòng chọn file Excel để import!");
            return "redirect:/tables";
        }
        if (!ExcelHelper.hasExcelFormat(file)) {
            redirectAttributes.addFlashAttribute("errorMessage", "Chỉ hỗ trợ file định dạng .xlsx!");
            return "redirect:/tables";
        }
        try {
            List<RestaurantTable> tables = ExcelHelper.excelToTables(file.getInputStream());
            for (RestaurantTable table : tables) {
                tableService.saveTable(table);
            }
            redirectAttributes.addFlashAttribute("successMessage", "Import thành công " + tables.size() + " bàn!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Lỗi import: " + e.getMessage());
        }
        return "redirect:/tables";
    }
}
