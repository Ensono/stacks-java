package com.xxAMIDOxx.xxSTACKSxx.repository.azure;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "azure.cosmosdb")
public class CosmosDbProperties {

    private String uri;
    private String key;
    private String secondaryKey;
    private String database;

    private boolean populateQueryMetrics;
}
