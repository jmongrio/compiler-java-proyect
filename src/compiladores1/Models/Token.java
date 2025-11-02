package compiladores1.Models;

/**
 *
 * @author jmong
 */
public class Token {
    private final String lexeme;
    private final TokenType type;
    private final int line;

    public Token(String lexeme, TokenType type, int line) {
        this.lexeme = lexeme;
        this.type = type;
        this.line = line;
    }

    public String getLexeme() { return lexeme; }
    public TokenType getType() { return type; }
    public int getLine() { return line; }

    @Override
    public String toString() {
        return String.format("<%s, '%s', line %d>", type, lexeme, line);
    }
}