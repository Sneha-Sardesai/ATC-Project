package util;

import java.util.List;
import java.util.Map;
import model.Flight;

public class JSONUtil {

    public static String toJson(Object obj) {
        if (obj instanceof List) {
            return listToJson((List<?>) obj);
        } else if (obj instanceof Flight) {
            return flightToJson((Flight) obj);
        } else if (obj instanceof Map) {
            return mapToJson((Map<?, ?>) obj);
        } else {
            return "\"" + obj.toString() + "\"";
        }
    }

    private static String listToJson(List<?> list) {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < list.size(); i++) {
            sb.append(toJson(list.get(i)));
            if (i < list.size() - 1) sb.append(",");
        }
        sb.append("]");
        return sb.toString();
    }

    private static String flightToJson(Flight flight) {
        return "{"
            + "\"flightId\":" + flight.getFlightId() + ","
            + "\"status\":\"" + flight.getStatus() + "\","
            + "\"aircraftId\":" + flight.getAircraftId() + ","
            + "\"runwayId\":" + flight.getRunwayId() + ","
            + "\"gateId\":" + flight.getGateId()
            + "}";
    }

    private static String mapToJson(Map<?, ?> map) {
        StringBuilder sb = new StringBuilder("{");
        int i = 0;
        for (Map.Entry<?, ?> entry : map.entrySet()) {
            sb.append("\"" + entry.getKey() + "\":" + toJson(entry.getValue()));
            if (i < map.size() - 1) sb.append(",");
            i++;
        }
        sb.append("}");
        return sb.toString();
    }
}