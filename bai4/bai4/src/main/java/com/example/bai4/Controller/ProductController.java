package com.example.bai4.Controller;

import com.example.bai4.model.Product;
import com.example.bai4.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Controller
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    // ================= LIST =================
    @GetMapping
    public String list(Model model) {
        model.addAttribute("products", productService.getAll());
        return "products/products";
    }

    // ================= ADD FORM =================
    @GetMapping("/add")
    public String showAdd(Model model) {
        model.addAttribute("product", new Product());
        return "products/add-product";
    }

    // ================= ADD HANDLE =================
    @PostMapping("/add")
    public String add(
            @Valid @ModelAttribute("product") Product product,
            BindingResult result
    ) throws IOException {

        MultipartFile file = product.getImageFile();

        // ❗ validate ảnh CHỈ ở add
        if (file == null || file.isEmpty()) {
            result.rejectValue("imageFile", "error.product", "Vui lòng chọn ảnh");
        }

        if (result.hasErrors()) {
            return "products/add-product";
        }

        product.setImage(saveImage(file));
        productService.add(product);
        return "redirect:/products";
    }

    // ================= EDIT FORM =================
    @GetMapping("/edit/{id}")
    public String showEdit(@PathVariable Long id, Model model) {
        Product product = productService.getById(id);
        if (product == null) {
            return "redirect:/products";
        }
        model.addAttribute("product", product);
        return "products/edit-product";
    }

    // ================= EDIT HANDLE =================
    @PostMapping("/edit")
    public String edit(
            @Valid @ModelAttribute("product") Product product,
            BindingResult result
    ) throws IOException {

        Product old = productService.getById(product.getId());
        if (old == null) {
            return "redirect:/products";
        }

        // ❗ QUAN TRỌNG: validate trước khi xử lý file
        if (result.hasErrors()) {
            product.setImage(old.getImage()); // giữ ảnh cũ
            return "products/edit-product";
        }

        MultipartFile file = product.getImageFile();
        if (file != null && !file.isEmpty()) {
            product.setImage(saveImage(file));
        } else {
            product.setImage(old.getImage());
        }

        productService.update(product);
        return "redirect:/products";
    }

    // ================= DELETE =================
    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        productService.delete(id);
        return "redirect:/products";
    }

    // ================= SAVE IMAGE =================
    private String saveImage(MultipartFile file) throws IOException {
        String uploadDir = System.getProperty("user.dir") + "/uploads/";
        File dir = new File(uploadDir);
        if (!dir.exists()) dir.mkdirs();

        String originalName = file.getOriginalFilename();
        String ext = "";

        if (originalName != null && originalName.contains(".")) {
            ext = originalName.substring(originalName.lastIndexOf("."));
        }

        String fileName = UUID.randomUUID() + ext;
        file.transferTo(new File(uploadDir + fileName));

        return "/uploads/" + fileName;
    }
}
