package com.zelda.util;

import com.zelda.model.entity.Category;
import com.zelda.model.filter.CategoryFilter;
import org.springframework.data.jpa.domain.Specification;

public class Filters {
  private Filters() {
    throw new UnsupportedOperationException();
  }

//  public static Specification<Category> toSpecification(CategoryFilter filter) {
//    return Specification.where(Specifications.contain(Category_.name, filter.getName()));
//  }
}
