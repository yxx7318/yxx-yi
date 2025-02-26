package com.yxx.framework.web.domain;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@ConfigurationProperties(prefix = "server")
@Data
@Component
public class ServerProperties {

    private List<Integer> ports;

    private List<String> appNames;
}
