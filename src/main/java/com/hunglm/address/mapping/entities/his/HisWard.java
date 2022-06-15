package com.hunglm.address.mapping.entities.his;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.hunglm.address.mapping.entities.ESDocument;
import com.hunglm.address.mapping.entities.Normalizeable;
import com.hunglm.address.mapping.utils.StringUtils;
import lombok.*;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.web.bind.annotation.GetMapping;

@Getter
@Setter
@ToString
@Document("175hisWards")
@JsonIgnoreProperties(ignoreUnknown = true)
public class HisWard extends ESDocument implements Normalizeable {

    @Id
    private String id;
    private String districtId;
    private String name;
    private String normalizedName;

    public void setNormalizedName(String normalizedName) {
        this.normalizedName = normalizedName;
    }

}
