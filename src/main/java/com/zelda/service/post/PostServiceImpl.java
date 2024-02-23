package com.zelda.service.post;

import com.zelda.exception.AppException;
import com.zelda.exception.InputInvalidException;
import com.zelda.exception.PostNotFoundException;
import com.zelda.model.entity.Category;
import com.zelda.model.entity.Comment;
import com.zelda.model.entity.Post;
import com.zelda.model.entity.User;
import com.zelda.repository.CommentRepository;
import com.zelda.repository.PostRepository;
import com.zelda.service.UserService;
import com.zelda.service.category.ICategoryService;
import com.zelda.util.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;

@Service
public class PostServiceImpl implements IPostService {
    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private ICategoryService categoryService;

    @Override
    public Iterable<Post> findAll() {
        return postRepository.findAll();
    }

    @Override
    public Optional<Post> findById(Long id) {
        return postRepository.findById(id);
    }

    @Override
    public Post save(Post post) throws InputInvalidException {
        validateInput(post);
        Date date = new Date(Calendar.getInstance().getTime().getTime());
        post.setCreateAt(date);
//        post.setLikes((long) 0);
        postRepository.save(post);
        if (post.getDescription().equals("1")) {
            User user = post.getUser();
            Long oldPosts = user.getPosts();
            oldPosts = oldPosts == null ? Long.valueOf(0) : oldPosts;
            user.setPosts(oldPosts + Long.valueOf(1));
            userService.save(user);
        } else if (post.getDescription().equals("2")) {
            User user = post.getUser();
            Long oldQuestions = user.getQuestions();
            oldQuestions = oldQuestions == null ? Long.valueOf(0) : oldQuestions;
            user.setQuestions(oldQuestions + Long.valueOf(1));
            userService.save(user);
        }
        Category category = post.getCategory();
        Long oldPosts = category.getCountPost();
        oldPosts = oldPosts == null ? Long.valueOf(0) : oldPosts;
        category.setCountPost(oldPosts + Long.valueOf(1));
        categoryService.save(category);
        return post;
    }

    @Override
    public void remove(Long id) {
        Optional<Post> post = postRepository.findById(id);
        if (!post.isPresent()) {
            throw new PostNotFoundException("Bài viết không tồn tại!");
        }
        post.get().setStatus(0);
        postRepository.save(post.get());
    }

    @Override
    public Page<Post> getAll(Pageable pageable) {
        int size = pageable.getPageSize();
        int page = pageable.getPageNumber();
        if (page >= 1) {
            page = page - 1;
        } else if (page < 0) {
            page = 0;
        }
        Pageable pageDefault = PageRequest.of(page, size);
        List<Post> posts = postRepository.getAllByStatusAndUserAndDes("1", pageDefault.getOffset(), pageDefault.getPageSize());
        long countPosts = postRepository.countListPostByStatusAndUserAndDes("1");
        return new PageImpl<>(posts, pageDefault, countPosts);
    }

    @Override
    public Iterable<Post> getAllPostByIndex(int index) {
        return postRepository.getAllPostByIndex(index);
    }

    @Override
    public Iterable<Post> findAllByStatus(int status) {
        return postRepository.findAllByStatus(status);
    }

    @Override
    public Iterable<Post> findAllByTitle(String title) {
        return postRepository.findAllByTitle(title);
    }

    @Override
    public Iterable<Post> findAllByCategoryIdAndIndex(Long id, int index) {
        return postRepository.findAllByCategoryIdAndIndex(id, index);
    }

    @Override
    public Iterable<Post> findByTitleContainingAndCategoryId(String title, Long id) {
        return postRepository.findByTitleContainingAndCategoryId(title, id);
    }

    @Override
    public Page<Post> findAllByUserId(Long userId, Pageable pageable) {
        int size = pageable.getPageSize();
        int page = pageable.getPageNumber();
        if (page >= 1) {
            page = page - 1;
        } else if (page < 0) {
            page = 0;
        }
        Pageable pageDefault = PageRequest.of(page, size);
        List<Post> posts = postRepository.findAllByUserId("1",userId, pageDefault.getOffset(), pageDefault.getPageSize());
        Long coutListPostByUserId = postRepository.countListPostByUserId(userId, "1");
        return new PageImpl<>(posts, pageDefault, coutListPostByUserId);
    }

    @Override
    public Page<Post> findAllQuestionsByUserId(Long userId, Pageable pageable) {
        int size = pageable.getPageSize();
        int page = pageable.getPageNumber();
        if (page >= 1) {
            page = page - 1;
        } else if (page < 0) {
            page = 0;
        }
        Pageable pageDefault = PageRequest.of(page, size);
        List<Post> posts = postRepository.findAllByUserId("2", userId, pageDefault.getOffset(), pageDefault.getPageSize());
        Long coutListPostByUserId = postRepository.countListPostByUserId(userId, "2");
        return new PageImpl<>(posts, pageDefault, coutListPostByUserId);
    }

    @Override
    public Page<Post> findAllByCategoryId(Long categoryId, Pageable pageable) {
        if (Utils.stringIsEmpty(Utils.trimToEmpty(categoryId + ""))) {
            throw new InputInvalidException("Danh mục không tồn tại");
        }
        int size = pageable.getPageSize();
        int page = pageable.getPageNumber();
        if (page >= 1) {
            page = page - 1;
        } else if (page < 0) {
            page = 0;
        }
        Pageable pageDefault = PageRequest.of(page, size);
        List<Post> posts = postRepository.findAllByCategoryId(categoryId, pageDefault.getOffset(), pageDefault.getPageSize());
        long countPostByCategoryId = postRepository.countPostByCategoryId(categoryId);
        return new PageImpl<>(posts, pageDefault, countPostByCategoryId);
    }

