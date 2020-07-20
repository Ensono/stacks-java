package com.xxAMIDOxx.xxSTACKSxx.core.mapper;

import java.util.UUID;

public class UuidMapper {
    public UUID map(String uuid) {
        return UUID.fromString(uuid);
    }
    public String map(UUID uuid) {
        return uuid.toString();
    }
}
