package com.xxAMIDOxx.xxSTACKSxx.handler;

import com.xxAMIDOxx.xxSTACKSxx.core.events.ApplicationEvent;
import com.xxAMIDOxx.xxSTACKSxx.core.events.ApplicationEventPublisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class DefaultEventPublisher implements ApplicationEventPublisher {

    Logger logger = LoggerFactory.getLogger(DefaultEventPublisher.class);

    @Override
    public void publish(ApplicationEvent applicationEvent) {
        logger.info(applicationEvent.toString());
    }
}
