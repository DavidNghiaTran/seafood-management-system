package com.seafood.demo.service;

import com.seafood.demo.entity.Dish;
import com.seafood.demo.repository.DishRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Implement các nghiệp vụ liên quan đến Món ăn.
 */
@Service
public class DishServiceImpl implements DishService {

    @Autowired
    private DishRepository dishRepository;

    @Override
    public List<Dish> getAllDishes() {
        return dishRepository.findAll();
    }

    @Override
    public Dish saveDish(Dish dish) {
        return dishRepository.save(dish);
    }

    @Override
    public Optional<Dish> getDishById(Long id) {
        return dishRepository.findById(id);
    }

    @Override
    public void deleteDish(Long id) {
        dishRepository.deleteById(id);
    }

    /**
     * Lấy danh sách món ăn thuộc về một danh mục cụ thể (vd: "Đồ uống", "Món chính")
     */
    @Override
    public List<Dish> getDishesByCategoryId(Long categoryId) {
        return dishRepository.findByCategoryId(categoryId);
    }
}
