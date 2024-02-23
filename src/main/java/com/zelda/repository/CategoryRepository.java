package com.zelda.repository;

import com.zelda.model.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    Category findByName(String roleName);

    @Modifying
    @Query(value = "select * from Category order by id desc limit 7;", nativeQuery = true)
    Iterable<Category> findTop7Category();

    @Modifying
    @Query(value = "select * from category left join roles " +
            "on category.role_id = roles.id " +
            "where roles.id = 2", nativeQuery = true)
    Iterable<Category> getCategoryForUser();
}
