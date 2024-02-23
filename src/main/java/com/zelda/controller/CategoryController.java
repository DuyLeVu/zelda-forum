package com.zelda.controller;

import com.zelda.model.entity.Category;
import com.zelda.model.filter.CategoryFilter;
import com.zelda.service.category.ICategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/categories")
public class CategoryController {
    @Autowired
    private ICategoryService categoryService;

    @CrossOrigin(origins = "http://localhost:4200")
    @GetMapping("")
    public ResponseEntity<Page<Category>> getAll(CategoryFilter filter, Pageable pageable) {
            Page<Category> categories = categoryService.getAll(filter, pageable);
        if (categories == null) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(categories, HttpStatus.OK);
    }

    @CrossOrigin(origins = "http://localhost:4200")
    @GetMapping("/categories")
    public ResponseEntity<Iterable<Category>> getAllNoPaging() {
        Iterable<Category> categories = categoryService.getAllNoPaging();
        if (categories == null) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(categories, HttpStatus.OK);
    }

    @CrossOrigin(origins = "http://localhost:4200")
    @GetMapping("/top7Category")
    public ResponseEntity<Iterable<Category>> getTop7Category() {
        Iterable<Category> categories = categoryService.findAll();
        return new ResponseEntity<>(categories, HttpStatus.OK);
    }

    @CrossOrigin(origins = "http://localhost:4200")
    @GetMapping("/{id}")
    public ResponseEntity<Category> getById(@PathVariable Long id) {
        Optional<Category> category = categoryService.findById(id);
        return new ResponseEntity<>(category.get(), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Category> save(@RequestBody Category category) {
        categoryService.save(category);
        return new ResponseEntity<>(category, HttpStatus.OK);
    }

    @CrossOrigin(origins = "http://localhost:4200")
    @GetMapping("/countPostByCategory/{id}")
    public ResponseEntity<Long> countPostByCategory(@PathVariable Long id) {
        Long postByCategory = categoryService.countPostByCategory(id);
        return new ResponseEntity<>(postByCategory, HttpStatus.OK);
    }
}
