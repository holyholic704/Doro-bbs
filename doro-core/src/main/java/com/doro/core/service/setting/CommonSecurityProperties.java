package com.doro.core.service.setting;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

/**
 * @author jiage
 */
@Data
@Component
@RefreshScope
@ConfigurationProperties(prefix = "common-security")
public class CommonSecurityProperties {

    private String gatewayHeader;
}
