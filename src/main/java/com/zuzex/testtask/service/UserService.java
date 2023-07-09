package com.zuzex.testtask.service;

import com.zuzex.testtask.entity.User;
import com.zuzex.testtask.repository.UserRepository;
import org.springframework.security.core.userdetails.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;

@Service
public class UserService implements UserDetailsService {
    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository repository, PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
    }

    public User add(User user) {
        user.setPassword(this.passwordEncoder.encode(user.getPassword()).toCharArray());
        return this.repository.save(user);
    }

    public User edit(User user) {
        if (user.getId() == null || this.repository.findById(user.getId()).isEmpty()) {
            throw new RuntimeException(
                String.format(
                    "User with id %s not found",
                    user.getId()
                )
            );
        }
        return this.repository.save(user);
    }

    public void removeById(Long id) {
        this.repository.deleteById(id);
    }

    public Optional<User> getById(Long id) {
        return this.repository.findById(id);
    }

    public List<User> getAll() {
        return this.repository.findAll();
    }

    public Optional<User> getByName(String name) {
        return this.repository.findByName(name);
    }

    public List<User> getByIds(List<Long> ids) {
        return this.repository.findAllById(ids);
    }

    @Override
    public UserDetails loadUserByUsername(String username) {
        return this.getByName(username).orElseThrow(EntityNotFoundException::new);
    }
}
