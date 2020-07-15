package com.xxAMIDOxx.xxSTACKSxx.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author ArathyKrishna
 */
public class TestHelper {

    public static String toJson(ObjectMapper mapper, Object o) throws JsonProcessingException {
        return mapper.writeValueAsString(o);
    }

    public static String getBaseURL(int port) {
        return String.format("http://localhost:%d", port);
    }
}
