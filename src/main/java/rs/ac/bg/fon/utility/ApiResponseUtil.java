package rs.ac.bg.fon.utility;

import org.springframework.http.ResponseEntity;

import java.util.List;

public class ApiResponseUtil {
    public static ResponseEntity<?> handleApiResponse(ApiResponse<?> apiResponse) {
        if (apiResponse.getErrorMessages().isEmpty()) {
            return ResponseEntity.ok().body(apiResponse);
        } else {
            return ResponseEntity.badRequest().body(apiResponse);
        }
    }

    public static ApiResponse<?> transformListToApiResponse(List<?> listOfObjects, String obejectName) {
        ApiResponse response = new ApiResponse();
        if (listOfObjects.isEmpty()) {
            response.addInfoMessage("There are no " + obejectName + " to show!");
            response.setData(listOfObjects);
        } else {
            response.setData(listOfObjects);
        }
        return response;
    }

    public static ApiResponse<?> transformObjectToApiResponse(Object object, String obejectName) {
        ApiResponse response = new ApiResponse();
        if (object == null) {
            response.addErrorMessage("Error getting " + obejectName + "!");
        } else {
            response.setData(object);
        }
        return response;
    }
}
