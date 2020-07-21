package com.xxAMIDOxx.xxSTACKSxx.core.mapper;

import java.util.UUID;

public class UuidMapper {
    public UUID map(String uuid) {
        return uuid != null ? UUID.fromString(uuid) : null;
    }
    public String map(UUID uuid) {
        return uuid != null ? uuid.toString() : null;
    }
}
