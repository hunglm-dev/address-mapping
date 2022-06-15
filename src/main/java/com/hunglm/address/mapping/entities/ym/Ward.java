package com.hunglm.address.mapping.entities.ym;

import com.hunglm.address.mapping.entities.ESDocument;
import com.hunglm.address.mapping.utils.StringUtils;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@Document("wards")
public class Ward extends ESDocument {
    @Id
    private String id;
    private String name;
    private String type;
    private String districtId;
    private String normalizeName;

    public void setName(String name) {
        this.name = name;
        this.normalizeName = StringUtils.convertUnicodeToEngString(name);
    }
}
