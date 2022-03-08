package com.hunglm.address.mapping.repositories;

import com.hunglm.address.mapping.entities.his.HisWard;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface HisWardRepository extends MongoRepository<HisWard, String> {
}
