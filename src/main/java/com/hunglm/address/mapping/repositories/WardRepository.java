package com.hunglm.address.mapping.repositories;

import com.hunglm.address.mapping.entities.ym.Ward;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface WardRepository  extends MongoRepository<Ward, String> {
}
