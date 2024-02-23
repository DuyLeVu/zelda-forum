package com.zelda.service.category;

import com.zelda.model.entity.Category;
import com.zelda.model.filter.CategoryFilter;
import com.zelda.repository.CategoryRepository;
import com.zelda.service.post.IPostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;
@Service
public class CategoryServiceImpl implements ICategoryService{

    @Autowired
    private CategoryRepository categoryRepository;

//    @Autowired
//    private IPostService postService;

    @Override
    public Iterable<Category> findAll() {
        return categoryRepository.findAll();
    }

    @Override
    public Optional<Category> findById(Long id) {
        return categoryRepository.findById(id);
    }

    @Override
    public Category save(Category category) {
        return categoryRepository.save(category);
    }

    @Override
    public void remove(Long id) {
        categoryRepository.deleteById(id);
    }

    @Override
    public Iterable<Category> findTop7Category() {
        return categoryRepository.findTop7Category();
    }

    @Override
    public Page<Category> getAll(CategoryFilter filter, Pageable pageable) {
        int size = pageable.getPageSize();
        int page = pageable.getPageNumber();
        if (page >= 1) {
            page = page - 1;
        } else if (page < 0) {
            page = 0;
        }
        Pageable pageDefault = PageRequest.of(page, size);
        return categoryRepository.findAll( pageDefault);
    }

    @Override
    public Iterable<Category> getAllNoPaging() {
        return categoryRepository.getCategoryForUser();
    }

    @Override
    public Long countPostByCategory(Long categoryId) {
//        return postService.countPostByCategoryId(categoryId);
        return null;
    }
}
