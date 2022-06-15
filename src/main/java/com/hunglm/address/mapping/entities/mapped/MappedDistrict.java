package com.hunglm.address.mapping.entities.mapped;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.hunglm.address.mapping.entities.his.HisDistrict;
import com.hunglm.address.mapping.entities.ym.District;
import java.time.Instant;
import java.util.UUID;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;

@Document("mappedDistricts")
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@Accessors(chain = true)
@NoArgsConstructor
public class MappedDistrict {

  private String id = UUID.randomUUID().toString();
  private String cityId;
  private String hisCityId;
  private String hisConnectId;
  private String districtId;
  private String hisDistrictId;
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


  public MappedDistrict(District district) {
    cityId = district.getCityId();
    name = district.getName();
    type = district.getType();
    districtId = district.getId();
  }

  public void mapHis(HisDistrict hisDistrict) {
    hisCityId = hisDistrict.getCityId();
    hisName = hisDistrict.getName();
    hisDistrictId = hisDistrict.getId();
  }


}
