package com.xxAMIDOxx.xxSTACKSxx.api.v1.menu.utilities;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author ArathyKrishna
 */
public class HelperMethods {

    public static String toJson(ObjectMapper mapper, Object actual) throws JsonProcessingException {
        return mapper.writeValueAsString(actual);
    }

    public static String getBaseURL(int port) {
        return "http://localhost:" + port;
    }
}
