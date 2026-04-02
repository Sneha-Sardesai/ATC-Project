package util;

import java.util.HashMap;
import java.util.Map;

public class SimpleJSONParser {

    public static Map<String, Object> parse(String json) {
        Map<String, Object> map = new HashMap<>();
        json = json.trim();
        if (json.startsWith("{") && json.endsWith("}")) {
            json = json.substring(1, json.length() - 1);
            String[] pairs = json.split(",");
            for (String pair : pairs) {
                String[] keyValue = pair.split(":", 2);
                if (keyValue.length == 2) {
                    String key = keyValue[0].trim().replace("\"", "");
                    String value = keyValue[1].trim();
                    if (value.startsWith("\"") && value.endsWith("\"")) {
                        value = value.substring(1, value.length() - 1);
                        map.put(key, value);
                    } else {
                        try {
                            map.put(key, Integer.parseInt(value));
                        } catch (NumberFormatException e) {
                            map.put(key, value);
                        }
                    }
                }
            }
        }
        return map;
    }
}