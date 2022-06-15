package com.hunglm.address.mapping.configs;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@ConfigurationProperties(prefix = "app.dictionary")
@Data
public class AddressDictionary {
    private List<String> cityDic;
    private List<String> districtDic;
    private List<String> wardDic;
}
