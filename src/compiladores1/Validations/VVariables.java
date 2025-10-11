package compiladores1.Validations;

import compiladores1.Models.ErrorDictionary;
import compiladores1.Models.ReservedWordDictionary;
import compiladores1.Models.Symbol;

import java.util.*;

/**
 *
 * @author jmong
 */
public class VVariables {

    private final List<String> lines;
    private final ErrorDictionary errorDict = new ErrorDictionary();
    private final List<String> errors = new ArrayList<>();
    private final Map<String, Symbol> symbolTable = new HashMap<>();

    public VVariables(List<String> lines) {
        this.lines = lines;
    }

    public void validateIdentifier() {
        boolean existProgram = false;
        boolean existBegin = false;

        int lineNumber = 0;
        for (String line : lines) {
            String[] tokens = tokenize(line);
            boolean isVar = false;
            String identifierName = null;
            String type = null;

            for (int i = 0; i < tokens.length; i++) {
                String token = tokens[i];

                existProgram = checkProgram(token, existProgram);
                existBegin = checkBegin(token, existBegin);

                if (isVar) {
                    identifierName = token;
                    if (i + 1 < tokens.length) {
                        type = tokens[i + 1].replace(";", "");
                    }
                    break;
                }

                if (token.equals("var")) {
                    isVar = true;
                    if ((!existProgram || existBegin)) {
                        addError(505, lineNumber, null);
                    }
                } else {
                    checkVariableUsage(token, lineNumber, line);
                }
            }

            validateIdentifierName(identifierName, lineNumber);

//            if (identifierName != null && type != null) {
//                registerVariable(identifierName, type, lineNumber);
//            }

            validateLineEnding(isVar, line, lineNumber);

            lineNumber++;
        }

        for (Symbol sym : symbolTable.values()) {
            if (!sym.isUsed()) {
                addError(508, sym.getLineDeclared(), sym.getName());
            }
        }
    }

    // -----------------------------
    // MÉTODOS AUXILIARES
    // -----------------------------
    private String[] tokenize(String line) {
        return line.trim().split("\\s+");
    }

    private boolean checkProgram(String token, boolean existProgram) {
        return (!existProgram && token.equals("program")) || existProgram;
    }

    private boolean checkBegin(String token, boolean existBegin) {
        return (token.equals("begin")) || existBegin;
    }

    private void validateIdentifierName(String identifierName, int lineNumber) {
        if (identifierName == null) return;

        if (ReservedWordDictionary.isReserved(identifierName)) {
            addError(503, lineNumber, identifierName);
        }

        for (int i = 0; i < identifierName.length(); i++) {
            char c = identifierName.charAt(i);
            if (i == 0 && !(Character.isLetter(c) || c == '_')) {
                addError(506, lineNumber, identifierName);
            }
            if (!(Character.isLetter(c) || c == '_')) {
                addError(507, lineNumber, identifierName);
            }
        }
    }

    private void validateLineEnding(boolean isVar, String line, int lineNumber) {
        if (isVar && !line.trim().endsWith(";")) {
            addError(202, lineNumber, null);
        }
    }

//    private void registerVariable(String identifierName, String type, int lineNumber) {
//        if (!type.equalsIgnoreCase("integer") &&
//            !type.equalsIgnoreCase("string") &&
//            !type.equalsIgnoreCase("word")) {
//            addError(511, lineNumber, type);
//            return;
//        }
//
//        if (symbolTable.containsKey(identifierName)) {
//            addError(510, lineNumber, identifierName);
//            return;
//        }
//
//        symbolTable.put(identifierName, new Symbol(identifierName, type, lineNumber));
//    }

    private void checkVariableUsage(String token, int lineNumber, String line) {
        if (!symbolTable.containsKey(token)) return;

        Symbol sym = symbolTable.get(token);
        sym.setUsed(true);

        if (line.contains(token + " := ") || line.contains(token + ":=")) {
            sym.setInitialized(true);
        }

        if (!sym.isInitialized() && line.contains(token) && !line.contains(":=")) {
            addError(513, lineNumber, token);
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

    public Map<String, Symbol> getSymbolTable() {
        return symbolTable;
    }
}