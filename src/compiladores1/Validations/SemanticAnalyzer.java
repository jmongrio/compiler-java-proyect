package compiladores1.Validations;

import compiladores1.Models.ErrorDictionary;
import compiladores1.Models.Symbol;
import compiladores1.Models.SymbolTable;
import compiladores1.Models.Token;
import compiladores1.Models.TokenType;
import java.util.ArrayList;
import java.util.List;

public class SemanticAnalyzer {

    private final List<Token> tokens;
    private final SymbolTable symbolTable = new SymbolTable();
    private final List<String> errors = new ArrayList<>();
    private final ErrorDictionary errorDict = new ErrorDictionary();

    private int current = 0;

    public SemanticAnalyzer(List<Token> tokens) {
        this.tokens = tokens;
    }

    public void analyze() {
        while (!isAtEnd()) {
            Token token = peek();

            if (token.getLexeme().equalsIgnoreCase("const")) {
                if (!consume("const", 400, "Se esperaba palabra clave 'const'")) {
                    advance();
                    continue;
                }
                handleConstDeclaration();
            } else if (token.getLexeme().equalsIgnoreCase("var")) {
                if (!consume("var", 500, "Se esperaba palabra clave 'var'")) {
                    advance();
                    continue;
                }
                handleVarDeclaration();
            } else {
                advance();
            }
        }
    }

    private void handleConstDeclaration() {
        while (!isAtEnd() && !peek().getLexeme().equalsIgnoreCase("var")
                && !peek().getLexeme().equalsIgnoreCase("begin")) {

            Token identifier = consumeType(TokenType.IDENTIFIER, 403, "Se esperaba un identificador");
            if (identifier == null) {
                return;
            }

            if (!consume("=", 402, "Falta signo '=' o valor en la declaración de constante")) {
                return;
            }

            if (isAtEnd()) {
                addError(405, identifier.getLine(), "Falta valor para la constante");
                return;
            }

            Token value = advance();

            // Intentar registrar la constante y capturar duplicados
            try {
                symbolTable.addSymbol(new Symbol(identifier.getLexeme(), inferType(value), true));
            } catch (Exception ex) {
                // 608 = símbolo duplicado (puedes agregarlo a ErrorDictionary)
                addError(608, identifier.getLine(), identifier.getLexeme());
                // continuar analizando (no return) para no cortar el análisis
            }

            if (!consume(";", 401, "Falta punto y coma al final de la declaración de constante")) {
                return;
            }
        }
    }

    private void handleVarDeclaration() {
        while (!isAtEnd() && !peek().getLexeme().equalsIgnoreCase("begin")) {
            Token identifier = consumeType(TokenType.IDENTIFIER, 502, "Se esperaba un identificador");
            if (identifier == null) {
                return;
            }

            if (!consume(":", 501, "Falta ':' en declaración de variable")) {
                return;
            }

            String type = parseType();
            if (type == null) {
                addError(504, peekSafeLine(), "Tipo de variable no válido");
                return;
            }

            // Intentar registrar la variable y capturar duplicados
            try {
                symbolTable.addSymbol(new Symbol(identifier.getLexeme(), type, false));
            } catch (Exception ex) {
                addError(608, identifier.getLine(), identifier.getLexeme());
                // continuar el análisis
            }

            if (!consume(";", 500, "Falta punto y coma al final de la declaración")) {
                return;
            }
        }
    }

    private String parseType() {
        if (isAtEnd()) {
            return null;
        }

        Token token = advance();
        if (token.getLexeme().equalsIgnoreCase("array")) {
            if (!consume("[", 504, "Falta '[' en declaración de array")) {
                return null;
            }
            Token start = advance();
            if (!consume(".", 504, "Falta primer '.' en rango del array")) {
                return null;
            }
            if (!consume(".", 504, "Falta segundo '.' en rango del array")) {
                return null;
            }
            Token end = advance();
            if (!consume("]", 504, "Falta ']' en declaración de array")) {
                return null;
            }
            if (!consume("of", 504, "Falta palabra clave 'of' en declaración de array")) {
                return null;
            }
            Token baseType = consumeType(TokenType.IDENTIFIER, 504, "Falta tipo base del array");
            if (baseType == null) {
                return null;
            }
            return "array[" + start.getLexeme() + ".." + end.getLexeme() + "] of " + baseType.getLexeme();
        } else {
            return token.getLexeme();
        }
    }

    private String inferType(Token value) {
        if (value.getType() == TokenType.NUMBER) {
            return "integer";
        }
        if (value.getType() == TokenType.STRING) {
            return "string";
        }
        return "unknown";
    }

    // === Utilidades ===
    private Token advance() {
        if (!isAtEnd()) {
            current++;
        }
        return previous();
    }

    private boolean isAtEnd() {
        return current >= tokens.size();
    }

    private Token peek() {
        return tokens.get(current);
    }

    private Token previous() {
        return tokens.get(current - 1);
    }

    private boolean consume(String expected, int errorCode, String extraInfo) {
        if (isAtEnd()) {
            int line = (current == 0) ? 0 : previous().getLine();
            addError(errorCode, line, "Fin inesperado del archivo");
            return false;
        }

        if (peek().getLexeme().equalsIgnoreCase(expected)) {
            advance();
            return true;
        } else {
            String info = (extraInfo == null) ? "" : extraInfo + " ";
            addError(errorCode, peek().getLine(), info + "(Encontrado: '" + peek().getLexeme() + "')");
            return false;
        }
    }

    private Token consumeType(TokenType type, int errorCode, String extraInfo) {
        if (isAtEnd()) {
            int line = (current == 0) ? 0 : previous().getLine();
            addError(errorCode, line, "Fin inesperado del archivo");
            return null;
        }

        if (peek().getType() == type) {
            advance();
            return previous();
        } else {
            String info = (extraInfo == null) ? "" : extraInfo + " ";
            addError(errorCode, peek().getLine(), info + "(Encontrado: '" + peek().getLexeme() + "')");
            return null;
        }
    }

    // === NUEVO: Método de formato unificado ===
    private void addError(int code, int lineNumber, String extraInfo) {
        String message = errorDict.getError(code);

        if (extraInfo != null && !extraInfo.isEmpty()) {
            message += ": " + extraInfo;
        }

        errors.add(String.format("Error %d en línea %04d: %s", code, lineNumber, message));
    }

    public SymbolTable getSymbolTable() {
        return symbolTable;
    }

    public List<String> getErrors() {
        return errors;
    }

    private int peekSafeLine() {
        if (!isAtEnd()) {
            return peek().getLine();
        } else if (current > 0) {
            return previous().getLine();
        }
        return 0;
    }
}
