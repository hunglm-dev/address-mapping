package com.hunglm.address.mapping.repositories;

import com.hunglm.address.mapping.entities.ym.Speciality;
import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface SpecialityRepository extends MongoRepository<Speciality, String> {

  Optional<Speciality> findByServiceIdAndHisId(String serviceId, String hisId);

}
