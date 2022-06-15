package com.hunglm.address.mapping.controller;

import com.hunglm.address.mapping.schedules.IndexRefresher;
import com.hunglm.address.mapping.services.HisConnectService;
import com.hunglm.address.mapping.services.HisMappingService;
import com.hunglm.address.mapping.services.IntegrateService;
import java.io.IOException;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class BaseController {

  private final HisConnectService hisConnectService;
  private final IndexRefresher indexRefresher;
  private final HisMappingService hisMappingService;
  private final IntegrateService integrateService;

  @GetMapping("clone-his-address")
  public ResponseEntity<String> cloneHisAddress() {
    hisConnectService.getAllHisCities();
    return ResponseEntity.ok("OK");
  }

  @GetMapping("refreshing-es-index")
  public ResponseEntity<String> refreshingESIndex() throws IOException {
    indexRefresher.refreshingAllESData();
    return ResponseEntity.ok("OK");
  }

  @GetMapping("mapping-cities")
  public ResponseEntity<String> mappingCities() {
    hisMappingService.mappingCities();
    return ResponseEntity.ok("OK");
  }

  @GetMapping("mapping-districts")
  public ResponseEntity<String> mappingDistricts() {
    hisMappingService.mappingDistricts();
    return ResponseEntity.ok("OK");
  }

  @GetMapping("mapping-wards")
  public ResponseEntity<String> mappingWards() {
    hisMappingService.mappingWards();
    return ResponseEntity.ok("OK");
  }

  @GetMapping("integrate-cities")
  public ResponseEntity<String> integrateCities(@RequestParam(value = "hisConnectId", required = true) String hisConnectId) {
    integrateService.integrateCities(hisConnectId);
    return ResponseEntity.ok("OK");
  }

  @GetMapping("integrate-districts")
  public ResponseEntity<String> integrateDistricts(@RequestParam(value = "hisConnectId", required = true) String hisConnectId) {
    integrateService.integrateDistricts(hisConnectId);
    return ResponseEntity.ok("OK");
  }

  @GetMapping("integrate-wards")
  public ResponseEntity<String> integrateWards(@RequestParam(value = "hisConnectId", required = true) String hisConnectId) {
    integrateService.integreateWards(hisConnectId);
    return ResponseEntity.ok("OK");
  }
}
