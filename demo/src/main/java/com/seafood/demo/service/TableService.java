package com.seafood.demo.service;

import com.seafood.demo.entity.RestaurantTable;

import java.util.List;
import java.util.Optional;

public interface TableService {
    List<RestaurantTable> getAllTables();
    RestaurantTable saveTable(RestaurantTable table);
    Optional<RestaurantTable> getTableById(Long id);
    void deleteTable(Long id);
    RestaurantTable findByTableNumber(String tableNumber);
}
