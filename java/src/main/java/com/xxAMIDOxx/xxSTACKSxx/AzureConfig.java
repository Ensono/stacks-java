package com.xxAMIDOxx.xxSTACKSxx;

import com.xxAMIDOxx.xxSTACKSxx.menu.repository.AzureMenuAdapter;
import com.xxAMIDOxx.xxSTACKSxx.menu.repository.MenuAdapter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AzureConfig {
  @Bean(name = "menuAdapter")
  @ConditionalOnProperty(name = "cloud.provider", havingValue = "azure")
  public MenuAdapter azureMenuFacadeBean() {
    return new AzureMenuAdapter();
  }
}
