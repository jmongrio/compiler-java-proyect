package compiladores1.Validations;

import compiladores1.Models.ReservedWordDictionary;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import compiladores1.Models.Token;
import compiladores1.Models.TokenType;

public class LexicalAnalyzer {

    private final List<String> lines;
    private final List<Token> tokens = new ArrayList<>();
    private final List<String> errors = new ArrayList<>();

    public LexicalAnalyzer(List<String> lines) {
        this.lines = lines;
    }

    public void analyze() {
        int lineNumber = 0;
        boolean afterProgramUses = false;

        for (String line : lines) {
            if (line.trim().isEmpty()) {
                lineNumber++;
                continue;
            }

            if (!afterProgramUses) {
                if (line.toLowerCase().contains("program") || line.toLowerCase().contains("uses")) {
                    afterProgramUses = true;
                }
            }

            Pattern pattern = Pattern.compile(
                    "('([^']|'')*')"
                    + "|(#[0-9]+)+"
                    + "|([a-zA-Z_][a-zA-Z0-9_]*)"
                    + "|([0-9]+)"
                    + "|(<=|>=|<>|:=|\\.{2})"
                    + "|([;,.()\\[\\]{}=:+\\-/*<>])"
            );

            Matcher matcher = pattern.matcher(line);
            while (matcher.find()) {
                String part = matcher.group();
                classifyToken(part, lineNumber, afterProgramUses);
            }

            lineNumber++;
        }
    }

    private String removeComments(String line) {
        int index = line.indexOf("//");
        if (index != -1) {
            line = line.substring(0, index);
        }
        return line.replaceAll("\\{.*?\\}", "");
    }

    private void classifyToken(String token, int lineNumber, boolean afterProgramUses) {
        String lower = token.toLowerCase();

        if (ReservedWordDictionary.isReserved(lower)) {
            TokenType type = TokenType.valueOf(lower.toUpperCase());
            tokens.add(new Token(token, type, lineNumber));

        } else if (token.matches("^[a-zA-Z_][a-zA-Z0-9_]*$")) {
            if(lower.equals("integer")){
                tokens.add(new Token(token, TokenType.NUMBER, lineNumber));
                return;
            }

            if(lower.equals("word")){
                tokens.add(new Token(token, TokenType.STRING, lineNumber));
                return;
            }

            if(lower.equals("string")){
                tokens.add(new Token(token, TokenType.STRING, lineNumber));
                return;
            }

            if(lower.charAt(0) == '#'){
                tokens.add(new Token(token, TokenType.CHAR_CODE, lineNumber));
                return;
            }

            tokens.add(new Token(token, TokenType.IDENTIFIER, lineNumber));

        } else if (token.matches("^(#[0-9]+)+$")) {
            tokens.add(new Token(token, TokenType.CHAR_CODE, lineNumber));

        } else if (token.matches("^'([^']|'')*'$")) {
            tokens.add(new Token(token, TokenType.STRING, lineNumber));

        } else if (token.matches("^[0-9]+$")) {
            tokens.add(new Token(token, TokenType.NUMBER, lineNumber));

        } else if (token.matches("^(<=|>=|<>|:=|\\.{2}|[;,.()\\[\\]{}=:+\\-/*<>])$")) {
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
