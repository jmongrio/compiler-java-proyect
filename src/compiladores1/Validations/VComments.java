package compiladores1.Validations;

import compiladores1.Models.ErrorDictionary;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
        Pattern pattern = Pattern.compile("//.*|\\{*}]\\}");

        boolean existProgram = false;
        boolean existEnd = false;

        int lineNumber = 0;
        for (String line : lines) {
            Matcher matcher = pattern.matcher(line);
            boolean isComment = false;
            int openBrace = 0;
            int closeBrace = 0;
            int slash = 0;
            boolean isSlash = false;

            if (matcher.hasMatch() && !existProgram) {
                addError(205, lineNumber, null);
            }

            String[] tokens = line.trim().split("\\s+|(?=[{}\\/])|(?<=[{}\\/])");

            for (int i = 0; i < tokens.length; i++) {
                String token = tokens[i];

                existProgram = checkProgram(token, existProgram);
                existEnd = checkEnd(token, existEnd);

                if (token.equals("/")) {
                    isComment = true;
                    isSlash = true;
                    slash++;
                }

                if (token.equals("{")) {
                    isComment = true;
                    openBrace++;
                }

                if (token.equals("}")) {
                    isComment = true;
                    closeBrace++;
                }
            }

            if (isComment && openBrace != closeBrace) {
                addError(803, lineNumber, null);
            }

            if (isComment && !existProgram) {
                addError(205, lineNumber, null);
            }
            
            if(isComment && isSlash && slash != 2){
                addError(805, lineNumber, null);
            }
            
            if(isComment && existProgram && existEnd){
                addError(806, lineNumber, null);
            }

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

    private boolean checkProgram(String token, boolean existProgram) {
        return (!existProgram && token.equals("program")) || existProgram;
    }
    
    private boolean checkEnd(String token, boolean existEnd) {
        return (token.equals("end.")) || existEnd;
    }
}
