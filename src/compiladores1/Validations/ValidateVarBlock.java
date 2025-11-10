package compiladores1.Validations;

import java.util.ArrayList;
import java.util.List;

import compiladores1.Models.ErrorDictionary;
import compiladores1.Models.IdentificationModel;
import compiladores1.Models.Token;
import compiladores1.Models.TokenType;

public class ValidateVarBlock {
    private final List<Token> tokens;
    private List<IdentificationModel> identificationList = new ArrayList<>();
    private final List<String> errors = new ArrayList<>();
    private final ErrorDictionary errorDict = new ErrorDictionary();
    private int position = 0;
    private final Token beginToken;

    public ValidateVarBlock(List<Token> tokens, Token beginToken) {
        this.tokens = tokens;
        this.beginToken = beginToken;
    }

    public void parseVarBlock() {
        while (position < tokens.size()) {
            Token token = peek();
            if (token == null) {
                break;
            }

            if (token.getType() != TokenType.VAR) {
                advance();
                continue;
            }

            if (token.getType() == TokenType.VAR) {
                advance();
                parseVarDeclarations();
            } else {
                advance();
            }
        }
    }

    private void parseVarDeclarations() {
        IdentificationModel identificationModel = new IdentificationModel();
        identificationModel.setConstant(false);

        Token token = peek();
        if (token.getType() != TokenType.IDENTIFIER) {
            addError(502, token.getLine(), null);
        } else {
            identificationModel.setIdentifier(token);
        }

        advance();
        token = peek();
        if (token.getType() != TokenType.SYMBOL || !token.getLexeme().equals(":")) {
            addError(501, token.getLine(), null);
        }

        advance();
        token = peek();
        if (token.getType() != TokenType.STRING && token.getType() != TokenType.NUMBER
                && token.getType() != TokenType.ARRAY) {
            addError(503, token.getLine(), null);
        } else {
            identificationModel.setType(token);
        }

        if (token.getType() == TokenType.STRING) {
            validateStringType();
        } else if (token.getType() == TokenType.NUMBER) {
            validateNumberType();
        } else if (token.getType() == TokenType.ARRAY) {
            validateArrayType();
        }

        identificationList.add(identificationModel);
        advance();
    }

    private void validateStringType() {
        advance();
        Token token = peek();
        if (token.getType() != TokenType.SYMBOL || !token.getLexeme().equals(";")) {
            addError(500, token.getLine(), null);
        }
    }

    private void validateNumberType() {
        advance();
        Token token = peek();
        if (token.getType() != TokenType.SYMBOL || !token.getLexeme().equals(";")) {
            addError(500, token.getLine(), null);
        }
    }

    private void validateArrayType() {
        advance();
        Token token = peek();
        if( token.getType() != TokenType.SYMBOL || !token.getLexeme().equals("[")) {
            addError(504, token.getLine(), null);
            return;
        }

        advance();
        token = peek();
        if( token.getType() != TokenType.NUMBER) {
            addError(505, token.getLine(), null);
            return;
        }

        advance();
        token = peek();
        if( token.getType() != TokenType.SYMBOL || !token.getLexeme().equals("..")) {
            addError(506, token.getLine(), null);
            return;
        }

        advance();
        token = peek();
        if( token.getType() != TokenType.NUMBER) {
            addError(505, token.getLine(), null);
            return;
        }

        advance();
        token = peek();
        if( token.getType() != TokenType.SYMBOL || !token.getLexeme().equals("]")) {
            addError(504, token.getLine(), null);
            return;
        }

        advance();
        token = peek();
        if( token.getType() != TokenType.OF) {
            addError(507, token.getLine(), null);
            return;
        }

        advance();
        token = peek();
        if( token.getType() != TokenType.STRING && token.getType() != TokenType.NUMBER) {
            addError(508, token.getLine(), null);
            return;
        }

        advance();
        token = peek();
        if( token.getType() != TokenType.SYMBOL || !token.getLexeme().equals(";")) {
            addError(500, token.getLine(), null);
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

    public List<IdentificationModel> getIdentificationList() {
        return identificationList;
    }
}