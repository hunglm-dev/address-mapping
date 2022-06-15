package com.hunglm.address.mapping.repositories;

import com.hunglm.address.mapping.entities.mapped.MappedDistrict;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface MappedDistrictRepository extends MongoRepository<MappedDistrict, String> {
    Optional<MappedDistrict> findByDistrictId(String districtId);
}
