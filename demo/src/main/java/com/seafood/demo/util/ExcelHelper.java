package com.seafood.demo.util;

import com.seafood.demo.entity.*;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ExcelHelper {

    public static String TYPE = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";

    public static boolean hasExcelFormat(MultipartFile file) {
        return TYPE.equals(file.getContentType());
    }

    private static String getStringCellValue(Cell cell) {
        if (cell == null) return "";
        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue().trim();
            case NUMERIC:
                return String.valueOf((long) cell.getNumericCellValue());
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            default:
                return "";
        }
    }

    private static double getNumericCellValue(Cell cell) {
        if (cell == null) return 0;
        switch (cell.getCellType()) {
            case NUMERIC:
                return cell.getNumericCellValue();
            case STRING:
                try {
                    return Double.parseDouble(cell.getStringCellValue().trim());
                } catch (NumberFormatException e) {
                    return 0;
                }
            default:
                return 0;
        }
    }

    private static int getIntCellValue(Cell cell) {
        return (int) getNumericCellValue(cell);
    }

    private static boolean getBooleanCellValue(Cell cell) {
        if (cell == null) return true;
        switch (cell.getCellType()) {
            case BOOLEAN:
                return cell.getBooleanCellValue();
            case NUMERIC:
                return cell.getNumericCellValue() != 0;
            case STRING:
                String val = cell.getStringCellValue().trim().toLowerCase();
                return val.equals("true") || val.equals("1") || val.equals("yes")
                        || val.equals("có") || val.equals("sẵn sàng") || val.equals("trống");
            default:
                return true;
        }
    }

    /**
     * Đọc danh sách Category từ file Excel.
     * Cột: name | description
     */
    public static List<Category> excelToCategories(InputStream is) {
        try (Workbook workbook = new XSSFWorkbook(is)) {
            Sheet sheet = workbook.getSheetAt(0);
            List<Category> categories = new ArrayList<>();

            int firstDataRow = -1;
            // Tìm hàng tiêu đề (hàng có chứa "Tên Danh Mục" hoặc "name"), sau đó bắt đầu đọc từ hàng tiếp theo
            for (int i = sheet.getFirstRowNum(); i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row == null) continue;
                String cellValue = getStringCellValue(row.getCell(0)).toLowerCase();
                if (cellValue.contains("tên danh mục") || cellValue.contains("ten danh muc")
                        || cellValue.contains("name") || cellValue.contains("danh mục")) {
                    firstDataRow = i + 1;
                    break;
                }
            }
            // Nếu không tìm thấy header, bỏ qua hàng đầu tiên có dữ liệu
            if (firstDataRow == -1) {
                firstDataRow = sheet.getFirstRowNum() + 1;
            }

            for (int i = firstDataRow; i <= sheet.getLastRowNum(); i++) {
                Row currentRow = sheet.getRow(i);
                if (currentRow == null) continue;
                String name = getStringCellValue(currentRow.getCell(0));
                if (name.isEmpty()) continue;

                Category category = new Category();
                category.setName(name);
                category.setDescription(getStringCellValue(currentRow.getCell(1)));
                categories.add(category);
            }
            return categories;
        } catch (Exception e) {
            throw new RuntimeException("Lỗi đọc file Excel Category: " + e.getMessage());
        }
    }

    /**
     * Đọc danh sách Dish từ file Excel.
     * Cột: name | price | categoryName | description | status (true/false)
     * categoryName sẽ được map bên ngoài.
     */
    public static List<Object[]> excelToDishData(InputStream is) {
        try (Workbook workbook = new XSSFWorkbook(is)) {
            Sheet sheet = workbook.getSheetAt(0);
            Iterator<Row> rows = sheet.iterator();
            List<Object[]> dishDataList = new ArrayList<>();

            if (rows.hasNext()) rows.next(); // skip header

            while (rows.hasNext()) {
                Row currentRow = rows.next();
                String name = getStringCellValue(currentRow.getCell(0));
                if (name.isEmpty()) continue;

                double price = getNumericCellValue(currentRow.getCell(1));
                String categoryName = getStringCellValue(currentRow.getCell(2));
                String description = getStringCellValue(currentRow.getCell(3));
                boolean status = getBooleanCellValue(currentRow.getCell(4));

                dishDataList.add(new Object[]{name, price, categoryName, description, status});
            }
            return dishDataList;
        } catch (Exception e) {
            throw new RuntimeException("Lỗi đọc file Excel Dish: " + e.getMessage());
        }
    }

    /**
     * Đọc danh sách RestaurantTable từ file Excel.
     * Cột: tableNumber | capacity | status (true/false)
     */
    public static List<RestaurantTable> excelToTables(InputStream is) {
        try (Workbook workbook = new XSSFWorkbook(is)) {
            Sheet sheet = workbook.getSheetAt(0);
            Iterator<Row> rows = sheet.iterator();
            List<RestaurantTable> tables = new ArrayList<>();

            if (rows.hasNext()) rows.next();

            while (rows.hasNext()) {
                Row currentRow = rows.next();
                String tableNumber = getStringCellValue(currentRow.getCell(0));
                if (tableNumber.isEmpty()) continue;

                RestaurantTable table = new RestaurantTable();
                table.setTableNumber(tableNumber);
                table.setCapacity(getIntCellValue(currentRow.getCell(1)));
                table.setStatus(getBooleanCellValue(currentRow.getCell(2)));
                tables.add(table);
            }
            return tables;
        } catch (Exception e) {
            throw new RuntimeException("Lỗi đọc file Excel Table: " + e.getMessage());
        }
    }

    /**
     * Đọc danh sách Customer từ file Excel.
     * Cột: fullName | phone | address
     */
    public static List<Customer> excelToCustomers(InputStream is) {
        try (Workbook workbook = new XSSFWorkbook(is)) {
            Sheet sheet = workbook.getSheetAt(0);
            Iterator<Row> rows = sheet.iterator();
            List<Customer> customers = new ArrayList<>();

            if (rows.hasNext()) rows.next();

            while (rows.hasNext()) {
                Row currentRow = rows.next();
                String fullName = getStringCellValue(currentRow.getCell(0));
                if (fullName.isEmpty()) continue;

                Customer customer = new Customer();
                customer.setFullName(fullName);
                customer.setPhone(getStringCellValue(currentRow.getCell(1)));
                customer.setAddress(getStringCellValue(currentRow.getCell(2)));
                customers.add(customer);
            }
            return customers;
        } catch (Exception e) {
            throw new RuntimeException("Lỗi đọc file Excel Customer: " + e.getMessage());
        }
    }

    /**
     * Đọc danh sách User (Employee) từ file Excel.
     * Cột: fullName | username | password | phone | address | position
     */
    public static List<User> excelToEmployees(InputStream is) {
        try (Workbook workbook = new XSSFWorkbook(is)) {
            Sheet sheet = workbook.getSheetAt(0);
            Iterator<Row> rows = sheet.iterator();
            List<User> users = new ArrayList<>();

            if (rows.hasNext()) rows.next();

            while (rows.hasNext()) {
                Row currentRow = rows.next();
                String fullName = getStringCellValue(currentRow.getCell(0));
                if (fullName.isEmpty()) continue;

                User user = new User();
                user.setFullName(fullName);
                user.setUsername(getStringCellValue(currentRow.getCell(1)));
                user.setPassword(getStringCellValue(currentRow.getCell(2)));
                user.setPhone(getStringCellValue(currentRow.getCell(3)));
                user.setAddress(getStringCellValue(currentRow.getCell(4)));
                user.setPosition(getStringCellValue(currentRow.getCell(5)));
                user.setRole(Role.EMPLOYEE);
                users.add(user);
            }
            return users;
        } catch (Exception e) {
            throw new RuntimeException("Lỗi đọc file Excel Employee: " + e.getMessage());
        }
    }
}
