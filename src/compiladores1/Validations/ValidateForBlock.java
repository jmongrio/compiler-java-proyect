package compiladores1.Validations;

import java.util.ArrayList;
import java.util.List;

import compiladores1.Models.ErrorDictionary;
import compiladores1.Models.IdentificationModel;
import compiladores1.Models.Token;
import compiladores1.Models.TokenType;

/**
 *
 * @author jmong
 */

public class ValidateForBlock {
    private final List<Token> tokens;
    private final List<String> errors = new ArrayList<>();
    private final ErrorDictionary errorDict = new ErrorDictionary();
    private int position = 0;
    private Token beginToken;
    private List<IdentificationModel> identificationList;

    public ValidateForBlock(List<Token> tokens, Token beginToken, List<IdentificationModel> identificationList) {
        this.tokens = tokens;
        this.beginToken = beginToken;
        this.identificationList = identificationList;
    }

    public void parseForBlock() {
        while (position < tokens.size()) {
            Token current = peek();

            if (current.getType() == TokenType.BEGIN && current.getLine() == beginToken.getLine()) {
                advance();
                continue;
            }

            // if (current.getType() == TokenType.BEGIN) {
            // addError(900, current.getLine(), "Declaración encontrada después de BEGIN");
            // advance();
            // continue;
            // }

            if (current.getType() == TokenType.FOR) {
                advance();
                parseForDeclaration();
            } else {
                advance();
            }
        }
    }

    private void parseForDeclaration() {
        int positionAux = 0;
        int deepth = 0;

        Token token = peek();
        if (token.getType() != TokenType.IDENTIFIER) {
            addError(901, token.getLine(), null);
        }
        else{
                boolean identifierFound = false;
                for (IdentificationModel idToken : identificationList) {
                    if (idToken.getIdentifier().getLexeme().equals(token.getLexeme())) {
                        identifierFound = true;
                        break;
                    }
                }
                if (!identifierFound) {
                    addError(902, token.getLine(), "El identificador '" + token.getLexeme() + "' no ha sido declarado");
                }
        }

        advance();
        token = peek();
        if (token.getType() != TokenType.SYMBOL || !":=".equals(token.getLexeme())) {
            addError(903, token.getLine(), "Se esperaba el operador ':=' completo");
        }

        advance();
        token = peek();
        if (token.getType() != TokenType.NUMBER) {
            addError(904, token.getLine(), null);
        }

        advance();
        token = peek();
        if (token.getType() != TokenType.TO) {
            addError(905, token.getLine(), null);
        }

        advance();
        token = peek();
        if (token.getType() != TokenType.NUMBER) {
            addError(906, token.getLine(), null);
        }

        advance();
        token = peek();
        if (token.getType() != TokenType.DO) {
            addError(907, token.getLine(), null);
        }

        advance();
        token = peek();
        if (token.getType() != TokenType.BEGIN) {
            addError(908, token.getLine(), null);
        } else {
            deepth++;
        }

        while (deepth > 0) {
            advance();
            token = peek();

            if (token.getType() == TokenType.BEGIN) {
                deepth++;
            }

            if (token.getType() == TokenType.END) {
                deepth--;
            }
        }

        if (token.getType() != TokenType.END) {
            addError(910, token.getLine(), null);
        }

        advance();
        token = peek();
        if (token.getType() != TokenType.SYMBOL || !";".equals(token.getLexeme())) {
            addError(910, token.getLine(), null);
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