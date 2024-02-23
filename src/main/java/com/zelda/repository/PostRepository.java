package com.zelda.repository;

import com.zelda.model.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    @Modifying
    @Query(value = "select * from Post where status = 1 order by id desc limit ?1,5;", nativeQuery = true)
    Iterable<Post> getAllPostByIndex(int index);

    @Modifying
    @Query(value = "select * from Post where status = 1 order by id desc limit 6;", nativeQuery = true)
    Iterable<Post> findTop6New();

    @Modifying
    @Query(value = "select * from Post where status = 1 and category_id = :id order by id desc limit 6;", nativeQuery = true)
    Iterable<Post> findTop6NewByCategory(@Param("id") Long id);

    Iterable<Post> findAllByStatus(int status);

    Iterable<Post> findAllByTitle(String title);

    @Modifying
    @Query(value = "select * from Post where status = 1 and category_id = ?1 order by id desc" +
            " Limit ?2,?3 ", nativeQuery = true)
    List<Post> findAllByCategoryId(@Param("id") Long id, @Param("startPage") Long startPage, @Param("pageSize") int pageSize);

    @Modifying
    @Query(value = "select * from Post where status = 1 and category_id = ?1 order by id desc limit ?2,5;", nativeQuery = true)
    Iterable<Post> findAllByCategoryIdAndIndex(Long id, int index);

    Iterable<Post> findByTitleContainingAndCategoryId(String title, Long id);

    @Modifying
    @Query(value = "select * from Post where status = 1 "+
            "and description = ?1 " +
            "and user_id = ?2 order by id" +
            " Limit ?3,?4 ", nativeQuery = true)
    List<Post> findAllByUserId(@Param("description") String description,@Param("id") Long id, @Param("startPage") Long startPage, @Param("pageSize") int pageSize);

    @Modifying
    @Query(value = "select p.`id`, p.`content`, p.`create_at`, p.`description`, p.`detail`, p.`status`, p.`category_id`, p.`user_id`, p.`title`, p.`imgs` from `post` as p left join `user_role` as r on p.`user_id` = r.`user_id` where p.`status` = 1 and r.`role_id` = 1 order by p.`create_at` desc limit 4;", nativeQuery = true)
    List<Post> getTop4PostByAdmin();

    @Modifying
    @Query(value = "select p.* from `post` as p left join `user_role` as r on p.`user_id` = r.`user_id` where " +
            "p.`status` = 1 and r.`role_id` = 2 " +
            "and p.`description` = ?1 " +
            "order by p.`create_at` desc limit ?2,?3 ", nativeQuery = true)
    List<Post> getAllByStatusAndUserAndDes(@Param("description") String description, @Param("startPage") Long startPage, @Param("pageSize") int pageSize);

//    @Modifying
    @Query(value = "select COUNT(p.id) from `post` as p left join `user_role` as r on p.`user_id` = r.`user_id` where " +
            "p.`status` = 1 and r.`role_id` = 2 " +
            "and p.`description` = ?1 " , nativeQuery = true)
    long countListPostByStatusAndUserAndDes(@Param("description") String description);

    @Query(value = "select COUNT(p.id) from `post` as p where " +
            "p.`status` = 1 and p.`user_id` = ?1 " +
            "and p.`description` = ?2 ", nativeQuery = true)
    long countListPostByUserId(@Param("userId") Long userId, @Param("description") String description);

    @Query(value = "select COUNT(*) from `post` as p where " +
            "p.`status` = 1 and p.`category_id` = ?1 ", nativeQuery = true)
    long countPostByCategoryId(@Param("categoryId") Long categoryId);

    @Modifying
    @Query(value = "select * from (select * from post where id != ?1) as p " +
            "where p.`status` = 1 " +
            "and p.`user_id` = ?2 " +
            "order by id desc limit 5; ", nativeQuery = true)
    Iterable<Post> getTop5PostByUserId(@Param("postId") Long currentPostId, @Param("userId") Long userId);
}
