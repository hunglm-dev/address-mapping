package com.hunglm.address.mapping.repositories;

import com.hunglm.address.mapping.entities.his.HisDistrict;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface HisDistrictRepository extends MongoRepository<HisDistrict, String> {
}
