package com.seafood.demo.service;

import com.seafood.demo.entity.Dish;

import java.util.List;
import java.util.Optional;

public interface DishService {
    List<Dish> getAllDishes();
    Dish saveDish(Dish dish);
    Optional<Dish> getDishById(Long id);
    void deleteDish(Long id);
    List<Dish> getDishesByCategoryId(Long categoryId);
}
