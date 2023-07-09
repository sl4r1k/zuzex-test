package com.zuzex.testtask.service;

import com.zuzex.testtask.entity.House;
import com.zuzex.testtask.entity.User;
import com.zuzex.testtask.repository.HouseRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;

@Service
public class HouseService {
    private final HouseRepository repository;
    private final UserService userService;

    public HouseService(HouseRepository repository, UserService userService) {
        this.repository = repository;
        this.userService = userService;
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

    @Transactional
    public House populateUsersInHouse(Long houseId, List<Long> occupantIds, String hostName) {
        House house = this.repository.findById(houseId).orElseThrow(EntityNotFoundException::new);
        User host = this.userService.getByName(hostName).orElseThrow(EntityNotFoundException::new);
        if (!house.getHost().getId().equals(host.getId())) {
            throw new RuntimeException("Only the host can populate users in his house");
        }
        List<User> occupants = this.userService.getByIds(occupantIds);
        house.getOccupants().addAll(occupants);
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
