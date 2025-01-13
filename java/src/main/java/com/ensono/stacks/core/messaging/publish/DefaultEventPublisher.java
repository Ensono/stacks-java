package com.ensono.stacks.core.messaging.publish;

import com.ensono.stacks.core.messaging.event.ApplicationEvent;
import com.ensono.stacks.core.messaging.listen.ApplicationEventListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty(
        value = "azure.servicebus.enabled",
        havingValue = "false",
        matchIfMissing = true)
public class DefaultEventPublisher extends ApplicationEventPublisherWithListener {

    Logger logger = LoggerFactory.getLogger(DefaultEventPublisher.class);

    protected DefaultEventPublisher(ApplicationEventListener applicationEventListener) {
        super(applicationEventListener);
    }

    @Override
    public void publish(ApplicationEvent applicationEvent) {
        logger.info(applicationEvent.toString());
    }

    @Override
    public void listen() {
        // Nothing to listen to
    }
}
