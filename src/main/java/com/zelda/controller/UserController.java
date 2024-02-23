package com.zelda.controller;

import com.zelda.exception.AppException;
import com.zelda.exception.InputInvalidException;
import com.zelda.model.entity.*;
import com.zelda.service.Impl.JwtService;
import com.zelda.service.RoleService;
import com.zelda.service.UserService;
import com.zelda.service.VerificationTokenService;
import com.zelda.service.post.IPostService;
import com.zelda.util.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@RestController
@PropertySource("classpath:application.properties")
@CrossOrigin("*")
public class UserController {
    @Autowired
    private Environment env;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private VerificationTokenService verificationTokenService;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private IPostService postService;

    @GetMapping("/users")
    public ResponseEntity<Iterable<User>> showAllUser() {
        Iterable<User> users = userService.findAll();
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @PostMapping("/register")
    public ResponseEntity<User> createUser(@RequestBody User user, BindingResult bindingResult) throws AppException {
//        if (bindingResult.hasFieldErrors()) {
//            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
//        }
        if (Utils.stringIsEmpty(Utils.trimToEmpty(user.getUsername()))) {
            throw new InputInvalidException("Tên tài khoản không hợp lệ");
        }
        if (user.getUsername().length() < 6 || user.getUsername().length() > 50) {
            throw new InputInvalidException("Tên tài khoản có độ dài từ 6 - 50");
        }
        userService.validateEmail(user);
        userService.validatePassword(user);
        Iterable<User> users = userService.findAll();
        for (User currentUser : users) {
            if (currentUser.getUsername().equals(user.getUsername())) {
                throw new AppException("Tên tài khoản đã được sử dụng");
            }
        }
        if (!userService.isCorrectConfirmPassword(user)) {
            throw new AppException("Vui lòng xác nhận lại mật khẩu");
        }
        if (user.getRoles() != null) {
            Role role = roleService.findByName("ROLE_ADMIN");
            Set<Role> roles = new HashSet<>();
            roles.add(role);
            user.setRoles(roles);
        } else {
            Role role1 = roleService.findByName("ROLE_USER");
            Set<Role> roles1 = new HashSet<>();
            roles1.add(role1);
            user.setRoles(roles1);
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setConfirmPassword(passwordEncoder.encode(user.getConfirmPassword()));
        user.setPosts((long) 0);
        userService.save(user);
        VerificationToken token = new VerificationToken(user);
        token.setExpiryDate(10);
        verificationTokenService.save(token);
        return new ResponseEntity<>(user, HttpStatus.CREATED);
    }

    @PostMapping("/match-password")
    public ResponseEntity<User> matches(@RequestBody User user) {
        return new ResponseEntity<>(userService.matchPassword(user), HttpStatus.OK);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User user) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = jwtService.generateTokenLogin(authentication);
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        User currentUser = userService.findByUsername(user.getUsername());
        return ResponseEntity.ok(new JwtResponse(jwt, currentUser.getId(), userDetails.getUsername(), userDetails.getAuthorities()));
    }

    @GetMapping("/hello")
    public ResponseEntity<String> hello() {
        return new ResponseEntity("Hello World", HttpStatus.OK);
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<User> getProfile(@PathVariable Long id) {
        Optional<User> userOptional = this.userService.findById(id);
        return userOptional.map(user -> new ResponseEntity<>(user, HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @CrossOrigin(origins = "http://localhost:4200")
    @PutMapping("/users/{id}")
    public ResponseEntity<User> updateUserProfile(@PathVariable Long id,@RequestBody User user, BindingResult bindingResult) {
//        validate email & phone number
//        if (bindingResult.hasFieldErrors()) {
//            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
//        }
        return new ResponseEntity<>(userService.updateUserProfile(id, user), HttpStatus.OK);
    }

    @CrossOrigin(origins = "http://localhost:4200")
    @GetMapping("/users/{id}/posts")
    public ResponseEntity<Page<Post>> getUserPosts(@PathVariable Long id, Pageable pageable) {
        Page<Post> posts = this.postService.findAllByUserId(id, pageable);
        return new ResponseEntity<>(posts, HttpStatus.OK);
    }

    @CrossOrigin(origins = "http://localhost:4200")
    @GetMapping("/users/question/{id}")
    public ResponseEntity<Page<Post>> getUserQuestions(@PathVariable Long id, Pageable pageable) {
        Page<Post> posts = this.postService.findAllQuestionsByUserId(id, pageable);
        return new ResponseEntity<>(posts, HttpStatus.OK);
    }

    @CrossOrigin(origins = "http://localhost:4200")
    @PutMapping("/users/update-info/{id}")
    public ResponseEntity<User> updatePassword(@PathVariable Long id, @RequestBody User user) {
        return new ResponseEntity<>(userService.updatePassword(id, user), HttpStatus.OK);
    }

    @CrossOrigin(origins = "http://localhost:4200")
    @GetMapping("/admin")
    public ResponseEntity<Page<User>> getAll(Pageable pageable) {
        Page<User> users = userService.getAll(pageable);
        if (users == null) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(users, HttpStatus.OK);
    }
}
