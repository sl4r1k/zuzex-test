package com.zuzex.testtask.controller;

import com.zuzex.testtask.dto.UserDto;
import com.zuzex.testtask.entity.User;
import com.zuzex.testtask.service.UserService;
import lombok.SneakyThrows;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpServletRequest;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {
    private final UserService service;

    public UserController(UserService service) {
        this.service = service;
    }

    @PostMapping
    @SneakyThrows
    public ResponseEntity<User> create(@RequestBody UserDto user, HttpServletRequest request) {
        User entity = new User(
            null,
            user.getName(),
            user.getAge(),
            user.getPassword().toCharArray()
        );
        Long id = this.service.add(entity).getId();
        return ResponseEntity.created(
            new URI(
                String.format("%s/%d", request.getRequestURI(), id)
            )
        ).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> update(@RequestBody UserDto user, @PathVariable Long id) {
        User entity = new User(id, user.getName(), user.getAge(), user.getPassword().toCharArray());
        return ResponseEntity.ok(this.service.edit(entity));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<User> deleteById(@PathVariable Long id) {
        this.service.removeById(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getById(@PathVariable Long id) {
        return ResponseEntity.ok(this.service.getById(id).orElseThrow(EntityNotFoundException::new));
    }

    @GetMapping
    public ResponseEntity<List<User>> getAll() {
        return ResponseEntity.ok(this.service.getAll());
    }
}
