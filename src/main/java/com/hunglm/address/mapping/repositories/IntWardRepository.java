package com.hunglm.address.mapping.repositories;

import com.hunglm.address.mapping.entities.intg.IntHisWard;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface IntWardRepository extends MongoRepository<IntHisWard, String> {

  void deleteAllByHisConnectId(String hisConnectId);
}
