package com.xxAMIDOxx.xxSTACKSxx.mapper;

import java.util.UUID;

public class UuidMapper {
    public UUID map(String uuid) {
        return UUID.fromString(uuid);
    }
    public String map(UUID uuid) {
        return uuid.toString();
    }
}
