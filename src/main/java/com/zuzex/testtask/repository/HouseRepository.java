package com.zuzex.testtask.repository;

import com.zuzex.testtask.entity.House;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface HouseRepository extends CrudRepository<House, Long> {
    @Override
    List<House> findAll();
}
