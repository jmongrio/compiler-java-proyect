package compiladores1.Validations;

import compiladores1.Models.ReservedWordDictionary;
import java.util.ArrayList;
import java.util.List;
import compiladores1.Models.Token;
import compiladores1.Models.TokenType;

/**
 *
 * @author jmong
 */
public class LexicalAnalyzer {

    private final List<String> lines;
    private final List<Token> tokens = new ArrayList<>();
    private final List<String> errors = new ArrayList<>();

    public LexicalAnalyzer(List<String> lines) {
        this.lines = lines;
    }

    public void analyze() {
        int lineNumber = 0;

        for (String line : lines) {
            if (line.trim().isEmpty()) {
                lineNumber++;
                continue;
            }

            line = removeComments(line);

            String[] parts = tokenize(line);

            for (String part : parts) {
                part = part.trim();
                if (part.isEmpty()) {
                    continue;
                }
                classifyToken(part, lineNumber);
            }

            lineNumber++;
        }
    }

    // Splitter symbols
    private String[] tokenize(String line) {
        return line.trim().split("(?=[;,.()\\[\\]{}=:+-/*])|(?<=[;,.()\\[\\]{}=:+-/*])|\\s+");
    }

    // Skip valid comments
    private String removeComments(String line) {
        int index = line.indexOf("//");
        if (index != -1) {
            return line.substring(0, index);
        }

        if (line.contains("{") && line.contains("}")) {
            return line.replaceAll("\\{.*?\\}", "");
        }

        return line;
    }

    private void classifyToken(String token, int lineNumber) {
        String lower = token.toLowerCase();

        if (ReservedWordDictionary.isReserved(lower)) {
            TokenType type = TokenType.valueOf(lower.toUpperCase());
            tokens.add(new Token(token, type, lineNumber));
        } else if (token.matches("^[a-zA-Z_][a-zA-Z0-9_]*$")) {
            tokens.add(new Token(token, TokenType.IDENTIFIER, lineNumber));
        } else if (token.matches("^(#[0-9]+)+$")) {
            tokens.add(new Token(token, TokenType.CHAR_CODE, lineNumber));
        } else if (token.matches("^[0-9]+$")) {
            tokens.add(new Token(token, TokenType.NUMBER, lineNumber));
        } else if (token.matches("^'.*'$")) {
            tokens.add(new Token(token, TokenType.STRING, lineNumber));
        } else if (token.matches("^[;,.()\\[\\]{}=:+-/*]$")) {
            tokens.add(new Token(token, TokenType.SYMBOL, lineNumber));
        } else {
            errors.add(String.format("Token desconocido '%s' en línea %d", token, lineNumber));
            tokens.add(new Token(token, TokenType.UNKNOWN, lineNumber));
        }
    }

    public List<Token> getTokens() {
        return tokens;
    }

    public List<String> getErrors() {
        return errors;
    }
}
