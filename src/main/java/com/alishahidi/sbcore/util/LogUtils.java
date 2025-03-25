package com.alishahidi.sbcore.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.experimental.UtilityClass;

import java.util.Iterator;
import java.util.Map;

@UtilityClass
public class LogUtils {
    private static final int MAX_SIZE = 500;

    public static String filterObject(Object data, ObjectMapper objectMapper) {
        try {
            if (data == null) return "NULL";
            JsonNode jsonNode = objectMapper.valueToTree(data);
            sanitizeJson(jsonNode);
            return objectMapper.writeValueAsString(jsonNode);
        } catch (Exception e) {
            return "ERROR PROCESSING DATA";
        }
    }

    private static void sanitizeJson(JsonNode node) {
        if (node.isObject()) {
            ObjectNode objectNode = (ObjectNode) node;
            Iterator<Map.Entry<String, JsonNode>> fields = objectNode.fields();
            while (fields.hasNext()) {
                Map.Entry<String, JsonNode> entry = fields.next();
                if (entry.getKey().matches("(?i).*password|secret.*")) {
                    objectNode.put(entry.getKey(), "[SECURED]");
                } else if (entry.getValue().asText().length() > MAX_SIZE) {
                    objectNode.put(entry.getKey(), "[TRIMMED]");
                }
            }
        }
    }
}
