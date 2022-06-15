package com.hunglm.address.mapping.repositories;

import com.hunglm.address.mapping.entities.intg.IntHisDistrict;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface IntDistrictRepository extends MongoRepository<IntHisDistrict, String> {

  void deleteAllByHisConnectId(String hisConnectId);
}
