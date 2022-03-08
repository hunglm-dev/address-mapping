package com.hunglm.address.mapping.entities.his;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Data
@Accessors(chain = true)
@Document("hisWards")
public class HisWard {
  @Id
  @JsonProperty("_id")
  private String id;
  private String hisConnectId;
  private String districtId;
  private String hisDistrictId;
  private String wardId;
  private String hisWardId;
  private String name;
  private String hisName;
  private String type;
  private String failedMessage;
  private Boolean manualFixed;
  private Boolean syncSucceeded;
  private Instant createdAt;
  private Instant updatedAt;
}
