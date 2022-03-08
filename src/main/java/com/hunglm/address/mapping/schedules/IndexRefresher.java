package com.hunglm.address.mapping.schedules;

import com.hunglm.address.mapping.entities.ym.District;
import com.hunglm.address.mapping.entities.ym.Ward;
import com.hunglm.address.mapping.repositories.CityRepository;
import com.hunglm.address.mapping.repositories.DistrictRepository;
import com.hunglm.address.mapping.repositories.WardRepository;
import com.hunglm.address.mapping.services.ElasticSearchService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;


@Service
@AllArgsConstructor
@Slf4j
public class IndexRefresher {
  private final CityRepository cityRepository;
  private final DistrictRepository districtRepository;
  private final WardRepository wardRepository;
  private final ElasticSearchService elasticSearchService;

  private static final int BATCH_SIZE = 100;
  private static final Executor executor = Executors.newFixedThreadPool(1);

  public void refreshingData() {
    executor.execute(() -> {
      try {
        refreshESCity();
        refreshESDistrict();
        refreshESWard();
      } catch (Exception e) {
        log.error("Error when refreshing ES data, cause: ", e);
        e.printStackTrace();
      }
    });
  }

  private void refreshESCity() throws IOException {
    log.info("Refreshing ES YM City");
    final String indexName = "ym-cities";
    elasticSearchService.deleteIndex(indexName);
    elasticSearchService.insertBulk(indexName, cityRepository.findAll());
    log.info("Done refreshingESCity.");
  }

  private void refreshESDistrict() throws IOException {
    final String indexName = "ym-districts";
    log.info("Refreshing ES YM District");
    elasticSearchService.deleteIndex(indexName);
    Page<District> page;
    Pageable pageable = Pageable.ofSize(BATCH_SIZE);
    do {
      page = districtRepository.findAll(pageable);
      elasticSearchService.insertBulk(indexName, page.get().collect(Collectors.toList()));
      pageable = pageable.next();
    } while (page.hasNext());
    log.info("Done refreshingESDistrict.");
  }

  private void refreshESWard() throws IOException {
    log.info("Refreshing ES YM Ward");
    final String indexName = "ym-wards";
    elasticSearchService.deleteIndex(indexName);
    Page<Ward> page;
    Pageable pageable = Pageable.ofSize(BATCH_SIZE);
    do {
      page = wardRepository.findAll(pageable);
      elasticSearchService.insertBulk(indexName, page.get().collect(Collectors.toList()));
      pageable = pageable.next();
    } while (page.hasNext());
    log.info("Done refreshingESWard.");
    elasticSearchService.searchTermCity("ym-cities", "Long An")
        .hits().forEach(cityMGHit -> log.info("Hit: {}", cityMGHit.source()));
  }

}
