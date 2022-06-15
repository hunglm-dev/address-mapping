package com.hunglm.address.mapping.services;

import co.elastic.clients.elasticsearch.core.search.Hit;
import com.hunglm.address.mapping.configs.AddressDictionary;
import com.hunglm.address.mapping.entities.his.HisCity;
import com.hunglm.address.mapping.entities.his.HisDistrict;
import com.hunglm.address.mapping.entities.his.HisWard;
import com.hunglm.address.mapping.entities.mapped.MappedCity;
import com.hunglm.address.mapping.entities.mapped.MappedDistrict;
import com.hunglm.address.mapping.entities.mapped.MappedWard;
import com.hunglm.address.mapping.entities.ym.City;
import com.hunglm.address.mapping.entities.ym.District;
import com.hunglm.address.mapping.entities.ym.Ward;
import com.hunglm.address.mapping.repositories.*;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class HisMappingService {

  private final ElasticSearchService elastic;

  private final AddressDictionary addressDictionary;
  private final CityRepository cityRepository;
  private final DistrictRepository districtRepository;
  private final WardRepository wardRepository;

  private final MappedCityRepository mappedCityRepository;
  private final MappedDistrictRepository mappedDistrictRepository;
  private final MappedWardRepository mappedWardRepository;
  private static final int BATCH_SIZE = 100;

  public void mappingCities() {
    mappedCityRepository.deleteAll();
    List<City> list = cityRepository.findAll();
    list.forEach(city -> {
      try {
        HisCity hisCity = null;
        MappedCity mappedCity = new MappedCity(city);
        List<Hit<HisCity>> hitsTerm = elastic.searchTermCity(city.getName()).hits();
        if (!hitsTerm.isEmpty()) {
          hisCity = hitsTerm.get(0).source();
          mappedCity.setMatchedTerm(true);
        } else {
          log.warn("Search Term notfound. Try to search match");
          List<Hit<HisCity>> hitsMatch = elastic.searchCity(city.getName()).hits();
          if (!hitsMatch.isEmpty()) {
            hisCity = hitsMatch.get(0).source();
            log.warn("Found Match hisCity {} from YoumedCity {}", hisCity, city);
          }
        }
        if (hisCity != null) {
          mappedCity
              .setSyncSucceeded(true)
              .mapHis(hisCity);
          mappedCityRepository.save(mappedCity);
        }
      } catch (IOException e) {
        log.error("Cannot find city {}. Cause: ", city, e);
      }
    });
  }

  public void mappingDistricts() {
    List<District> list = districtRepository.findAll();
    mappedDistrictRepository.deleteAll();
    list.forEach(district -> {
      try {
        Optional<MappedCity> optionalMappedCity = mappedCityRepository
            .findByCityId(district.getCityId());
        if (optionalMappedCity.isPresent()) {
          processMappingDistrict(optionalMappedCity.get(), district);
        } else {
          log.error("Cannot find Mapped City with district={}", district);
        }
      } catch (IOException e) {
        log.error("Cannot find district {}. Cause: ", district, e);
      }
    });
  }

  private void processMappingDistrict(MappedCity mappedCity, District district) throws IOException {
    MappedDistrict mappedDistrict = new MappedDistrict(district);
    HisDistrict hisDistrict = proccessDistrictSearchTerm(mappedCity, district);
    if (hisDistrict != null) {
      mappedDistrict.setMatchedTerm(true);
    } else {
      hisDistrict = processDistrictSearchMatch(mappedCity, district);
    }
    if (hisDistrict != null) {
      mappedDistrict
          .setSyncSucceeded(true)
          .mapHis(hisDistrict);
      mappedDistrictRepository.save(mappedDistrict);
    } else {
      handleUndefinedDistrict(mappedCity, district);
    }
  }

  private HisDistrict proccessDistrictSearchTerm(MappedCity mappedCity, District district) throws IOException {
    HisDistrict hisDistrict = null;
    List<Hit<HisDistrict>> hitsTerm = elastic.searchTermDistrict(mappedCity.getHisCityId(), district.getName()).hits();
    if (!hitsTerm.isEmpty()) {
      hisDistrict = hitsTerm.get(0).source();
    }
    return hisDistrict;
  }

  private HisDistrict processDistrictSearchMatch(MappedCity mappedCity, District district) throws IOException {
    HisDistrict hisDistrict = null;
    List<Hit<HisDistrict>> hitsMatch = elastic.searchDistrict(mappedCity.getHisCityId(), district.getName()).hits();
    if (!hitsMatch.isEmpty()) {
      hisDistrict = hitsMatch.get(0).source();
      assert hisDistrict != null;
      log.warn("DISTRICT HIS={} YM={} | CHid={} CYid={} | val={}",
          hisDistrict.getName(), district.getName(),
          mappedCity.getCityId(), mappedCity.getHisCityId(),
          elastic.normalizedSearch(district.getName(), addressDictionary.getDistrictDic()));
    } else {
      log.warn("Cannot find District {} | mappedCity {}. SearchValue={}", district, mappedCity,
          elastic.normalizedSearch(district.getName(), addressDictionary.getDistrictDic()));
    }
    return hisDistrict;
  }

  private void handleUndefinedDistrict(MappedCity mappedCity, District district) throws IOException {
    log.warn("Handle Undefined district. MappedCity: {}", mappedCity);
    String undefinedCityName = "Không xác định";
    District undefinedDistrict = new District();
    undefinedDistrict.setName(undefinedCityName);
    HisDistrict hisDistrict = processDistrictSearchMatch(mappedCity, undefinedDistrict);
    MappedDistrict mappedDistrict = new MappedDistrict(district);
    if (hisDistrict != null) {
      mappedDistrict
          .setSyncSucceeded(true)
          .mapHis(hisDistrict);
      mappedDistrictRepository.save(mappedDistrict);
    }
  }

  public void mappingWards() {
    mappedWardRepository.deleteAll();
    Page<Ward> page;
    Pageable pageable = Pageable.ofSize(BATCH_SIZE);
    do {
      page = wardRepository.findAll(pageable);
      List<Ward> list = page.get().collect(Collectors.toList());
      list.forEach(ward -> {
        try {
          Optional<MappedDistrict> optionalMappedDistrict = mappedDistrictRepository.findByDistrictId(ward.getDistrictId());
          if (optionalMappedDistrict.isPresent()) {
            processMappingWard(optionalMappedDistrict.get(), ward);
          } else {
            log.error("Cannot find Mapp District with ward={}", ward);
          }
        } catch (IOException e) {
          log.error("Cannot find Ward, cause: ", e);
        }
      });
      pageable = pageable.next();
    } while (page.hasNext());
    log.info("Mapped District Done");
  }

  private void processMappingWard(MappedDistrict mappedDistrict, Ward ward) throws IOException {
    MappedWard mappedWard = new MappedWard(ward);
    HisWard hisWard = processWardSearchTerm(mappedDistrict, ward);
    if (hisWard != null) {
      mappedWard.setMatchedTerm(true);
    } else {
      hisWard = processWardSearchMatch(mappedDistrict, ward);
    }
    if (hisWard != null) {
      mappedWard
          .setSyncSucceeded(true)
          .mapHis(hisWard);
      mappedWardRepository.save(mappedWard);
    } else {
      handleUndefinedWard(mappedDistrict, ward);
    }
  }

  private void handleUndefinedWard(MappedDistrict mappedDistrict, Ward ward) throws IOException {
    log.warn("Handle Undefined ward. MappedDistrict: {}", mappedDistrict);
    String undefinedWardName = "Không xác định";
    Ward undefinedWard = new Ward();
    undefinedWard.setName(undefinedWardName);
    HisWard hisWard = processWardSearchMatch(mappedDistrict, undefinedWard);
    MappedWard mappedWard = new MappedWard(ward);
    if (hisWard != null) {
      mappedWard
          .setSyncSucceeded(false)
          .mapHis(hisWard);
      mappedWardRepository.save(mappedWard);
    }
  }

  private HisWard processWardSearchMatch(MappedDistrict mappedDistrict, Ward ward) throws IOException {
    HisWard hisWard = null;
    List<Hit<HisWard>> hitsMatch = elastic.searchMatchHisWard(mappedDistrict.getHisDistrictId(), ward.getName()).hits();
    if (!hitsMatch.isEmpty()) {
      hisWard = hitsMatch.get(0).source();
      assert hisWard != null;
      log.warn("WARD HIS={} YM={} | DHid={} DYid={} | val={}",
          hisWard.getName(), ward.getName(),
          mappedDistrict.getHisDistrictId(), mappedDistrict.getDistrictId(),
          elastic.normalizedSearch(ward.getName(), addressDictionary.getWardDic()));
    } else {
      log.warn("Cannot find District {} | mappedCity {}. SearchValue={}", ward, mappedDistrict,
          elastic.normalizedSearch(ward.getName(), addressDictionary.getWardDic()));
    }
    return hisWard;
  }

  private HisWard processWardSearchTerm(MappedDistrict mappedDistrict, Ward ward) throws IOException {
    HisWard hisWard = null;
    List<Hit<HisWard>> hitsTerm = elastic.searchTermHisWard(mappedDistrict.getHisDistrictId(), ward.getName()).hits();
    if (!hitsTerm.isEmpty()) {
      hisWard = hitsTerm.get(0).source();
    }
    return hisWard;
  }


}
