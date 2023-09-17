package rs.ac.bg.fon.utility;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class ApiResponse<T> {

    private T data;
    private List<String> infoMessages;
    private List<String> errorMessages;

    public ApiResponse(T data) {
        this.data = data;
    }
    public ApiResponse() {
        this.infoMessages = new ArrayList<>();
        this.errorMessages = new ArrayList<>();
    }
    public ApiResponse(List<String> infoMessages, List<String> errorMessages) {
        this.infoMessages = infoMessages;
        this.errorMessages = errorMessages;
    }
    public void addInfoMessage(String infoMessage){
        infoMessages.add(infoMessage);
    }
    public void addErrorMessage(String errorMessage){
        errorMessages.add(errorMessage);
    }
}
