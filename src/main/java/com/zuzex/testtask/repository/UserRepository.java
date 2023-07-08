package com.zuzex.testtask.repository;

import com.zuzex.testtask.entity.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {
    @Override
    List<User> findAll();

    @Override
    List<User> findAllById(Iterable<Long> ids);


    Optional<User> findByName(String name);
}
