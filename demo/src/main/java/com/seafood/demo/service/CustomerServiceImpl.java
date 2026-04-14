package com.seafood.demo.service;

import com.seafood.demo.entity.Customer;
import com.seafood.demo.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Logic thao tác và quản lý dữ liệu Khách hàng với thao tác gọi xuống CSDL.
 */
@Service
public class CustomerServiceImpl implements CustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    @Override
    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }

    @Override
    public Optional<Customer> getCustomerById(Long id) {
        return customerRepository.findById(id);
    }

    @Override
    public Customer saveCustomer(Customer customer) {
        return customerRepository.save(customer);
    }

    @Override
    public void deleteCustomer(Long id) {
        customerRepository.deleteById(id);
    }

    /**
     * Kiểm tra số điện thoại bị trùng nhằm tránh thêm trùng khách hàng khi import Excel.
     */
    @Override
    public boolean existsByPhone(String phone) {
        return customerRepository.existsByPhone(phone);
    }
}
