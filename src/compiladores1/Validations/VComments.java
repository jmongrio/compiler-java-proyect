package compiladores1.Validations;

import compiladores1.Models.ErrorDictionary;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author jmong
 */
public class VComments {
    private List<String> lines = new ArrayList<>();
    private final ErrorDictionary errorDict = new ErrorDictionary();
    private final List<String> errors = new ArrayList<>();
    
    public VComments(List<String> lines) {
        this.lines = lines;
    }
    
    public void validateComments() {
        for(String line : lines){
            String[] token = line.trim().split("\\s+");
            for (int i = 0; i < token.length; i++) {
                
            }
        }
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
