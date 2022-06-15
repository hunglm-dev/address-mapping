package com.hunglm.address.mapping.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hunglm.address.mapping.entities.intg.IntHisCity;
import com.hunglm.address.mapping.entities.intg.IntHisDistrict;
import com.hunglm.address.mapping.entities.intg.IntHisWard;
import com.hunglm.address.mapping.repositories.IntCitiesRepository;
import com.hunglm.address.mapping.repositories.IntDistrictRepository;
import com.hunglm.address.mapping.repositories.IntWardRepository;
import com.hunglm.address.mapping.repositories.MappedCityRepository;
import com.hunglm.address.mapping.repositories.MappedDistrictRepository;
import com.hunglm.address.mapping.repositories.MappedWardRepository;
import io.swagger.models.auth.In;
import io.vertx.core.json.JsonObject;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import vn.davido.youmed.entity.HisCityEntity;
import vn.davido.youmed.entity.HisDistrictEntity;
import vn.davido.youmed.entity.HisWardEntity;

@Slf4j
@Service
@RequiredArgsConstructor
public class IntegrateService {

  private final MappedCityRepository mappedCityRepository;
  private final MappedDistrictRepository mappedDistrictRepository;
  private final MappedWardRepository mappedWardRepository;
  private final IntCitiesRepository intCitiesRepository;
  private final IntDistrictRepository intDistrictRepository;
  private final IntWardRepository intWardRepository;

  public void integrateCities(String hisConnectId) {
    log.info("integrateCities");
    intCitiesRepository.deleteAllByHisConnectId(hisConnectId);
    mappedCityRepository.findAll()
        .forEach(mappedCity -> {
          IntHisCity hisCity = JsonObject.mapFrom(mappedCity).mapTo(IntHisCity.class);
          hisCity.setHisConnectId(hisConnectId);
          log.info("IntCity; {}", hisCity);
          intCitiesRepository.save(hisCity);
        });
    log.info("integrateCities DONE");
  }

  public void integrateDistricts(String hisConnectId) {
    log.info("integrateDistricts");
    intDistrictRepository.deleteAllByHisConnectId(hisConnectId);
    mappedDistrictRepository.findAll().parallelStream()
        .forEach(mappedDistrict -> {
          IntHisDistrict hisDistrict = JsonObject.mapFrom(mappedDistrict).mapTo(IntHisDistrict.class);
          hisDistrict.setHisConnectId(hisConnectId);
          log.info("hisDistrict: {}", hisDistrict);
          intDistrictRepository.save(hisDistrict);
        });
    log.info("integrateDistricts DONE");

  }

  public void integreateWards(String hisConnectId) {
    log.info("integreateWards");
    intWardRepository.deleteAllByHisConnectId(hisConnectId);
    mappedWardRepository.findAll().parallelStream()
        .forEach(mappedWard -> {
          IntHisWard hisWard = JsonObject.mapFrom(mappedWard).mapTo(IntHisWard.class);
          hisWard.setHisConnectId(hisConnectId);
          log.info("hisWard: {}", hisWard);
          intWardRepository.save(hisWard);
        });
    log.info("integreateWards DONE");

  }


}
