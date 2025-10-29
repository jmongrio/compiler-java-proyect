package compiladores1.Validations;

import compiladores1.Models.ErrorDictionary;
import compiladores1.Models.Token;
import compiladores1.Models.TokenType;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author jmong
 */
public class ValidateUsesBlock {

    private final List<Token> tokens;
    private int position = 0;
    private final List<String> errors = new ArrayList<>();
    private final ErrorDictionary errorDict = new ErrorDictionary();
    private final boolean programIsValid;

    ValidateUsesBlock(List<Token> tokens, boolean programIsValid) {
        this.tokens = tokens;
        this.programIsValid = programIsValid;
    }

    public void parseUses() {
        boolean foundProgram = false;
        int lineProgram = 0;
        boolean foundUses = false;

        while (position < tokens.size()) {
            Token token = peekSafe();
            if (token == null) {
                break;
            }
            
            if (programIsValid && token.getType() == TokenType.PROGRAM) {
                lineProgram = token.getLine();
            }
            
            if (token.getType() == TokenType.USES) {
                foundUses = true;

                if (!programIsValid) {
                    addError(304, token.getLine(), null);
                    break;
                }

                if (lineProgram != -1 && (token.getLine() - lineProgram) > 1) {
                    addError(300, token.getLine(), null);
                }

                validateUsesSentence(token.getLine());
                break;
            }

            advance();
        }
        
        if (!foundUses && programIsValid) {
            addError(303, peekSafeLine(), null);
        }
    }

    private void validateUsesSentence(int lineUses) {
        advance();

        List<String> modules = new ArrayList<>();
        boolean ended = false;

        while (!isAtEnd()) {
            Token current = peekSafe();
            if (current == null) break;

            if (current.getLexeme().equals(";")) {
                ended = true;
                advance();
                break;
            }

            if (current.getType() == TokenType.IDENTIFIER) {
                modules.add(current.getLexeme());
            } else if (!current.getLexeme().equals(",")) {
                addError(302, current.getLine(), "Token inesperado en la sentencia uses: " + current.getLexeme());
                return;
            }

            advance();
        }

        if (modules.isEmpty()) {
            addError(302, lineUses, "La sentencia uses debe incluir al menos un comando");
        }

        if (!ended) {
            addError(301, peekSafeLine(), "Falta punto y coma ';' al final de uses");
        }
    }

    private boolean isAtEnd() {
        return position >= tokens.size();
    }

    private Token advance() {
        return position < tokens.size() ? tokens.get(position++) : null;
    }

    private Token peekSafe() {
        return position < tokens.size() ? tokens.get(position) : null;
    }

    private int peekSafeLine() {
        Token t = peekSafe();
        return t != null ? t.getLine() : 0;
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
