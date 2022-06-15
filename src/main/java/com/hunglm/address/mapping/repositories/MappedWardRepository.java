package com.hunglm.address.mapping.repositories;

import com.hunglm.address.mapping.entities.mapped.MappedWard;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface MappedWardRepository extends MongoRepository<MappedWard, String> {
}
