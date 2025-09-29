package compiladores1.Validations;

import compiladores1.Models.ErrorDictionary;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author jmong
 */
public class VWrite {

    private List<String> lines = new ArrayList<>();
    private final ErrorDictionary errorDict = new ErrorDictionary();
    private final List<String> errors = new ArrayList<>();

    public VWrite(List<String> lines) {
        this.lines = lines;
    }

    public void validateWrite() {
        boolean existEndDot = false;
        boolean firstBegin = false;
        boolean existProgram = false;

        int lineNumber = 0;
        for (String line : lines) {
            String[] tokens = line.trim().split("\\s+|(?=[{}()])|(?<=[{}()])");

            boolean openBracket = false;
            boolean closeBracket = false;
            boolean isWrite = false;
            boolean isWriteLn = false;
            boolean hasContent = false;

            for (String token : tokens) {
                if(token.equals("program")){
                    existProgram = true;
                }
                
                if(token.equals("begin")){
                    firstBegin = true;
                }
                
                if(token.equals("end.")){
                    existEndDot = true;
                }
                
                if (token.equals("write")) {
                    isWrite = true;
                }

                if (token.equals("writeln")) {
                    isWriteLn = true;
                }

                if (isWrite || isWriteLn) {
                    if (token.equals("(")) {
                        openBracket = true;
                    }
                }

                if (isWrite || isWriteLn) {
                    if (token.equals(")")) {
                        closeBracket = true;
                    }
                }
                
                if((isWrite || isWriteLn) && openBracket == true && closeBracket == false && !token.equals(")") && !token.equals("(")){
                    hasContent = true;
                }
            }

            if (isWrite || isWriteLn) {
                if (closeBracket == true && openBracket == false) {
                    addError(704, lineNumber, null);
                }

                if (closeBracket == false && openBracket == true){
                    addError(704, lineNumber, null);
                }
                
                if(closeBracket == true && openBracket == true && hasContent == false){
                    addError(701, lineNumber, null);
                }
            }
            
            if((isWrite || isWriteLn) && existProgram == false){
                addError(705, lineNumber, null);
            }
            
            if((isWrite || isWriteLn) && existEndDot == true){
                addError(706, lineNumber, null);
            }
            
            if((isWrite || isWriteLn) && (firstBegin == false || existEndDot == true)){
                addError(707, lineNumber, null);
            }

            validateLineEnding(isWrite, isWriteLn, line, lineNumber);

            lineNumber++;
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

    private void validateLineEnding(boolean isWrite, boolean isWriteLn, String line, int lineNumber) {
        if ((isWrite || isWriteLn) && !line.trim().endsWith(";")) {
            addError(202, lineNumber, null);
        }
    }
}
