package com.zelda.service.category;

import com.zelda.model.entity.Category;
import com.zelda.model.filter.CategoryFilter;
import com.zelda.service.IGeneralService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public interface ICategoryService extends IGeneralService<Category> {
    Iterable<Category> findTop7Category();

    Page<Category> getAll(CategoryFilter filter, Pageable pageable);

    Iterable<Category> getAllNoPaging();

    Long countPostByCategory(Long categoryId);
}
