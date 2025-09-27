package compiladores1.Validations;

import compiladores1.Models.ErrorDictionary;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author jmong
 */
public class Uses {

    private List<String> lines = new ArrayList<>();
    private final ErrorDictionary errorDict = new ErrorDictionary();
    private final List<String> errors = new ArrayList<>();
    private final String baseName;

    public Uses(List<String> lines, String baseName) {
        this.lines = lines;
        this.baseName = baseName;
    }

    public void validateUses() {
        Program programValidation = new Program(lines, baseName);

        int programLine = programValidation.getProgramLine();

        int lineNumber = 0;
        for (String line : lines) {
//            if (lineNumber == (programLine + 1) && !line.contains("uses")) {
//                addError(300, lineNumber, null);
//            }

            if (line.contains("uses") && !line.endsWith(";")) {
                addError(202, lineNumber, null);
            }

            lineNumber++;
        }
    }

    private void addError(int code, int lineNumber, String extraInfo) {
        String message = errorDict.getError(code);

        if (extraInfo != null && !extraInfo.isEmpty()) {
            message += ": " + extraInfo;
        }

        errors.add(String.format("Error %d en línea %04d: %s", code, lineNumber, message));
    }

    public List<String> getErrors() {
        return errors;
    }
}