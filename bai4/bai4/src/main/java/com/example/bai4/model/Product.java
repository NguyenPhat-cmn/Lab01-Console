package com.example.bai4.model;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.web.multipart.MultipartFile;

public class Product {

    private Long id;

    @NotBlank(message = "Tên không được trống")
    private String name;

    @NotBlank(message = "Loại không được trống")
    private String category;

    @NotNull(message = "Giá không được trống")
    @DecimalMin(value = "1.0", message = "Giá phải ≥ 1")
    private Double price;

    private String image;
    private MultipartFile imageFile;

    // ===== getter setter =====
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public Double getPrice() { return price; }
    public void setPrice(Double price) { this.price = price; }

    public String getImage() { return image; }
    public void setImage(String image) { this.image = image; }

    public MultipartFile getImageFile() { return imageFile; }
    public void setImageFile(MultipartFile imageFile) { this.imageFile = imageFile; }
}
