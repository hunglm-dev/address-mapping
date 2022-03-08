package com.hunglm.address.mapping.repositories;

import com.hunglm.address.mapping.entities.ym.City;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CityRepository extends MongoRepository<City, String> {
}
