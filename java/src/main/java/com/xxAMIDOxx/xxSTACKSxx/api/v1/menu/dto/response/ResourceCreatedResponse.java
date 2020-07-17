package com.xxAMIDOxx.xxSTACKSxx.api.v1.menu.dto.response;

import java.util.UUID;

public class ResourceCreatedResponse {

    private UUID id = null;

    public ResourceCreatedResponse(UUID id) {
        this.id = id;
    }

    public UUID getId() {
        return id;
    }
}
