package com.hunglm.address.mapping.entities.his;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Document("hisCities")
@Data
@Accessors(chain = true)
public class HisCity {
  @JsonProperty("_id")
  @Id
  private String id;
  private String hisConnectId;
  private String cityId;
  private String hisCityId;
  private String name;
  private String hisName;
  private Integer index;
  private String type;
  private String failedMessage;
  private Boolean manualFixed;
  private Boolean syncSucceeded;
  private Instant createdAt;
  private Instant updatedAt;
}
