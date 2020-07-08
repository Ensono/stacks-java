package com.xxAMIDOxx.xxSTACKSxx.repository;

import com.azure.data.cosmos.CosmosKeyCredential;
import com.microsoft.azure.spring.data.cosmosdb.config.AbstractCosmosConfiguration;
import com.microsoft.azure.spring.data.cosmosdb.config.CosmosDBConfig;
import com.microsoft.azure.spring.data.cosmosdb.core.ResponseDiagnostics;
import com.microsoft.azure.spring.data.cosmosdb.core.ResponseDiagnosticsProcessor;
import com.microsoft.azure.spring.data.cosmosdb.repository.config.EnableCosmosRepositories;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Nullable;

@Configuration
@EnableCosmosRepositories
public class CosmosDBConfiguration extends AbstractCosmosConfiguration {

    private static Logger logger = LoggerFactory.getLogger(CosmosDBConfiguration.class);

    @Value("${azure.cosmosdb.uri}")
    private String uri;

    @Value("${azure.cosmosdb.key}")
    private String key;

    @Value("${azure.cosmosdb.database}")
    private String dbName;

    @Value("${azure.cosmosdb.populateQueryMetrics}")
    private boolean populateQueryMetrics;

    @Bean
    public CosmosDBConfig getConfig() {
        CosmosKeyCredential cosmosKeyCredential = new CosmosKeyCredential(key);
        CosmosDBConfig cosmosdbConfig = CosmosDBConfig.builder(
                uri,
                cosmosKeyCredential,
                dbName)
                .build();
        cosmosdbConfig.setPopulateQueryMetrics(populateQueryMetrics);
        cosmosdbConfig.setResponseDiagnosticsProcessor(
                new ResponseDiagnosticsProcessorImplementation());
        return cosmosdbConfig;
    }

    private static class ResponseDiagnosticsProcessorImplementation implements ResponseDiagnosticsProcessor {
        @Override
        public void processResponseDiagnostics(@Nullable ResponseDiagnostics responseDiagnostics) {
            logger.debug("Response Diagnostics {}", responseDiagnostics);
        }
    }
}
