package compiladores1.Models;

/**
 *
 * @author jmong
 * @param <T> Generic
 */
public class ResponseModel<T> {
    private boolean isSuccess;
    private String message;
    private T objectResult;
    
    public ResponseModel(){}
    
    public ResponseModel(boolean isSuccess, String message, T objectResult) {
        this.isSuccess = isSuccess;
        this.message = message;
        this.objectResult = objectResult;
    }

    public boolean isIsSuccess() {
        return isSuccess;
    }

    public void setIsSuccess(boolean isSuccess) {
        this.isSuccess = isSuccess;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getObjectResult() {
        return objectResult;
    }

    public void setObjectResult(T objectResult) {
        this.objectResult = objectResult;
    }    
}