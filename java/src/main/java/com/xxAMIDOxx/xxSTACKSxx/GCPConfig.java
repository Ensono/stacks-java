// package com.xxAMIDOxx.xxSTACKSxx;
//
// import com.xxAMIDOxx.xxSTACKSxx.menu.repository.MenuAdapter;
// import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
// import org.springframework.context.annotation.Bean;
// import org.springframework.context.annotation.Configuration;
//
// @Configuration
// public class GCPConfig {
//
//    @Bean(name = "menuFacade")
//  @ConditionalOnProperty(name = "cloud.provider", havingValue = "gcp")
//  public MenuAdapter gcpMenuFacadeBean() {
//    return new GCPMenuFacade();
//  }
// }
