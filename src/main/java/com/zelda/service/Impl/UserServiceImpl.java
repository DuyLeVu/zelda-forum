package com.zelda.service.Impl;

import com.zelda.exception.AppException;
import com.zelda.exception.InputInvalidException;
import com.zelda.model.entity.User;
import com.zelda.model.entity.UserPrinciple;
import com.zelda.repository.UserRepository;
import com.zelda.service.UserService;
import com.zelda.util.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;
import java.util.regex.Pattern;

@Component
@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    private static final String EMAIL_PATTERN =
            "^[a-zA-Z][\\w-]+@([\\w]+\\.[\\w]+|[\\w]+\\.[\\w]{2,}\\.[\\w]{2,})$";
    private static final String PHONE_NUMBER_PATTERN = "^(0?)(3[2-9]|5[6|8|9]|7[0|6-9]|8[0-6|8|9]|9[0-4|6-9])[0-9]{7}$";

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException(username);
        }
        if (this.checkLogin(user)) {
            return UserPrinciple.build(user);
        }
        boolean enable = false;
        boolean accountNonExpired = false;
        boolean credentialsNonExpired = false;
        boolean accountNonLocked = false;
        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), enable, accountNonExpired, credentialsNonExpired, accountNonLocked, null);
    }


    @Override
    public void save(User user) {
        userRepository.save(user);
    }

    @Override
    public Iterable<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public User getCurrentUser() {
        User user;
        String userName;
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (principal instanceof UserDetails) {
            userName = ((UserDetails) principal).getUsername();
        } else {
            userName = principal.toString();
        }
        user = this.findByUsername(userName);
        return user;
    }

    @Override
    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    @Override
    public UserDetails loadUserById(Long id) {
        Optional<User> user = userRepository.findById(id);
        if (!user.isPresent()) {
            throw new NullPointerException();
        }
        return UserPrinciple.build(user.get());
    }

    @Override
    public boolean checkLogin(User user) {
        Iterable<User> users = this.findAll();
        boolean isCorrectUser = false;
        for (User currentUser : users) {
            if (currentUser.getUsername().equals(user.getUsername()) && user.getPassword().equals(currentUser.getPassword()) && currentUser.isEnabled()) {
                isCorrectUser = true;
            }
        }
        return isCorrectUser;
    }

    @Override
    public boolean isRegister(User user) {
        boolean isRegister = false;
        Iterable<User> users = this.findAll();
        for (User currentUser : users) {
            if (user.getUsername().equals(currentUser.getUsername())) {
                isRegister = true;
                break;
            }
        }
        return isRegister;
    }

    @Override
    public boolean isCorrectConfirmPassword(User user) {
        boolean isCorrentConfirmPassword = user.getPassword().equals(user.getConfirmPassword());
        return isCorrentConfirmPassword;
    }

    @Override
    public User matchPassword(User user) {
        if (Utils.stringIsEmpty(Utils.trimToEmpty(user.getPassword()))) {
            throw new InputInvalidException("Mật khẩu là bắt buộc");
        }
        if (Utils.stringIsEmpty(Utils.trimToEmpty(user.getId() + ""))) {
            throw new InputInvalidException("Tài khoản không hợp lệ");
        }
        Optional<User> userOptional = userRepository.findById(user.getId());
        if (passwordEncoder.matches(user.getPassword(), userOptional.get().getPassword())) {
            return userOptional.get();
        } else {
            throw new AppException("Mật khẩu hiện tại không đúng!");
        }
    }

    @Override
    public void validatePassword(User user) {
        if (Utils.stringIsEmpty(Utils.trimToEmpty(user.getPassword()))) {
            throw new InputInvalidException("Mật khẩu là bắt buộc");
        }

        if (Utils.stringIsEmpty(Utils.trimToEmpty(user.getConfirmPassword()))) {
            throw new InputInvalidException("Xác nhận mật khẩu là bắt buộc");
        }

        if (user.getPassword().length() < 6 || user.getPassword().length() > 32) {
            throw new InputInvalidException("Mật khẩu có độ dài từ 6 - 32!");
        }

        if (user.getConfirmPassword().length() < 6 || user.getConfirmPassword().length() > 32) {
            throw new InputInvalidException("Mật khẩu có độ dài từ 6 - 32!");
        }

        if (!isCorrectConfirmPassword(user)) {
            throw new AppException("Vui lòng xác nhận lại mật khẩu");
        }
    }

    @Override
    public void validateEmail(User user) {
        if (Utils.stringIsEmpty(Utils.trimToEmpty(user.getEmail()))) {
            throw new InputInvalidException("Email là bắt buộc");
        }
        if (!Pattern.matches(EMAIL_PATTERN, user.getEmail())){
            throw new InputInvalidException("Sai định dạng email");
        }
    }

    @Override
    public User updateUserProfile(Long id, User user) {
        Optional<User> userOptional = userRepository.findById(id);
        if (!userOptional.isPresent()) {
            throw new AppException("Tên người dùng không tồn tại");
        }
        validateDataUpdate(user);
        user.setId(userOptional.get().getId());
        user.setUsername(userOptional.get().getUsername());
        user.setEnabled(userOptional.get().isEnabled());
        if (!user.getPassword().equals(userOptional.get().getPassword())) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        } else {
            user.setPassword(userOptional.get().getPassword());
        }
        user.setRoles(userOptional.get().getRoles());
        if (!user.getConfirmPassword().equals(userOptional.get().getConfirmPassword())) {
            user.setConfirmPassword(passwordEncoder.encode(user.getConfirmPassword()));
        } else {
            user.setConfirmPassword(userOptional.get().getConfirmPassword());
        }
        userRepository.save(user);
        return user;
    }

    @Override
    public Page<User> getAll(Pageable pageable) {
        int size = pageable.getPageSize();
        int page = pageable.getPageNumber();
        if (page >= 1) {
            page = page - 1;
        } else if (page < 0) {
            page = 0;
        }
        Pageable pageDefault = PageRequest.of(page, size);
        return userRepository.findAll(pageDefault);
    }

    @Override
    public User updatePassword(Long id, User user) {
        Optional<User> userOptional = userRepository.findById(id);
        if (!userOptional.isPresent()) {
            throw new InputInvalidException("Tài khoản không hợp lệ");
        }
        validatePassword(user);
        user.setId(userOptional.get().getId());
        user.setUsername(userOptional.get().getUsername());
        user.setEnabled(userOptional.get().isEnabled());
        if (!user.getPassword().equals(userOptional.get().getPassword())) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        } else {
            throw new InputInvalidException("Vui lòng nhập lại mật khẩu mới, bạn đã nhập mật khẩu cũ");
        }
        user.setRoles(userOptional.get().getRoles());