    @Override
    public Iterable<Post> findTop6ByCategoryId(Long categoryId) {
        return postRepository.findTop6NewByCategory(categoryId);
    }

    @Override
    public Iterable<Post> findTop6New() {
        return postRepository.findTop6New();
    }

    @Override
    public Iterable<Post> getTop4PostByAdmin() {
        return postRepository.getTop4PostByAdmin();
    }

    @Override
    public Optional<Post> updatePost(Long idPost, Post post) {
        Optional<Post> postEntity = findById(idPost);
        if (!postEntity.isPresent()) {
            throw new PostNotFoundException("Bài viết không tồn tại!");
        }
        Date date = new Date(Calendar.getInstance().getTime().getTime());
        post.setId(postEntity.get().getId());
        post.setCreateAt(date);
        validateInput(post);
        postEntity.get().setTitle(post.getTitle());
        postEntity.get().setContent(post.getContent());
        postEntity.get().setDetail(post.getDetail());
        postEntity.get().setCategory(post.getCategory());
        postEntity.get().setDescription(post.getDescription());
        postRepository.save(post);
        return postEntity;
    }

    @Override
    public Optional<Post> addCommentPost(Long id, Comment comment, Long idParent) {
//        if (idParent != null) {
//            Date date = new Date(Calendar.getInstance().getTime().getTime());
//            comment.setCreateAt(date);
//            Optional<Comment> commentEntity = commentRepository.findById(idParent);
//            if (commentEntity.isPresent()) {
//                commentEntity.get().getChildrenComment().add(comment);
//                commentRepository.save(commentEntity.get());
//                User user = comment.getUser();
//                Long oldPosts = user.getComments();
//                oldPosts = oldPosts == null ? Long.valueOf(0) : oldPosts;
//                user.setComments(oldPosts + Long.valueOf(1));
//                userService.save(user);
//                Optional<Post> postEntity = postRepository.findById(id);
//                if (postEntity.isPresent()) {
//                    int indexOfCommentOld = postEntity.get().getListComment().indexOf(commentEntity.get());
//                    postEntity.get().getListComment().set(indexOfCommentOld, commentEntity.get());
//                }
//                return postEntity;
//            } else throw new AppException("Bài viết không tồn tại");
//        } else {
            Date date = new Date(Calendar.getInstance().getTime().getTime());
            comment.setCreateAt(date);
            commentRepository.save(comment);
            Optional<Post> postEntity = postRepository.findById(id);
            if (postEntity.isPresent()) {
                postEntity.get().getListComment().add(comment);
                postRepository.save(postEntity.get());
                User user = comment.getUser();
                Long oldPosts = user.getComments();
                oldPosts = oldPosts == null ? Long.valueOf(0) : oldPosts;
                user.setComments(oldPosts + Long.valueOf(1));
                userService.save(user);
                return postEntity;
            } else throw new AppException("Bài viết không tồn tại");
//        }
    }

    @Override
    public Long countPostByCategoryId(Long categoryId) {
        Long postByCategoryId = postRepository.countPostByCategoryId(categoryId);
        return postByCategoryId;
    }

    @Override
    public Page<Post> getAllForAdmin(Pageable pageable) {
        int size = pageable.getPageSize();
        int page = pageable.getPageNumber();
        if (page >= 1) {
            page = page - 1;
        } else if (page < 0) {
            page = 0;
        }
        Pageable pageDefault = PageRequest.of(page, size);
        Page<Post> posts = postRepository.findAll(pageDefault);
        return posts;
    }

    @Override
    public Page<Post> getAllQuestion(Pageable pageable) {
        int size = pageable.getPageSize();
        int page = pageable.getPageNumber();
        if (page >= 1) {
            page = page - 1;
        } else if (page < 0) {
            page = 0;
        }
        Pageable pageDefault = PageRequest.of(page, size);
        List<Post> posts = postRepository.getAllByStatusAndUserAndDes("2", pageDefault.getOffset(), pageDefault.getPageSize());
        long countPosts = postRepository.countListPostByStatusAndUserAndDes("2");
        return new PageImpl<>(posts, pageDefault, countPosts);
    }

    @Override
    public Iterable<Post> getTop5PostByUserId(Long currentPostId, Long userId) {
        Iterable<Post> posts = postRepository.getTop5PostByUserId(currentPostId, userId);
        return posts;
    }

    private void validateInput(Post post) throws InputInvalidException {
        if (Utils.stringIsEmpty(Utils.trimToEmpty(post.getTitle()))) {
            throw new InputInvalidException("Tiêu đề không hợp lệ");
        }

        if (post.getTitle().length() > 255) {
            throw new InputInvalidException("Tiêu đề không được dài quá 255 ký tự");
        }

        if (Utils.stringIsEmpty(Utils.trimToEmpty(post.getContent()))) {
            throw new InputInvalidException("Mô tả không hợp lệ");
        }

        if (post.getContent().length() > 1000) {
            throw new InputInvalidException("Mô tả không được dài quá 1000 ký tự");
        }

        if (post.getCategory() == null) {
            throw new InputInvalidException("Thể loại bài viết không hợp lệ");
        }

        if (Utils.stringIsEmpty(Utils.trimToEmpty(post.getDescription()))) {
            throw new InputInvalidException("Phạm vi bài viết không hợp lệ");
        }

        if (Utils.stringIsEmpty(Utils.trimToEmpty(post.getDetail()))) {
            throw new InputInvalidException("Nội dung bài viết không hợp lệ");
        }
    }
}
