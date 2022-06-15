package com.hunglm.address.mapping.services;

import com.hunglm.address.mapping.entities.mapped.MappedDistrict;
import com.hunglm.address.mapping.entities.ym.City;
import com.hunglm.address.mapping.entities.ym.District;
import com.hunglm.address.mapping.entities.ym.Ward;
import com.hunglm.address.mapping.models.CityReport;
import com.hunglm.address.mapping.models.DistrictReport;
import com.hunglm.address.mapping.models.WardReport;
import com.hunglm.address.mapping.repositories.*;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ReportService {
    private final CityRepository cityRepository;
    private final HisCityRepository hisCityRepository;
    private final DistrictRepository districtRepository;
    private final HisDistrictRepository hisDistrictRepository;
    private final WardRepository wardRepository;
    private final HisWardRepository hisWardRepository;
    private final MappedCityRepository mappedCityRepository;
    private final MappedDistrictRepository mappedDistrictRepository;
    private final MappedWardRepository mappedWardRepository;

    public CityReport cityReport() {
        CityReport cityReport = new CityReport();
        cityReport.setYoumedCount(cityRepository.count());
        cityReport.setHisCount(hisCityRepository.count());
        cityReport.setMapped(mappedCityRepository.count());
        List<City> cities = cityRepository.findAll();
        cityReport.setNotMappedCities(cities.stream()
                .filter(city -> !mappedCityRepository.findByCityId(city.getId()).isPresent())
                .collect(Collectors.toList()));
        return cityReport;
    }

    public DistrictReport districtReport() {
        DistrictReport districtReport = new DistrictReport();
        districtReport.setYoumedCount(districtRepository.count());
        districtReport.setHisCount(hisDistrictRepository.count());
        districtReport.setMapped(mappedDistrictRepository.count());
        List<District> districts = districtRepository.findAll();
        districtReport.setNotMapped(districts.stream()
                .filter(district -> !mappedDistrictRepository.findByDistrictId(district.getId()).isPresent())
                .collect(Collectors.toList()));
        return districtReport;
    }

    public WardReport wardReport() {
        WardReport wardReport = new WardReport();
        wardReport.setYoumedCount(wardRepository.count());
        wardReport.setHisCount(hisWardRepository.count());
        wardReport.setMapped(mappedWardRepository.count());
        wardReport.setNotMapped(new ArrayList<>());
        Page<Ward> page;
        Pageable pageable = Pageable.ofSize(100);
        do {
            page = wardRepository.findAll(pageable);
            List<Ward> list = page.get().collect(Collectors.toList());
            List<Ward> notMapped = list.stream()
                    .filter(district -> !mappedDistrictRepository.findByDistrictId(district.getId()).isPresent())
                    .collect(Collectors.toList());
            wardReport.getNotMapped().addAll(notMapped);
            pageable = pageable.next();
        } while (page.hasNext());
        return wardReport;
    }


}
