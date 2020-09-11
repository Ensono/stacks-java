package com.xxAMIDOxx.xxSTACKSxx;

import com.xxAMIDOxx.xxSTACKSxx.menu.repository.AzureMenuRepositoryAdapter;
import com.xxAMIDOxx.xxSTACKSxx.menu.repository.GcpMenuRepositoryAdapter;
import com.xxAMIDOxx.xxSTACKSxx.menu.repository.MenuAdapter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AzureConfig {

  @Bean("menuAdapter")
  @ConditionalOnProperty(name = "cloud.provider", havingValue = "azure")
  public MenuAdapter azureMenuAdapter() {
    return new AzureMenuRepositoryAdapter();
  }

  @Bean("menuAdapter")
  @ConditionalOnProperty(name = "cloud.provider", havingValue = "gcp")
  public MenuAdapter gcpMenuAdapter() {
    return new GcpMenuRepositoryAdapter();
  }
}
