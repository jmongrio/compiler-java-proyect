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
        boolean existProgram = false;
        boolean existEndDot = false;
        boolean firstBegin = false;
        boolean firstEnd = false;
        int countBegin = 0;
        int countEnd = 0;

        int lineNumber = 0;
        for (String line : lines) {
            String[] tokens = line.trim().split("\\s+");

            for (String token : tokens) {
                existProgram = checkProgram(token, existProgram);
                existEndDot = checkEndDot(token, existEndDot);

                if (token.equals("begin")) {
                    firstBegin = true;
                    countBegin++;
                    
                    if(tokens.length > 1){
                        addError(601, lineNumber, null);
                    }
                }

                if (token.equals("end;")) {
                    firstEnd = true;
                    countEnd++;
                    
                    if(tokens.length > 1){
                        addError(604, lineNumber, null);
                    }
                }

                if (token.equals("end")) {
                    countEnd++;
                    
                    if(tokens.length > 1){
                        addError(604, lineNumber, null);
                    }
                }

                if (token.equals("end.")) {
                    existEndDot = true;
                    
                    if(tokens.length > 1){
                        addError(604, lineNumber, null);
                    }
                }
            }

            if (firstEnd == true && firstBegin == false) {
                addError(602, lineNumber, null);
            }

            lineNumber++;
        }

        if (countBegin != countEnd) {
            addError(607, lineNumber, String.format("Se encontráron %d begin y %d end", countBegin, countEnd));
        }

        if (firstBegin == false) {
            addError(605, lineNumber, null);
        }

        if (existEndDot == false) {
            addError(603, lineNumber, null);
            addError(606, lineNumber, null);
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

    private boolean checkProgram(String token, boolean existProgram) {
        return (!existProgram && token.equals("program")) || existProgram;
    }

    private boolean checkEndDot(String token, boolean existEnd) {
        return (token.equals("end.")) || existEnd;
    }
}