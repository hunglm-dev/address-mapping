package com.hunglm.address.mapping.entities.mapped;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.hunglm.address.mapping.entities.his.HisCity;
import com.hunglm.address.mapping.entities.ym.City;
import java.time.Instant;
import java.util.UUID;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;

@Document("mappedCities")
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@Accessors(chain = true)
@NoArgsConstructor
public class MappedCity {

  private String id = UUID.randomUUID().toString();
  private String hisConnectId;
  private String cityId;
  private String hisCityId;
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

  public MappedCity(City city) {
    cityId = city.getId();
    name = city.getName();
    type = city.getType();
  }

  public void mapHis(HisCity hisCity) {
    hisCityId = hisCity.getId();
    hisName = hisCity.getName();
  }
}
