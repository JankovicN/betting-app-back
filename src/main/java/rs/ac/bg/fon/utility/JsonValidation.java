package rs.ac.bg.fon.utility;

import com.google.gson.JsonElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import rs.ac.bg.fon.service.FootballApiServiceImpl;

public class JsonValidation {


    private static final Logger logger = LoggerFactory.getLogger(JsonValidation.class);

    // INFO - JSON ELEMENT VALIDATION
    public static boolean validateJsonElementIsObject(JsonElement jsonElement) {
        boolean isValid = jsonElement != null
                && !jsonElement.isJsonNull()
                && jsonElement.isJsonObject();
        if (!isValid) {
            logger.error("validateJsonElementIsObject: Invalid JSON " + jsonElement);
        }
        return isValid;
    }

    public static boolean validateJsonElementIsArray(JsonElement jsonElement) {
        boolean isValid = jsonElement != null
                && !jsonElement.isJsonNull()
                && jsonElement.isJsonArray();
        if (!isValid) {
            logger.error("validateJsonElementIsArray: Invalid JSON " + jsonElement);
        }
        return isValid;
    }

    public static boolean validateJsonElementIsPrimitive(JsonElement jsonElement) {
        boolean isValid = jsonElement != null
                && !jsonElement.isJsonNull()
                && jsonElement.isJsonPrimitive();
        if (!isValid) {
            logger.error("validateJsonElementIsPrimitive: Invalid JSON " + jsonElement);
        }
        return isValid;
    }

    public static boolean validateJsonElementIsNumber(JsonElement jsonElement) {
        return validateJsonElementIsPrimitive(jsonElement)
                && jsonElement.getAsJsonPrimitive().isNumber();
    }

    // INFO - FIELD INSIDE JSON ELEMENT VALIDATION

    public static boolean isJsonFieldPresent(JsonElement jsonElement, String fieldName) {
        return validateJsonElementIsObject(jsonElement)
                && jsonElement.getAsJsonObject().has(fieldName)
                && jsonElement.getAsJsonObject().get(fieldName) != null
                && !jsonElement.getAsJsonObject().get(fieldName).isJsonNull();
    }


    public static boolean validateJsonElementFieldIsObject(JsonElement jsonElement, String fieldName) {
        return isJsonFieldPresent(jsonElement, fieldName)
                && jsonElement.getAsJsonObject().get(fieldName).isJsonObject();
    }

    public static boolean validateJsonElementIsPrimitive(JsonElement jsonElement, String fieldName) {
        return isJsonFieldPresent(jsonElement, fieldName)
                && jsonElement.getAsJsonObject().get(fieldName).isJsonPrimitive();
    }

    public static boolean validateJsonElementFieldIsNumber(JsonElement jsonElement, String fieldName) {
        return validateJsonElementIsPrimitive(jsonElement, fieldName)
                && jsonElement.getAsJsonObject().get(fieldName).getAsJsonPrimitive().isNumber();
    }

    public static boolean validateJsonElementFieldIsString(JsonElement jsonElement, String fieldName) {
        return validateJsonElementIsPrimitive(jsonElement, fieldName)
                && jsonElement.getAsJsonObject().get(fieldName).getAsJsonPrimitive().isString();
    }

    public static boolean validateJsonElementFieldIsArray(JsonElement jsonElement, String fieldName) {
        return isJsonFieldPresent(jsonElement, fieldName)
                && jsonElement.getAsJsonObject().get(fieldName).isJsonArray()
                && !jsonElement.getAsJsonObject().get(fieldName).getAsJsonArray().isEmpty();
    }
}
