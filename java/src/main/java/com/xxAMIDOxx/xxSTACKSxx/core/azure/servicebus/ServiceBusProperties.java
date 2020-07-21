package com.xxAMIDOxx.xxSTACKSxx.core.azure.servicebus;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotEmpty;

@Validated
@ConfigurationProperties("azure.servicebus")
public class ServiceBusProperties {
    /**
     * Service Bus connection string.
     */
    @NotEmpty
    @Value("connectionString")
    private String connectionString;

    /**
     * Topic name. Entity path of the topic.
     */
    @NotEmpty
    @Value("topicName")
    private String topicName;

    /**
     * Subscription name.
     */
    @NotEmpty
    @Value("subscriptionName")
    private String subscriptionName;

    public String getConnectionString() {
        return connectionString;
    }

    public String getTopicName() {
        return topicName;
    }

    public String getSubscriptionName() {
        return subscriptionName;
    }

    public void setConnectionString(String connectionString) {
        this.connectionString = connectionString;
    }

    public void setTopicName(String topicName) {
        this.topicName = topicName;
    }

    public void setSubscriptionName(String subscriptionName) {
        this.subscriptionName = subscriptionName;
    }
}

