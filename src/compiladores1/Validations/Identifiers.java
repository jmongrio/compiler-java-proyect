package compiladores1.Validations;

import compiladores1.Models.ErrorDictionary;
import compiladores1.Models.ReservedWordDictionary;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author jmong
 */
public class Identifiers {

    private List<String> lines = new ArrayList<>();
    private final ErrorDictionary errorDict = new ErrorDictionary();
    private final List<String> errors = new ArrayList<>();

    public Identifiers(List<String> lines) {
        this.lines = lines;
    }

    public void validateIdentifier() {
        boolean existProgram = false;
        boolean existUses = false;
        boolean existBegin = false;

        // var before program and uses and after begin
        int lineNumber = 0;
        for (String line : lines) {
            String[] lineSplit = line.trim().split(" ");
            boolean isVar = false;
            String identifierName = null;
            for (String splitItem : lineSplit) {

                if (!existProgram) {
                    if (splitItem.equals("program")) {
                        existProgram = true;
                    }
                }

                if (isVar) {
                    identifierName = splitItem;
                    break;
                }

                if (splitItem.equals("var")) {
                    isVar = true;
                }

                if (splitItem.endsWith("begin")) {
                    existBegin = true;
                }

                if ((!existProgram || existBegin) && isVar) {
                    addError(505, lineNumber, null);
                }
            }

            if (identifierName != null && ReservedWordDictionary.isReserved(identifierName)) {
                addError(503, lineNumber, null);
            }

            if (identifierName != null) {
                for (int i = 0; i < identifierName.length(); i++) {
                    char c = identifierName.charAt(i);

                    if (i == 0 && !(Character.isLetter(c) || c == '_')) {
                        addError(506, lineNumber, null);
                    }
                }
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
