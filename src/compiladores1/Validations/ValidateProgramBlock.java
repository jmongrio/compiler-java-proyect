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
public class ValidateProgramBlock {

    private final List<Token> tokens;
    private final String fileName;
    private int position = 0;
    private final List<String> errors = new ArrayList<>();
    private final ErrorDictionary errorDict = new ErrorDictionary();

    ValidateProgramBlock(List<Token> tokens, String fileName) {
        this.tokens = tokens;
        this.fileName = fileName;
    }

    public void parseProgram() {
        boolean foundProgram = false;
        int lineProgram;

        while (position < tokens.size()) {
            Token tokenToCheck = peekSafe();
            if (tokenToCheck == null) {
                break;
            }

            if (tokenToCheck.getType() != TokenType.PROGRAM) {
                advance();
                continue;
            }
            
            if(foundProgram && tokenToCheck.getType() == TokenType.PROGRAM){
                addError(204, tokenToCheck.getLine(), null);
                advance();
                continue;
            }

            if (checkLinePosition(tokenToCheck)) {
                foundProgram = true;
                lineProgram = tokenToCheck.getLine();
            }
            advance();

            tokenToCheck = peekSafe();

            if (tokenToCheck.getType() != TokenType.IDENTIFIER) {
                addError(201, tokenToCheck.getLine(), null);
            }
            
            if(!tokenToCheck.getLexeme().equals(fileName)){
                addError(203, tokenToCheck.getLine(), null);
            }

            advance();
            tokenToCheck = peekSafe();

            if (tokenToCheck.getType() != TokenType.SYMBOL) {
                addError(202, tokenToCheck.getLine(), null);
            }
        }

        if (!foundProgram) {
            addError(200, 0, null);
        }
    }

    private boolean checkLinePosition(Token token) {
        return token.getLine() == 0;
    }

    private Token peek() {
        return position < tokens.size() ? tokens.get(position) : null;
    }

    private Token peekSafe() {
        return peek();
    }

    private Token advance() {
        return position < tokens.size() ? tokens.get(position++) : null;
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
