package com.hunglm.address.mapping.services;

import com.hunglm.address.mapping.configs.HisConnect;
import com.hunglm.address.mapping.entities.his.HisCity;
import com.hunglm.address.mapping.entities.his.HisDistrict;
import com.hunglm.address.mapping.entities.his.HisWard;
import com.hunglm.address.mapping.repositories.HisCityRepository;
import com.hunglm.address.mapping.repositories.HisDistrictRepository;
import com.hunglm.address.mapping.repositories.HisWardRepository;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.task.TaskExecutor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class HisConnectService {
    private final HisConnect hisConnect;
    private final RestTemplate restTemplate;
    private final HisCityRepository hisCityRepository;
    private final HisDistrictRepository hisDistrictRepository;
    private final HisWardRepository hisWardRepository;
    private final TaskExecutor taskExecutor;


    public ResponseEntity<String> getAllHisCities() {
        ResponseEntity<String> response = restTemplate.getForEntity(hisConnect.getHost() + "/api/v1/cities", String.class);
        log.info("Response: {}", response.getBody());
        JsonObject resJo = new JsonObject(response.getBody());
        JsonArray cities = resJo
                .getJsonObject("result", new JsonObject())
                .getJsonArray("cities", new JsonArray());
        if (!cities.isEmpty()) {
            clearAllHisData();
            for (int i = 0; i < cities.size(); i++) {
                JsonObject city = cities.getJsonObject(i);
                HisCity hisCity = city.mapTo(HisCity.class);
                hisCityRepository.save(hisCity);
                log.info("HisCity: {}", hisCity);
                taskExecutor.execute(() -> getHisDistrict(hisCity.getId()));
            }
        }
        return response;
    }

    private void clearAllHisData() {
        hisCityRepository.deleteAll();
        hisDistrictRepository.deleteAll();
        hisWardRepository.deleteAll();
    }

    public ResponseEntity<String> getHisDistrict(String cityId) {
        Map<String, Object> params = new HashMap();
        params.put("cityId", cityId);
        URI uri = UriComponentsBuilder.fromUriString(hisConnect.getHost() + "/api/v1/districts?cityId={cityId}")
                .build(params);
        ResponseEntity<String> response = restTemplate.getForEntity(uri, String.class);
        log.info("District of cityId={}. Result: {}", cityId, response.getBody());
        JsonObject resJo = new JsonObject(response.getBody());
        JsonArray districts = resJo.getJsonObject("result", new JsonObject()).getJsonArray("districts", new JsonArray());
        if (!districts.isEmpty()) {
            for (int i = 0; i < districts.size(); i++) {
                JsonObject district = districts.getJsonObject(i);
                HisDistrict hisDistrict = district.mapTo(HisDistrict.class);
                hisDistrict.setId(district.getString("id"));
                hisDistrict.setCityId(cityId);
                log.info("hisDistrictCity: {}", hisDistrict);
                hisDistrictRepository.save(hisDistrict);
                taskExecutor.execute(() -> getHisWard(hisDistrict.getId()));
            }
        }
        return response;
    }

    public ResponseEntity<String> getHisWard(String districtId) {
        Map<String, Object> params = new HashMap<>();
        params.put("districtId", districtId);
        URI uri = UriComponentsBuilder.fromUriString(hisConnect.getHost() + "/api/v1/wards?districtId={districtId}")
                .build(params);
        ResponseEntity<String> response = restTemplate.getForEntity(uri, String.class);
        log.info("Ward of district={}. Result: {}", districtId, response.getBody());
        JsonObject resJo = new JsonObject(response.getBody());
        JsonArray wards = resJo.getJsonObject("result", new JsonObject()).getJsonArray("wards", new JsonArray());
        if (!wards.isEmpty()) {
            for (int i = 0; i < wards.size(); i++) {
                JsonObject ward = wards.getJsonObject(i);
                HisWard hisWard = ward.mapTo(HisWard.class);
                hisWard.setDistrictId(districtId);
                log.info("hisWard: {}", hisWard);
                hisWardRepository.save(hisWard);
            }
        }
        return response;
    }
}
