package compiladores1.Validations;

import compiladores1.Models.ErrorDictionary;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author jmong
 */
public class VBeginAndEnd {
    private List<String> lines = new ArrayList<>();
    private final ErrorDictionary errorDict = new ErrorDictionary();
    private final List<String> errors = new ArrayList<>();
    
    public VBeginAndEnd(List<String> lines) {
        this.lines = lines;
    }
    
    public void validateBeginAndEnd() {
    }
    
    public List<String> getErrors() {
        return errors;
    }

    private void addError(int code, int lineNumber, String extraInfo) {
        String message = errorDict.getError(code);

        if (extraInfo != null && !extraInfo.isEmpty()) {
            message += ": " + extraInfo;
        }

        errors.add(String.format("Error %d en línea %04d: %s", code, lineNumber, message));
    }
}
