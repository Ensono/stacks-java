package com.ensono.stacks.core.messaging.event;

import static java.time.ZonedDateTime.now;

import com.ensono.stacks.core.operations.OperationContext;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.ZonedDateTimeSerializer;
import lombok.Getter;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.UUID;

public abstract class ApplicationEvent extends OperationContext implements Serializable {

    @Getter
    private final UUID id;
    @Getter
    private final int operationCode;

    @JsonSerialize(using = ZonedDateTimeSerializer.class)
    private final ZonedDateTime creationDate;

    public ApplicationEvent(int operationCode, String correlationId) {
        super(correlationId);
        this.operationCode = operationCode;
        this.id = UUID.randomUUID();
        this.creationDate = now();
    }

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSSZ")
    public ZonedDateTime getCreationDate() {
        return creationDate;
    }

    public abstract int getEventCode();

    @Override
    public String toString() {
        return "ApplicationEvent{"
                + "id="
                + id
                + ", eventCode="
                + getEventCode()
                + ", creationDate="
                + creationDate
                + "} "
                + super.toString();
    }
}
