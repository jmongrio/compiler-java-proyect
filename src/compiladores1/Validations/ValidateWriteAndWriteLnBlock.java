package compiladores1.Validations;

import java.util.ArrayList;
import java.util.List;

import compiladores1.Models.ErrorDictionary;
import compiladores1.Models.Token;
import compiladores1.Models.TokenType;

/**
 *
 * @author jmong
 */
public class ValidateWriteAndWriteLnBlock {
    private final List<Token> tokens;
    private int position = 0;
    private final List<String> errors = new ArrayList<>();
    private final ErrorDictionary errorDict = new ErrorDictionary();
    private final boolean programIsValid;

    public ValidateWriteAndWriteLnBlock(List<Token> tokens, boolean programIsValid) {
        this.tokens = tokens;
        this.programIsValid = programIsValid;
    }

    public void parseWriteAndWriteLn() {
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

            if( token.getType() == TokenType.WRITE || token.getType() == TokenType.WRITELN) {
                foundUses = true;

                validateWriteSentence(token.getLine());
            }

            advance();
        }
    }

    private void validateWriteSentence(int lineNumber) {
        Token token = peekSafe();

        token = peekSafe();
        if (token.getType() != TokenType.SYMBOL && !token.getLexeme().equals("(")) {
            addError(701, token.getLine(), null);
            return;
        }

        advance();
        token = peekSafe();

        if (token.getType() != TokenType.SYMBOL && !token.getLexeme().equals(")")) {
            addError(701, token.getLine(), null);
            return;
        }

        advance();
        token = peekSafe();

        if(token.getType() !=TokenType.SYMBOL && !token.getLexeme().equals(";")) {
            addError(700, token.getLine(), null);
        }
    }

    private Token advance() {
        return position < tokens.size() ? tokens.get(position++) : null;
    }

    private Token peekSafe() {
        return position < tokens.size() ? tokens.get(position) : null;
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