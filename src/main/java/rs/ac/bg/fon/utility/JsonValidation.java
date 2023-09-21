package rs.ac.bg.fon.utility;

import com.google.gson.JsonElement;

public class JsonValidation {


    // INFO - JSON ELEMENT VALIDATION
    public static boolean validateJsonElementIsObject(JsonElement jsonElement) {
        return jsonElement != null
                && !jsonElement.isJsonNull()
                && jsonElement.isJsonObject();
    }

    public static boolean validateJsonElementIsArray(JsonElement jsonElement) {
        return jsonElement != null
                && !jsonElement.isJsonNull()
                && jsonElement.isJsonArray();
    }

    public static boolean validateJsonElementIsPrimitive(JsonElement jsonElement) {
        return jsonElement != null
                || !jsonElement.isJsonNull()
                || jsonElement.isJsonPrimitive();
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
