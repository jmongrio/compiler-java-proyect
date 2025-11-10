package compiladores1.Validations;

import compiladores1.Models.*;
import java.util.ArrayList;
import java.util.List;

public class ValidateConstBlock {

    private final List<Token> tokens;
    private final List<String> errors = new ArrayList<>();
    private final ErrorDictionary errorDict = new ErrorDictionary();
    private int position = 0;
    private boolean insideBegin = false;

    public ValidateConstBlock(List<Token> tokens) {
        this.tokens = tokens;
    }

    public void parseConstBlock() {
        while (position < tokens.size()) {
            Token current = peek();

            if (current.getType() == TokenType.BEGIN)
                insideBegin = true;
            if (insideBegin && current.getType() == TokenType.CONST) {
                addError(400, current.getLine(), "Declaración encontrada después de BEGIN");
                advance();
                continue;
            }

            if (current.getType() == TokenType.CONST) {
                advance();
                parseConstDeclaration();
            } else {
                advance();
            }
        }
    }

    private void parseConstDeclaration() {
        Token identifier = consume(TokenType.IDENTIFIER, 403, "Se esperaba un identificador de constante");
        if (identifier == null)
            return;

        if (isReservedWord(identifier)) {
            addError(404, identifier.getLine(), identifier.getLexeme());
            return;
        }

        String typeDecl = "";
        if (match(TokenType.SYMBOL, ":")) {
            typeDecl = parseType();
        }

        if (!match(TokenType.SYMBOL, "=")) {
            addError(402, peekSafeLine(), "Falta signo '=' o valor en la constante");
            return;
        }

        if (typeDecl.startsWith("array")) {
            parseArrayConst(typeDecl);
        } else {
            parseValueConst();
        }

        if (!match(TokenType.SYMBOL, ";")) {
            addError(401, peekSafeLine(), "Falta punto y coma al final de la constante");
        }
    }

    private void parseValueConst() {
        Token value = advance();
        if (value == null || (!value.getLexeme().matches("^(#\\d+)+$") && value.getType() != TokenType.NUMBER)) {
            addError(405, peekSafeLine(), "Valor de constante inválido");
        }
    }

    private void parseArrayConst(String typeDecl) {
        if (!match(TokenType.SYMBOL, "(")) {
            addError(405, peekSafeLine(), "Falta '(' en declaración de array constante");
            return;
        }

        while (!isAtEnd() && !peek().getLexeme().equals(")")) {
            Token val = advance();
            if (val.getType() != TokenType.NUMBER) {
                addError(405, val.getLine(), "Valor no numérico en array constante");
            }
            match(TokenType.SYMBOL, ",");
        }

        if (!match(TokenType.SYMBOL, ")")) {
            addError(405, peekSafeLine(), "Falta ')' en declaración de array constante");
        }
    }

    private String parseType() {
        StringBuilder sb = new StringBuilder();
        if (match(TokenType.ARRAY)) {
            sb.append("array");
            match(TokenType.SYMBOL, "[");
            sb.append("[");
            while (!peek().getLexeme().equals("]") && !isAtEnd()) {
                sb.append(advance().getLexeme());
            }
            match(TokenType.SYMBOL, "]");
            sb.append("]");
            match(TokenType.OF);
            sb.append(" of ").append(advance().getLexeme());
        }
        return sb.toString();
    }

    private Token consume(TokenType expectedType, int errorCode, String extraInfo) {
        if (isAtEnd()) {
            addError(errorCode, peekSafeLine(), "Fin inesperado del archivo");
            return null;
        }

        Token token = peek();
        if (token.getType() == expectedType) {
            return advance();
        } else {
            addError(errorCode, token.getLine(), extraInfo + " (Encontrado: '" + token.getLexeme() + "')");
            return null;
        }
    }

    private boolean match(TokenType type) {
        return match(type, null);
    }

    private boolean match(TokenType type, String lexeme) {
        Token token = peek();
        if (token != null && token.getType() == type && (lexeme == null || token.getLexeme().equals(lexeme))) {
            advance();
            return true;
        }
        return false;
    }

    private boolean isReservedWord(Token token) {
        try {
            TokenType.valueOf(token.getLexeme().toUpperCase());
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private int peekSafeLine() {
        return peek() != null ? peek().getLine() : 0;
    }

    private Token peek() {
        return position < tokens.size() ? tokens.get(position) : null;
    }

    private Token advance() {
        return position < tokens.size() ? tokens.get(position++) : null;
    }

    private boolean isAtEnd() {
        return position >= tokens.size();
    }

    private void addError(int code, int lineNumber, String extraInfo) {
        String message = errorDict.getError(code);
        if (extraInfo != null && !extraInfo.isEmpty())
            message += ": " + extraInfo;
        errors.add(String.format("Error %d en línea %04d: %s", code, lineNumber, message));
    }

    public List<String> getErrors() {
        return errors;
    }
}