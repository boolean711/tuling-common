package com.tuling.common.core.properties;


import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Properties;

@Component
@ConfigurationProperties(prefix = "tenant")
@Data
public class TenantProperties implements Serializable {


    private static final long serialVersionUID = 7947048018455643545L;
    private boolean enable;
    private List<String> tableName;


}