//        if (user.getConfirmPassword().equals(passwordDuser.getPassword())) {
        if (passwordEncoder.matches(user.getConfirmPassword(), user.getPassword())) {
            user.setConfirmPassword(passwordEncoder.encode(user.getConfirmPassword()));
        } else {
            throw new InputInvalidException("Vui lòng xác nhận lại mật khẩu mới!");
        }
        userRepository.save(user);
        return user;
    }

    public void validateDataUpdate(User user) {
        if (Utils.stringIsEmpty(Utils.trimToEmpty(user.getFullName()))) {
            throw new InputInvalidException("Tên hiển thị là bắt buộc");
        }

        if (user.getFullName().length() < 6 || user.getFullName().length() > 150) {
            throw new InputInvalidException("Tên hiển thị có độ dài từ 6 - 150");
        }

        if (Utils.stringIsEmpty(Utils.trimToEmpty(user.getGender()))) {
            throw new InputInvalidException("Giới tính là bắt buộc");
        }

        validateEmail(user);

        if (Utils.stringIsEmpty(Utils.trimToEmpty(user.getPhoneNumber()))) {
            throw new InputInvalidException("Số điện thoại là bắt buộc");
        }

        if (!Pattern.matches(PHONE_NUMBER_PATTERN, user.getPhoneNumber())){
            throw new InputInvalidException("Số điện thoại không hợp lệ");
        }

//        if (user.getPassword().length() < 6 || user.getPassword().length() > 32) {
//            throw new InputInvalidException("Mật khẩu có độ dài từ 6 - 32!");
//        }
//
//        if (user.getConfirmPassword().length() < 6 || user.getConfirmPassword().length() > 32) {
//            throw new InputInvalidException("Mật khẩu có độ dài từ 6 - 32!");
//        }
    }
}
