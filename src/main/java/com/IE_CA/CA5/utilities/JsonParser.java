package com.IE_CA.CA5.utilities;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonParser {
    public static <T> T createObject(String rawData, Class<T> type) throws JsonProcessingException {
        return new ObjectMapper().readValue(rawData, type);
    }
}
