package com.hunglm.address.mapping.entities.his;

import com.hunglm.address.mapping.entities.ym.Speciality;
import java.time.Instant;
import java.util.UUID;
import lombok.Data;
import vn.davido.youmed.entity.AvailableStatus;

@Data
public class HisSpeciality {

  private String code;
  private Integer fee;
  private String id;
  private String name;

  public Speciality toSpeciality(String serviceId) {
    Instant now = Instant.now();
    return new Speciality()
      .setId(UUID.randomUUID().toString())
      .setName(name)
      .setHisId(id)
      .setFee(fee)
      .setOwner(serviceId)
      .setServiceId(serviceId)
      .setAvailableStatus(AvailableStatus.CREATED)
      .setBooking(true)
      .setIndex(0)
      .setCreatedAt(now)
      .setUpdatedAt(now);
  }


}
