package com.zuzex.testtask.controller;

import com.zuzex.testtask.dto.HouseDto;
import com.zuzex.testtask.entity.House;
import com.zuzex.testtask.entity.User;
import com.zuzex.testtask.service.HouseService;
import com.zuzex.testtask.service.UserService;
import lombok.SneakyThrows;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpServletRequest;
import java.net.URI;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/v1/houses")
public class HouseController {
    private final HouseService service;
    private final UserService userService;

    public HouseController(HouseService service, UserService userService) {
        this.service = service;
        this.userService = userService;
    }

    @PostMapping
    @SneakyThrows
    public ResponseEntity<House> create(@RequestBody HouseDto house, HttpServletRequest request) {
        House entity = new House(
            null,
            house.getAddress(),
            this.userService.getById(house.getHostId()).orElseThrow(RuntimeException::new),
            new ArrayList<>()
        );
        Long id = this.service.add(entity).getId();
        return ResponseEntity.created(
            new URI(
                String.format("%s/%d", request.getRequestURI(), id)
            )
        ).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<House> update(@RequestBody HouseDto house, @PathVariable Long id) {
        User host = this.userService.getById(house.getHostId()).orElseThrow(RuntimeException::new);
        List<User> occupants = this.service.getById(id).orElseThrow(RuntimeException::new).getOccupants();
        House entity = new House(id, house.getAddress(), host, occupants);
        return ResponseEntity.ok(this.service.edit(entity));
    }

    @PostMapping("/{id}/occupants")
    public ResponseEntity<House> populateUsers(@PathVariable Long id, @RequestBody List<Long> occupantIds, Principal principal) {
        return ResponseEntity.ok(this.service.populateUsersInHouse(id, occupantIds, principal.getName()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<House> deleteById(@PathVariable Long id) {
        this.service.removeById(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<House> getById(@PathVariable Long id) {
        return ResponseEntity.ok(this.service.getById(id).orElseThrow(EntityNotFoundException::new));
    }

    @GetMapping
    public ResponseEntity<List<House>> getAll() {
        List<House> all = this.service.getAll();
        return ResponseEntity.ok(all);
    }
}
