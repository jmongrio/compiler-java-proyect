package compiladores1.Validations;

import java.util.ArrayList;
import java.util.List;

import compiladores1.Models.ErrorDictionary;
import compiladores1.Models.Token;
import compiladores1.Models.TokenType;

public class ValidateIfBlock {
    private final List<Token> tokens;
    private final List<String> errors = new ArrayList<>();
    private final ErrorDictionary errorDict = new ErrorDictionary();
    private int position = 0;
    private Token beginToken;

    public ValidateIfBlock(List<Token> tokens, Token beginToken) {
        this.tokens = tokens;
        this.beginToken = beginToken;
    }

    public void parseIfBlock() {
        while (position < tokens.size()) {
            Token current = peek();

            if (current.getType() == TokenType.BEGIN && current.getLine() == beginToken.getLine()) {
                advance();
                continue;
            }

            if (current.getType() == TokenType.IF) {
                advance();
                parseIfDeclaration();
            } else {
                advance();
            }
        }
    }

    private void parseIfDeclaration() {
        int depth = 0;

        advance();
        Token token = peek();
        if(token.getType() != TokenType.THEN){
            addError(1001, token.getLine(), null);
        }

        advance();
        token = peek();
        if(token.getType() != TokenType.BEGIN){
            addError(1002, token.getLine(), null);
        }
        else{
            depth++;
        }

        advance();
        token = peek();
        if(token.getType() != TokenType.END){
            addError(1004, token.getLine(), null);
        }
        else{
            depth--;
        }

        advance();
        token = peek();
        if(token.getType() != TokenType.SYMBOL || !token.getLexeme().equals(";")){
            addError(804, token.getLine(), null);
        }

        advance();
        token = peek();
        if(token.getType() != TokenType.ELSE){
            addError(1005, token.getLine(), null);
        }

        advance();
        token = peek();
        if(token.getType() != TokenType.BEGIN){
            addError(1006, token.getLine(), null);
        }
        else{
            depth++;
        }

        advance();
        token = peek();
        if(token.getType() != TokenType.END){
            addError(1004, token.getLine(), null);
        }
        else{
            depth--;
        }

        advance();
        token = peek();
        if(token.getType() != TokenType.SYMBOL || !token.getLexeme().equals(";")){
            addError(808, token.getLine(), null);
        }
    }

    private Token peek() {
        return position < tokens.size() ? tokens.get(position) : null;
    }

    private Token advance() {
        return position < tokens.size() ? tokens.get(position++) : null;
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