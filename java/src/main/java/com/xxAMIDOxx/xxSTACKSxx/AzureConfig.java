package com.xxAMIDOxx.xxSTACKSxx;

import com.xxAMIDOxx.xxSTACKSxx.menu.repository.AzureMenuRepositoryAdapter;
import com.xxAMIDOxx.xxSTACKSxx.menu.repository.GcpMenuRepositoryAdapter;
import com.xxAMIDOxx.xxSTACKSxx.menu.repository.MenuRepositoryAdapter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AzureConfig {

  @Bean("menuRepositoryAdapter")
  @ConditionalOnProperty(name = "cloud.provider", havingValue = "azure")
  public MenuRepositoryAdapter azureMenuAdapter() {
    return new AzureMenuRepositoryAdapter();
  }

  @Bean("menuRepositoryAdapter")
  @ConditionalOnProperty(name = "cloud.provider", havingValue = "gcp")
  public MenuRepositoryAdapter gcpMenuAdapter() {
    return new GcpMenuRepositoryAdapter();
  }
}
