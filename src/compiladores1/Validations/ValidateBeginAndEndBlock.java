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
public class ValidateBeginAndEndBlock {

    private final List<Token> tokens;
    private int position = 0;
    private final List<String> errors = new ArrayList<>();
    private final ErrorDictionary errorDict = new ErrorDictionary();
    private final boolean programIsValid;

    ValidateBeginAndEndBlock(List<Token> tokens, boolean programIsValid) {
        this.tokens = tokens;
        this.programIsValid = programIsValid;
    }

    public void parseUses() {
        int lineProgram = 0;

        List<Token> beginLines = new ArrayList<>();
        List<Token> endLines = new ArrayList<>();
        List<Token> endDotLines = new ArrayList<>();

        while (position < tokens.size()) {
            Token token = peekSafe();
            if (token == null) {
                break;
            }

            if (programIsValid && token.getType() == TokenType.PROGRAM) {
                lineProgram = token.getLine();
            }

            if (token.getType() == TokenType.BEGIN) {
                beginLines.add(token);
                validateLineNoOtherTokens(token);
            }

            if (token.getType() == TokenType.END) {
                advance();
                token = peekSafe();

                if (token.getType() == TokenType.SYMBOL && token.getLexeme().equals(".")) {
                    endDotLines.add(token);
                    endLines.remove(endLines.size() - 1);
                } else {
                    endLines.add(token);
                }
                validateLineNoOtherTokens(token);
            }

            advance();
        }

        countBeginAndEndBlocks(beginLines, endLines, endDotLines);
    }

    private void validateLineNoOtherTokens(Token token) {
        List<Token> tokenInLine = new ArrayList<>();
        for (Token tokenCheckLine : tokens) {
            if (tokenCheckLine.getLine() == token.getLine()) {
                tokenInLine.add(tokenCheckLine);
            }
        }

        if (tokenInLine.size() > 2) {
            addError(601, token.getLine(), null);
        }
    }

    private void countBeginAndEndBlocks(List<Token> beginLines, List<Token> endLines, List<Token> endDotLines) {
        int beginCount = beginLines.size();
        int endCount = endLines.size() + endDotLines.size();

        if (beginCount > endCount) {
            for (int i = endCount; i < beginCount; i++) {
                Token token = beginLines.get(i);
                addError(607, token.getLine(), "Falta 'end' correspondiente para 'begin'");
            }
        } else if (endCount > beginCount) {
            for (int i = beginCount; i < endCount; i++) {
                Token token;
                if (i < endLines.size()) {
                    token = endLines.get(i);
                } else {
                    token = endDotLines.get(i - endLines.size());
                }
                addError(607, token.getLine(), "Falta 'begin' correspondiente para 'end'");
            }
        }
        
        if(endDotLines.isEmpty()){
            addError(606, 0, null);
        }
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
