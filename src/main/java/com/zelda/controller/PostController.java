package com.zelda.controller;

import com.zelda.exception.AppException;
import com.zelda.model.entity.Comment;
import com.zelda.model.entity.Post;
import com.zelda.service.UserService;
import com.zelda.service.post.IPostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/posts")
public class PostController {
    @Autowired
    private IPostService postService;

    @Autowired
    private UserService userService;

    @CrossOrigin(origins = "http://localhost:4200")
    @GetMapping("")
    public ResponseEntity<Page<Post>> showAll(Pageable pageable) {
        Page<Post> posts = postService.getAll(pageable);
        return new ResponseEntity<>(posts, HttpStatus.OK);
    }

    @CrossOrigin(origins = "http://localhost:4200")
    @GetMapping("/index")
    public ResponseEntity<Iterable<Post>> showAllPost(@RequestParam int index) {
        Iterable<Post> posts = postService.getAllPostByIndex(index);
        if (posts == null) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(posts, HttpStatus.OK);
    }

    @CrossOrigin(origins = "http://localhost:4200")
    @GetMapping("/public/{status}")
    public ResponseEntity<Iterable<Post>> getAllPostByStatus(@PathVariable("status") int status) {
        Iterable<Post> posts = this.postService.findAllByStatus(status);
        if (posts == null) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(posts, HttpStatus.OK);
    }

    @GetMapping("/search")
    public ResponseEntity<Iterable<Post>> getAllPostByTitle(@RequestParam(name = "title") String title) {
        Iterable<Post> posts = this.postService.findAllByTitle(title);
        return new ResponseEntity<>(posts, HttpStatus.OK);
    }

    @CrossOrigin(origins = "http://localhost:4200")
    @GetMapping("/categories/{categoryId}/index")
    public ResponseEntity<Iterable<Post>> getAllByCategoryIdAndIndex(@PathVariable Long categoryId,
                                                                     @RequestParam int index) {
        Iterable<Post> posts = postService.findAllByCategoryIdAndIndex(categoryId, index);
        if (posts == null) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(posts, HttpStatus.OK);
    }

    @CrossOrigin(origins = "http://localhost:4200")
    @GetMapping("/category/{categoryId}")
    public ResponseEntity<Page<Post>> getAllByCategoryId(@PathVariable Long categoryId, Pageable pageable) {
        Page<Post> posts = postService.findAllByCategoryId(categoryId, pageable);
        if (posts == null) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(posts, HttpStatus.OK);
    }

    @CrossOrigin(origins = "http://localhost:4200")
    @GetMapping("/findTop6By/categories/{categoryId}")
    public ResponseEntity<Iterable<Post>> findTop6ByCategoryId(@PathVariable Long categoryId) {
        Iterable<Post> posts = postService.findTop6ByCategoryId(categoryId);
        if (posts == null) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(posts, HttpStatus.OK);
    }

    @CrossOrigin(origins = "http://localhost:4200")
    @GetMapping("/findTopNew")
    public ResponseEntity<Iterable<Post>> findTop6New() {
        Iterable<Post> posts = postService.findTop6New();
        if (posts == null) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(posts, HttpStatus.OK);
    }

    @GetMapping("search/categories/{categoryId}")
    public ResponseEntity<Iterable<Post>> getAllByCategoryId(@RequestParam(name = "title") String title, @PathVariable Long categoryId) {
        Iterable<Post> posts = postService.findByTitleContainingAndCategoryId(title, categoryId);
        return new ResponseEntity<>(posts, HttpStatus.OK);
    }

//    @GetMapping("/user/{userId}")
//    public ResponseEntity<Iterable<Post>> getAllByUserId(@PathVariable Long userId) {
//        Iterable<Post> posts = postService.findAllByUserId(userId);
//        return new ResponseEntity<>(posts, HttpStatus.OK);
//    }

    @CrossOrigin(origins = "http://localhost:4200")
    @GetMapping("/{id}")
    public ResponseEntity<Optional<Post>> findById(@PathVariable("id") Long id) {
        Optional<Post> post = postService.findById(id);
        if (post == null) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(post, HttpStatus.OK);
    }

    @CrossOrigin(origins = "http://localhost:4200")
    @GetMapping("/byAdmin")
    public ResponseEntity<Iterable<Post>> getTop4PostByAdmin() {
        Iterable<Post> posts = postService.getTop4PostByAdmin();
        if (posts == null) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(posts, HttpStatus.OK);
    }

    @CrossOrigin(origins = "http://localhost:4200")
    @PostMapping
    public ResponseEntity<Post> save(@RequestBody Post post) throws AppException {
        return new ResponseEntity(postService.save(post), HttpStatus.OK);
    }

    @CrossOrigin(origins = "http://localhost:4200")
    @PutMapping("/edit/{id}")
    public ResponseEntity<Post> update(@PathVariable Long id, @RequestBody Post post) {
//        return new ResponseEntity(postService.save(post), HttpStatus.OK);
        return new ResponseEntity<>(postService.updatePost(id, post).get(), HttpStatus.OK);
    }

    @CrossOrigin(origins = "http://localhost:4200")
    @DeleteMapping("/del/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        postService.remove(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @CrossOrigin(origins = "http://localhost:4200")
    @PostMapping("/{id}/comments")
    public ResponseEntity<Optional<Post>> comment(@PathVariable("id") Long id, @RequestBody Comment commentForm, @RequestParam(value = "idParent", required = false) Long idParent) {
            return new ResponseEntity<>(postService.addCommentPost(id, commentForm, idParent), HttpStatus.OK);
    }

    @CrossOrigin(origins = "http://localhost:4200")
    @GetMapping("/admin")
    public ResponseEntity<Page<Post>> getAllForAdmin(Pageable pageable) {
        Page<Post> posts = postService.getAllForAdmin(pageable);
        return new ResponseEntity<>(posts, HttpStatus.OK);
    }

    @CrossOrigin(origins = "http://localhost:4200")
    @GetMapping("/question")
    public ResponseEntity<Page<Post>> getAllQuestion(Pageable pageable) {
        Page<Post> posts = postService.getAllQuestion(pageable);
        return new ResponseEntity<>(posts, HttpStatus.OK);
    }

    @CrossOrigin(origins = "http://localhost:4200")
    @GetMapping("/top5/{postId}/{userId}")
    public ResponseEntity<Iterable<Post>> getTop5PostByUserId(@PathVariable("postId") Long currentPostId, @PathVariable("userId") Long userId) {
        Iterable<Post> posts = postService.getTop5PostByUserId(currentPostId, userId);
        if (posts == null) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(posts, HttpStatus.OK);
    }

    @CrossOrigin(origins = "http://localhost:4200")
    @GetMapping("/countPostByCategory/{id}")
    public ResponseEntity<Long> countPostByCategory(@PathVariable Long id) {
        Long postByCategory = postService.countPostByCategoryId(id);
        return new ResponseEntity<>(postByCategory, HttpStatus.OK);
    }
}