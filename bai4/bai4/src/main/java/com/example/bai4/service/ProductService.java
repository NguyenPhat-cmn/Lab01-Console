package com.example.bai4.service;

import com.example.bai4.model.Product;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProductService {

    private final List<Product> products = new ArrayList<>();
    private long idCounter = 1;

    public List<Product> getAll() {
        return products;
    }

    public void add(Product product) {
        product.setId(idCounter++);
        products.add(product);
    }

    public Product getById(Long id) {
        return products.stream()
                .filter(p -> p.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    // ✅ UPDATE ĐÚNG
    public void update(Product updated) {
        for (int i = 0; i < products.size(); i++) {
            if (products.get(i).getId().equals(updated.getId())) {
                products.set(i, updated);
                return;
            }
        }
    }

    public void delete(Long id) {
        products.removeIf(p -> p.getId().equals(id));
    }
}
