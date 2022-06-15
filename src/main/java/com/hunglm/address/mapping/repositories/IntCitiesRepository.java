package com.hunglm.address.mapping.repositories;

import com.hunglm.address.mapping.entities.intg.IntHisCity;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface IntCitiesRepository extends MongoRepository<IntHisCity, String> {
  void deleteAllByHisConnectId(String hisConnectId);

}
