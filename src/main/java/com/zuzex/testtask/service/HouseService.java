package com.zuzex.testtask.service;

import com.zuzex.testtask.entity.House;
import com.zuzex.testtask.entity.User;
import com.zuzex.testtask.repository.HouseRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class HouseService {
    private final HouseRepository repository;

    public HouseService(HouseRepository repository) {
        this.repository = repository;
    }

    public House add(House house) {
        if (house.getHost() == null) {
            throw new RuntimeException("The house must have a host");
        }
        return this.repository.save(house);
    }

    public House edit(House house) {
        if (house.getId() == null || this.repository.findById(house.getId()).isEmpty()) {
            throw new RuntimeException(
                String.format(
                    "House with id %s not found",
                    house.getId()
                )
            );
        }
        return this.add(house);
    }

    public House populateUsersInHouse(House house, User host, List<User> users) {
        if (!house.getHost().equals(host)) {
            throw new RuntimeException("Only the host can populate users into his house");
        }
        house.getOccupants().addAll(users);
        return this.repository.save(house);
    }

    public void removeById(Long id) {
        this.repository.deleteById(id);
    }

    public Optional<House> getById(Long id) {
        return this.repository.findById(id);
    }

    public List<House> getAll() {
        return this.repository.findAll();
    }
}
