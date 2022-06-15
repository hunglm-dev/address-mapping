package com.hunglm.address.mapping.models;

import com.hunglm.address.mapping.entities.ym.City;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CityReport extends Report {
    private List<City> notMappedCities;
}
