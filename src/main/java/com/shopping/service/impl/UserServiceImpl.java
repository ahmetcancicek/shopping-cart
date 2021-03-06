package com.shopping.service.impl;

import com.shopping.domain.exception.AlreadyExistsElementException;
import com.shopping.domain.exception.NoSuchElementFoundException;
import com.shopping.domain.model.User;
import com.shopping.repository.UserRepository;
import com.shopping.service.RoleService;
import com.shopping.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Slf4j
@Service(value = "userService")
@RequiredArgsConstructor
public class UserServiceImpl implements UserService, UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    private final RoleService roleService;

    @Override
    public User findByUsername(String username) {
        return userRepository.findByUsername(username).orElseThrow(() -> {
            log.error("user does not exist with username: {}", username);
            return new NoSuchElementFoundException(String.format("user does not exist with username: %s", username));
        });
    }

    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(() -> {
            log.error("user does not exist with email: {}", email);
            return new NoSuchElementFoundException(String.format("user does not exist with email: %s", email));
        });
    }

    @Override
    public User save(User user) {
        isExisted(user);

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setActive(true);

        user.setRoles(new HashSet<>(Set.of(roleService.findByName("USER"))));

        User savedUser = userRepository.save(user);
        log.info("new user has been created with ID: {}", user.getId());
        return savedUser;
    }

    private void isExisted(User user) {
        userRepository.findByEmail(user.getEmail()).ifPresent((it) -> {
            log.error("user already exists with email: {}", it.getEmail());
            throw new AlreadyExistsElementException("user already exists with email: {" + it.getEmail() + "}");
        });

        userRepository.findByUsername(user.getUsername()).ifPresent((it) -> {
            log.error("user already exists with username: {}" + it.getUsername());
            throw new AlreadyExistsElementException("user already exists with username: {" + it.getUsername() + "}");
        });
    }

    @Override
    public void deleteById(Long id) {
        userRepository.findById(id).orElseThrow(() -> {
            log.error("user does not exist with id: {}", id);
            return new NoSuchElementFoundException(String.format("user does not exist with id: {%d}", id));
        });
        userRepository.deleteById(id);
        log.info("user has been deleted with ID: {}", id);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username).orElseThrow(() -> {
            return new NoSuchElementFoundException(String.format("user does not exist with username: %s", username));
        });
        user.setRoles(getAuthority(user));
        return user;
    }

    private Set getAuthority(User user) {
        Set authorities = new HashSet();
        user.getRoles().forEach(role -> {
            authorities.add(new SimpleGrantedAuthority(role.getName()));
        });
        return authorities;
    }

//    private Set<SimpleGrantedAuthority> getAuthority(User user) {
//        Set<SimpleGrantedAuthority> authorities = new HashSet<>();
//        user.getRoles().forEach(role -> {
//            authorities.add(new SimpleGrantedAuthority("ROLE_" + role.getName()));
//        });
//        return authorities;
//    }
}
