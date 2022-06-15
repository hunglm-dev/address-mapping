package com.hunglm.address.mapping.entities.ym;

import com.hunglm.address.mapping.entities.ESDocument;
import com.hunglm.address.mapping.utils.StringUtils;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.data.mongodb.core.mapping.Document;

@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@Document("cities")
public class City extends ESDocument {
  private String id;
  private String name;
  private String type;
  private Integer index;
  private String normalizeName;

}
