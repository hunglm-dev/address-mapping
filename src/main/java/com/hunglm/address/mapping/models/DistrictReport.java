package com.hunglm.address.mapping.models;

import com.hunglm.address.mapping.entities.ym.District;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class DistrictReport extends Report {
    List<District> notMapped;
}
