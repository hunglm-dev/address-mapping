package com.hunglm.address.mapping.repositories;

import com.hunglm.address.mapping.entities.his.HisCity;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface HisCityRepository extends MongoRepository<HisCity, String> {
}
