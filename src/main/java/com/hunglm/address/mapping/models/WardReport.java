package com.hunglm.address.mapping.models;

import com.hunglm.address.mapping.entities.ym.Ward;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class WardReport extends Report {
    private List<Ward> notMapped;
}
