package com.hunglm.address.mapping.controller;

import com.hunglm.address.mapping.models.CityReport;
import com.hunglm.address.mapping.models.DistrictReport;
import com.hunglm.address.mapping.models.WardReport;
import com.hunglm.address.mapping.services.ReportService;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class ReportController {
    private final ReportService reportService;

    @GetMapping("report-mapping-cities")
    public CityReport cityReport() {
        return reportService.cityReport();
    }

    @GetMapping("report-mapping-districts")
    public DistrictReport districtReport() {
        return reportService.districtReport();
    }

    @GetMapping("report-mapping-wards")
    public WardReport wardReport() {
        return reportService.wardReport();
    }
}
