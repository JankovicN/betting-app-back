package rs.ac.bg.fon.utility;

import org.springframework.http.ResponseEntity;

import java.net.URI;
import java.util.List;

public class ApiResponseUtil {
    public static ResponseEntity<?> handleApiResponse(ApiResponse<?> apiResponse) {
        if (apiResponse.getErrorMessages().isEmpty()) {
            return ResponseEntity.ok().body(apiResponse);
        } else {
            return ResponseEntity.badRequest().body(apiResponse);
        }
    }

    public static ResponseEntity<?> handleApiResponse(ApiResponse<?> apiResponse, URI uri) {
        if (apiResponse.getErrorMessages().isEmpty()) {
            return ResponseEntity.created(uri).body(apiResponse);
        } else {
            return ResponseEntity.badRequest().body(apiResponse);
        }
    }
    public static ResponseEntity<?> errorApiResponse(String errorMessage) {
        ApiResponse response= new ApiResponse<>();
        response.addErrorMessage(errorMessage);
        return ResponseEntity.badRequest().body(response);
    }
    public static ApiResponse<?> transformListToApiResponse(List<?> listOfObjects, String objectName) {
        ApiResponse response = new ApiResponse();
        if (listOfObjects==null || listOfObjects.isEmpty()) {
            response.addInfoMessage("There are no " + objectName + " to show!");
            response.setData(listOfObjects);
        } else {
            response.setData(listOfObjects);
        }
        return response;
    }

    public static ApiResponse<?> transformObjectToApiResponse(Object object, String objectName) {
        ApiResponse response = new ApiResponse();
        if (object == null) {
            response.addErrorMessage("Error getting " + objectName + "!");
        } else {
            response.setData(object);
        }
        return response;
    }

}
