package com.seafood.demo.service;

import com.seafood.demo.entity.RestaurantTable;
import com.seafood.demo.repository.TableRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Thực thi các logic liên quan đến tìm kiếm, thêm, sửa, xóa thông tin Bàn.
 */
@Service
public class TableServiceImpl implements TableService {

    @Autowired
    private TableRepository tableRepository;

    /**
     * Lấy tất cả bàn có trong hệ thống, thường dùng để hiển thị trên sơ đồ bàn (Pos layout).
     */
    @Override
    public List<RestaurantTable> getAllTables() {
        return tableRepository.findAll();
    }

    /**
     * Lưu thông tin bàn phục vụ cho chức năng thêm bàn mới hoặc cập nhật thông tin bàn cũ.
     */
    @Override
    public RestaurantTable saveTable(RestaurantTable table) {
        return tableRepository.save(table);
    }

    @Override
    public Optional<RestaurantTable> getTableById(Long id) {
        return tableRepository.findById(id);
    }

    @Override
    public void deleteTable(Long id) {
        tableRepository.deleteById(id);
    }

    /**
     * Tra cứu bàn thông qua "Số bàn". Hỗ trợ cho tính năng Import Excel để tránh ghi trùng lặp.
     */
    @Override
    public RestaurantTable findByTableNumber(String tableNumber) {
        return tableRepository.findByTableNumber(tableNumber);
    }
}
