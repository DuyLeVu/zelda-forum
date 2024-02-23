package com.zelda.service.user;

import com.zelda.model.entity.User;
import org.springframework.stereotype.Service;

import java.util.Optional;
@Service
public class AccountService implements IUserService {
    @Override
    public Iterable<User> findAll() {
        return null;
    }

    @Override
    public Optional<User> findById(Long id) {
        return Optional.empty();
    }

    @Override
    public User save(User user) {
        return null;
    }

    @Override
    public void remove(Long id) {

    }
}
