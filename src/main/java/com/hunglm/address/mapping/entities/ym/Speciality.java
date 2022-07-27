package com.hunglm.address.mapping.entities.ym;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.Instant;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;
import vn.davido.youmed.entity.AvailableStatus;

@Data
@Accessors(chain = true)
@Document(value = "specialities")
public class Speciality {

  @Id
  @Field(targetType = FieldType.STRING)
  @JsonProperty("_id")
  private String id;
  private String serviceId;
  private String owner;
  private String hisId;
  private String name;
  private Integer numberOfAttendants;
  private Integer fee;
  private String photo;
  private String intro;
  private AvailableStatus availableStatus;
  private Instant createdAt;
  private Instant updatedAt;
  private Boolean booking;
  private String flow;
  private Integer index;
  private String callSpecialityId;
}
