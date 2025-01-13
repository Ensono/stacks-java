package com.ensono.stacks.core.messaging.publish;

import com.ensono.stacks.core.messaging.event.ApplicationEvent;

public interface ApplicationEventPublisher {

    void publish(ApplicationEvent applicationEvent);
}
