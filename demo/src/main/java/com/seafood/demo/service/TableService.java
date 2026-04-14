package com.seafood.demo.service;

import com.seafood.demo.entity.RestaurantTable;

import java.util.List;
import java.util.Optional;

/**
 * Interface định nghĩa các nghiệp vụ Quản lý Bàn.
 */
public interface TableService {
    List<RestaurantTable> getAllTables();
    RestaurantTable saveTable(RestaurantTable table);
    Optional<RestaurantTable> getTableById(Long id);
    void deleteTable(Long id);
    
    /**
     * Tìm bàn theo số bàn (ví dụ: Bàn 01)
     */
    RestaurantTable findByTableNumber(String tableNumber);
}
