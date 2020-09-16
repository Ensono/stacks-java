package com.xxAMIDOxx.xxSTACKSxx;

import com.xxAMIDOxx.xxSTACKSxx.provider.gcp.GcpMenuRepositoryAdapter;
import com.xxAMIDOxx.xxSTACKSxx.menu.repository.MenuRepositoryAdapter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GCPConfig {

  @Bean("menuRepositoryAdapter")
  @ConditionalOnProperty(name = "cloud.provider", havingValue = "gcp")
  public MenuRepositoryAdapter gcpMenuAdapter() {
        return new GcpMenuRepositoryAdapter();
    }
}