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
public class Program {

    private List<String> lines = new ArrayList<>();
    private final ErrorDictionary errorDict = new ErrorDictionary();
    private final List<String> errors = new ArrayList<>();
    private int lineProgramExist = 0;
    private String baseName = "";

    public Program(List<String> lines, String baseName) {
        this.lines = lines;
        this.baseName = baseName;
    }

    public void validateProgram() {
        if (lines.isEmpty()) {
            addError(200, 0, null);
            return;
        }

        boolean existProgram = false;
        int lineProgram = 0;

        for (String line : lines) {
            String trimmed = line.trim();
            
            if(existProgram == false && trimmed.contains("//")){
                addError(205, lineProgram, null);
            }

            if (trimmed.startsWith("//")) {
                lineProgram++;
                continue;
            }

            // Delete comment code
            String noComments = trimmed
                    .replaceAll("\\{.*?\\}", "")
                    .replaceAll("\\(\\*.*?\\*\\)", "");

            if (noComments.toLowerCase().contains("program")) {
                if (existProgram) {
                    addError(204, lineProgram, null);
                    return;
                }

                if (!line.endsWith(";")) {
                    addError(202, lineProgram, "La declaración 'program' debe finalizar con ';'");
                }

                existProgram = true;
                lineProgramExist = lineProgram;
                
                String programIdentifier = getIdentifierName(line);

                if (programIdentifier == null ? baseName != null : !programIdentifier.equals(baseName)) {
                    addError(203, lineProgram, null);
                }
            }

            lineProgram++;
        }

        if (!existProgram) {
            addError(200, 0, null);
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

    public int getProgramLine() {
        return lineProgramExist;
    }
    
    private String getIdentifierName(String line){
        String identifier = "";
        Pattern pattern = Pattern.compile("^\\s*program\\s+([a-zA-Z_][a-zA-Z0-9_]*)\\s*;\\s*$", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(line);

        if (matcher.find()) {
            identifier = matcher.group(1);
        }
        
        return identifier;
    }
}