package compiladores1.Validations;

import compiladores1.Models.ErrorDictionary;
import compiladores1.Models.ReservedWordDictionary;
import compiladores1.Models.Symbol;

import java.util.*;

/**
 *
 * @author jmong
 */
public class VConstants {

    private final List<String> lines;
    private final ErrorDictionary errorDict = new ErrorDictionary();
    private final List<String> errors = new ArrayList<>();
    private final Map<String, Symbol> constantTable = new HashMap<>();

    public VConstants(List<String> lines) {
        this.lines = lines;
    }

    public void validateConstants() {
        boolean existProgram = false;
        boolean existUses = false;
        boolean existVar = false;
        boolean existBegin = false;

        int lineNumber = 0;
        for (String line : lines) {
            String trimmed = line.trim();
            String[] tokens = tokenize(trimmed);

            for (int i = 0; i < tokens.length; i++) {
                String token = tokens[i];

                existProgram = checkProgram(token, existProgram);
                existUses = checkUses(token, existUses);
                existVar = checkVar(token, existVar);
                existBegin = checkBegin(token, existBegin);

                if (token.equals("const")) {
                    if (!existProgram || !existUses || existVar || existBegin) {
                        addError(400, lineNumber, null);
                        break;
                    }

                    String rest = trimmed.substring(trimmed.indexOf("const") + 5).trim();
                    String[] parts = rest.split("=", 2);

                    if (parts.length < 2) {
                        addError(402, lineNumber, null);
                        break;
                    }

                    String identifierName = parts[0].trim();
                    String value = parts[1].trim();

                    if (!value.trim().endsWith(";")) {
                        addError(401, lineNumber, null);
                    }

                    value = value.replace(";", "").trim();

                    validateIdentifierName(identifierName, lineNumber);

                    if (!(value.matches("^[0-9]+$") || value.matches("^'.*'$"))) {
                        addError(405, lineNumber, value);
                    }

                    registerConstant(identifierName, value, lineNumber);

                    break;
                }
            }

            lineNumber++;
        }

        for (Symbol sym : constantTable.values()) {
            if (!sym.isUsed()) {
                addError(403, sym.getLineDeclared(), sym.getName());
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

    private boolean checkUses(String token, boolean existUses) {
        return (!existUses && token.equals("uses")) || existUses;
    }

    private boolean checkVar(String token, boolean existVar) {
        return token.equals("var") || existVar;
    }

    private boolean checkBegin(String token, boolean existBegin) {
        return token.equals("begin") || existBegin;
    }

    private void validateIdentifierName(String identifierName, int lineNumber) {
        if (identifierName == null) {
            return;
        }

        if (ReservedWordDictionary.isReserved(identifierName)) {
            addError(404, lineNumber, identifierName);
        }

        for (int i = 0; i < identifierName.length(); i++) {
            char c = identifierName.charAt(i);
            if (i == 0 && !(Character.isLetter(c) || c == '_')) {
                addError(403, lineNumber, identifierName);
            }
            if (!(Character.isLetter(c) || c == '_')) {
                addError(403, lineNumber, identifierName);
            }
        }
    }

    private void registerConstant(String name, String value, int lineNumber) {
        if (constantTable.containsKey(name)) {
            addError(403, lineNumber, name);
            return;
        }
        constantTable.put(name, new Symbol(name, "const", lineNumber));
    }

    public void markUsed(String name) {
        Symbol sym = constantTable.get(name);
        if (sym != null) {
            sym.setUsed(true);
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

    public Map<String, Symbol> getConstantTable() {
        return constantTable;
    }
}
