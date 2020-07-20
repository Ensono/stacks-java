package com.xxAMIDOxx.xxSTACKSxx.menu.api.v1.dto.response;

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
