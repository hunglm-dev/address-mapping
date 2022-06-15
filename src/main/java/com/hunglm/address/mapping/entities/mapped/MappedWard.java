package com.hunglm.address.mapping.entities.mapped;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.hunglm.address.mapping.entities.his.HisWard;
import com.hunglm.address.mapping.entities.ym.Ward;
import java.time.Instant;
import java.util.UUID;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;

@Document("mappedWards")
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@Accessors(chain = true)
@NoArgsConstructor
public class MappedWard {

  private String id = UUID.randomUUID().toString();
  private String districtId;
  private String hisDistrictId;
  private String wardId;
  private String hisWardId;
  private String name;
  private String hisName;
  private String type;
  private String hisSyncStatus = "VALID";
  private boolean manualFixed;
  private boolean syncSucceeded;
  private boolean matchedTerm;
  @Field(targetType = FieldType.STRING)
  private Instant createdAt = Instant.now();
  @Field(targetType = FieldType.STRING)
  private Instant updatedAt = Instant.now();

  public MappedWard(Ward ward) {
    districtId = ward.getDistrictId();
    wardId = ward.getId();
    type = ward.getType();
    name = ward.getName();
  }

  public void mapHis(HisWard hisWard) {
    hisName = hisWard.getName();
    hisWardId = hisWard.getId();
    hisDistrictId = hisWard.getDistrictId();
  }
}
