package com.hunglm.address.mapping.repositories;

import com.hunglm.address.mapping.entities.ym.District;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface DistrictRepository extends MongoRepository<District, String> {
}
