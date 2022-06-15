package com.hunglm.address.mapping.repositories;

import com.hunglm.address.mapping.entities.mapped.MappedCity;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface MappedCityRepository extends MongoRepository<MappedCity, String> {

    Optional<MappedCity> findByCityId(String cityId);

}
