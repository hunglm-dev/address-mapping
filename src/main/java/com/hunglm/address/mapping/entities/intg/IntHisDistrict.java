package com.hunglm.address.mapping.entities.intg;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.time.Instant;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.mongodb.core.mapping.Document;
import vn.davido.youmed.entity.enums.HisSyncStatus;

@Document("hisDistricts")
@Getter
@Setter
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class IntHisDistrict {

  private String id;
  private String hisConnectId;
  private String cityId;
  private String hisCityId;
  private String districtId;
  private String hisDistrictId;
  private String name;
  private String hisName;
  private String type;
  private HisSyncStatus hisSyncStatus;
  private String failedMessage;
  private Boolean manualFixed;
  private Boolean syncSucceeded;
  private Instant createdAt;
  private Instant updatedAt;
}
