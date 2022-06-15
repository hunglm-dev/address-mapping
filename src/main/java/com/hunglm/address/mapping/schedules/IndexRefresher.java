package com.hunglm.address.mapping.schedules;

import com.hunglm.address.mapping.configs.AddressDictionary;
import com.hunglm.address.mapping.constants.IndexName;
import com.hunglm.address.mapping.entities.Normalizeable;
import com.hunglm.address.mapping.entities.his.HisCity;
import com.hunglm.address.mapping.entities.his.HisDistrict;
import com.hunglm.address.mapping.entities.his.HisWard;
import com.hunglm.address.mapping.entities.ym.District;
import com.hunglm.address.mapping.entities.ym.Ward;
import com.hunglm.address.mapping.repositories.*;
import com.hunglm.address.mapping.services.ElasticSearchService;
import com.hunglm.address.mapping.utils.StringUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;


@Service
@AllArgsConstructor
@Slf4j
public class IndexRefresher {
    private final CityRepository cityRepository;

    private final HisCityRepository hisCityRepository;
    private final DistrictRepository districtRepository;
    private final HisDistrictRepository hisDistrictRepository;
    private final WardRepository wardRepository;
    private final HisWardRepository hisWardRepository;
    private final ElasticSearchService elasticSearchService;

    private final AddressDictionary dictionary;

    private static final int BATCH_SIZE = 100;

    public void refreshingAllESData() throws IOException {
        refreshingESData();
        refreshingHisESData();
    }

    public void refreshingHisESData() throws IOException {
        refreshHisESCity();
        refreshHisESDistrict();
        refreshHisESWard();


    }

    public void refreshingESData() throws IOException {
        refreshESCity();
        refreshESDistrict();
        refreshESWard();
    }

    private void refreshESCity() throws IOException {
        log.info("Refreshing ES YM City");
        final String indexName = IndexName.YM_CITIES;
        elasticSearchService.deleteIndex(indexName);
        elasticSearchService.insertBulk(indexName, cityRepository.findAll());
        log.info("Done refreshingESCity.");
    }

    private void refreshESDistrict() throws IOException {
        final String indexName = IndexName.YM_DISTRICTS;
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
        final String indexName = IndexName.YM_WARDS;
        elasticSearchService.deleteIndex(indexName);
        Page<Ward> page;
        Pageable pageable = Pageable.ofSize(BATCH_SIZE);
        do {
            page = wardRepository.findAll(pageable);
            elasticSearchService.insertBulk(indexName, page.get().collect(Collectors.toList()));
            pageable = pageable.next();
        } while (page.hasNext());
        log.info("Done refreshingESWard.");
    }

    private void refreshHisESCity() throws IOException {
        log.info("Refreshing ES --HIS-- City");
        final String indexName = IndexName.HIS_CITIES;
        elasticSearchService.deleteIndex(indexName);
        List<HisCity> hisCities = hisCityRepository.findAll();
        hisCities.forEach(hisCity -> {
            normalizedName(hisCity, dictionary.getCityDic());
        });
        elasticSearchService.insertBulk(indexName, hisCities);
        log.info("Done refreshing--HIS--ESCity.");
    }

    private void refreshHisESDistrict() throws IOException {
        final String indexName = IndexName.HIS_DISTRICTS;
        log.info("Refreshing ES --HIS-- District");
        elasticSearchService.deleteIndex(indexName);
        Page<HisDistrict> page;
        Pageable pageable = Pageable.ofSize(BATCH_SIZE);
        do {
            page = hisDistrictRepository.findAll(pageable);
            List<HisDistrict> list = page.get().collect(Collectors.toList());
            list.forEach(hisDistrict -> normalizedName(hisDistrict, dictionary.getDistrictDic()));
            elasticSearchService.insertBulk(indexName, list);
            pageable = pageable.next();
        } while (page.hasNext());
        log.info("Done refreshing--HIS--ESDistrict.");
    }

    private void refreshHisESWard() throws IOException {
        log.info("Refreshing ES --HIS-- Ward");
        final String indexName = IndexName.HIS_WARDS;
        elasticSearchService.deleteIndex(indexName);
        Page<HisWard> page;
        Pageable pageable = Pageable.ofSize(BATCH_SIZE);
        do {
            page = hisWardRepository.findAll(pageable);
            List<HisWard> list = page.get().collect(Collectors.toList());
            list.forEach(hisWard -> normalizedName(hisWard, dictionary.getWardDic()));
            elasticSearchService.insertBulk(indexName, list);
            pageable = pageable.next();
        } while (page.hasNext());
        log.info("Done refreshing--HIS--ESWard.");
    }

    public void normalizedName(Normalizeable normalizeable, List<String> dictionary) {
        String finalName = normalizeable.getName();
        for (String dic : dictionary) {
            if (normalizeable.getName().startsWith(dic)) {
                finalName = finalName.replace(dic, "");
                break;
            }
        }
        String normalizedName = StringUtils.nomalizeUnicodeAndSpecialChars(finalName);
        normalizeable.setNormalizedName(normalizedName);
    }

}
