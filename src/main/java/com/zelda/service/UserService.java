package com.zelda.service;

import com.zelda.model.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.Optional;

public interface UserService extends UserDetailsService {
    void save(User user);

    Iterable<User> findAll();

    User findByUsername(String username);

    User getCurrentUser();

    Optional<User> findById(Long id);

    UserDetails loadUserById(Long id);

    boolean checkLogin(User user);

    boolean isRegister(User user);

    boolean isCorrectConfirmPassword(User user);

    User matchPassword(User user);

    User updatePassword(Long id, User user);

    void validatePassword(User user);

    void validateEmail(User user);

    User updateUserProfile(Long id, User user);

    Page<User> getAll(Pageable pageable);
}
