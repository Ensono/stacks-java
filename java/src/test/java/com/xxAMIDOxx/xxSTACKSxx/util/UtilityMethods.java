package com.xxAMIDOxx.xxSTACKSxx.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author ArathyKrishna
 */
public class UtilityMethods {

    public static String toJson(ObjectMapper mapper, Object actual) throws JsonProcessingException {
        return mapper.writeValueAsString(actual);
    }

    public static String getBaseURL(int port) {
        return "http://localhost:" + port;
    }
}
