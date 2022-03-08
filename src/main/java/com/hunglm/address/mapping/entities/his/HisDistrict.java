package com.hunglm.address.mapping.entities.his;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
@Data
@Accessors(chain = true)
@Document("hisDistricts")
public class HisDistrict {
  @JsonProperty("_id")
  @Id
  private String id;
  private String hisConnectId;
  private String cityId;
  private String hisCityId;
  private String districtId;
  private String hisDistrictId;
  private String name;
  private String hisName;
  private String type;
  private String failedMessage;
  private Boolean manualFixed;
  private Boolean syncSucceeded;
  private Instant createdAt;
  private Instant updatedAt;
}
