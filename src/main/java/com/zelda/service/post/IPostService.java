package com.zelda.service.post;

import com.zelda.model.entity.Comment;
import com.zelda.model.entity.Post;
import com.zelda.service.IGeneralService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public interface IPostService extends IGeneralService<Post> {
    Page<Post> getAll(Pageable pageable);

    Iterable<Post> getAllPostByIndex(int index);

    Iterable<Post> findAllByStatus(int status);

    Iterable<Post> findAllByTitle(String title);

    Iterable<Post> findAllByCategoryIdAndIndex(Long id, int index);

    Iterable<Post> findByTitleContainingAndCategoryId(String title, Long id);

    Page<Post> findAllByUserId(Long userId, Pageable pageable);

    Page<Post> findAllQuestionsByUserId(Long userId, Pageable pageable);

    Page<Post> findAllByCategoryId(Long categoryId, Pageable pageable);

    Iterable<Post> findTop6ByCategoryId(Long categoryId);

    Iterable<Post> findTop6New();

    Iterable<Post> getTop4PostByAdmin();

    Optional<Post> updatePost(Long idPost, Post post);

    Optional<Post> addCommentPost(Long id, Comment comment, Long idParent);

    Long countPostByCategoryId(Long categoryId);

    Page<Post> getAllForAdmin(Pageable pageable);

    Page<Post> getAllQuestion(Pageable pageable);

    Iterable<Post> getTop5PostByUserId(Long currentPostId, Long userId);
}
