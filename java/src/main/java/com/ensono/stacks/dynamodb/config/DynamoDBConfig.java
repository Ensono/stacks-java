package com.ensono.stacks.dynamodb.config;

#if USE_DYNAMODB
import static com.amazonaws.util.StringUtils.isNullOrEmpty;

import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import com.amazonaws.client.builder.AwsClientBuilder.EndpointConfiguration;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import org.socialsignin.spring.data.dynamodb.repository.config.EnableDynamoDBRepositories;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
#endif

import org.springframework.context.annotation.Configuration;

#if USE_DYNAMODB
@Configuration
@ConfigurationProperties(prefix = "amazon.dynamodb")
@EnableDynamoDBRepositories(basePackages = "com.amido.stacks.workloads.menu.repository")
public class DynamoDBConfig {

    @Value("${amazon.dynamodb.endpoint}")
    private String dynamoDbEndpoint;

    @Value("${amazon.dynamodb.signingRegion}")
    private String signingRegion;

    @Bean
    public AmazonDynamoDB amazonDynamoDB() {

        AmazonDynamoDBClientBuilder clientBuilder =
                AmazonDynamoDBClientBuilder.standard()
                        .withCredentials(new DefaultAWSCredentialsProviderChain());

        if (!isNullOrEmpty(dynamoDbEndpoint)) {
            clientBuilder.withEndpointConfiguration(
                    new EndpointConfiguration(dynamoDbEndpoint, signingRegion));
        }

        return clientBuilder.build();
    }
}
#else
// Placeholder configuration class to ensure Maven compilation when Dynamo is not selected.
// The entire Dynamo folder, including this file, is filtered out with project-builder-config.
@Configuration
public class DynamoDBConfig {}
#endif
