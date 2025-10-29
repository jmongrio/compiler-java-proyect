package compiladores1.Validations;

import compiladores1.Models.Token;
import compiladores1.Models.TokenType;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author jmong
 */
public class SyntaxAnalyzer {

    private final List<Token> tokens;
    private final String fileName;
    private final List<String> errors = new ArrayList<>();
    private boolean programIsValid = false;

    public SyntaxAnalyzer(List<Token> tokens, String fileName) {
        this.tokens = tokens;
        this.fileName = fileName;
    }
    
    public void analize(){
        parseProgram(tokens, fileName);
        parseUses(tokens, programIsValid);
    }
    
    private void parseProgram(List<Token> tokens, String fileName){
        ValidateProgramBlock validateProgram = new ValidateProgramBlock(tokens, fileName);
        validateProgram.parseProgram();
        programIsValid = validateProgram.getProgramIsValid();
        
        if(!validateProgram.getErrors().isEmpty()){
            errors.addAll(validateProgram.getErrors());
        }
    }
    
    private void parseUses(List<Token> tokens, boolean programValid){
        ValidateUsesBlock validateUses = new ValidateUsesBlock(tokens, programValid);
        validateUses.parseUses();
        
        if(!validateUses.getErrors().isEmpty()){
            errors.addAll(validateUses.getErrors());
        }
    }

//    public void parseProgram() {
//        expect(TokenType.PROGRAM);
//        expect(TokenType.IDENTIFIER);
//        expect(TokenType.SYMBOL, ";");
//        parseBlock();
//        expect(TokenType.SYMBOL, ".");
//        if (errors.isEmpty()) {
//            System.out.println("Programa sintácticamente correcto");
//        }
//    }
//
//    private void parseBlock() {
//        // Aquí podrías agregar manejo de VAR, CONST, etc.
//        expect(TokenType.BEGIN);
//        while (!match(TokenType.END) && peekSafe() != null) {
//            parseStatement();
//        }
//        expect(TokenType.END);
//    }
//
//    private void parseStatement() {
//        Token current = peekSafe();
//        if (current == null) {
//            return;
//        }
//
//        if (current.getType() == TokenType.WRITE || current.getType() == TokenType.WRITELN) {
//            parseWrite();
//        } else {
//            addError(current.getLine(), "Sentencia no reconocida: " + current.getLexeme());
//            advance(); // avanzamos para no entrar en un bucle infinito
//        }
//    }
//
//    private void parseWrite() {
//        Token writeToken = advance(); // consume write o writeln
//        expect(TokenType.SYMBOL, "(");
//
//        Token content = peekSafe();
//        if (content != null && content.getType() != TokenType.STRING && content.getType() != TokenType.IDENTIFIER) {
//            addError(content.getLine(), "Contenido inválido en " + writeToken.getLexeme());
//        }
//        advance(); // consumir contenido
//
//        expect(TokenType.SYMBOL, ")");
//        expect(TokenType.SYMBOL, ";");
//    }
//
//    // ---------------------------
//    // Utilidades del parser
//    // ---------------------------
//    private Token peek() {
//        return position < tokens.size() ? tokens.get(position) : null;
//    }
//
//    private Token peekSafe() {
//        return peek();
//    }
//
//    private Token advance() {
//        return position < tokens.size() ? tokens.get(position++) : null;
//    }
//
//    private boolean match(TokenType type) {
//        Token token = peekSafe();
//        if (token != null && token.getType() == type) {
//            advance();
//            return true;
//        }
//        return false;
//    }
//
//    private void expect(TokenType type) {
//        Token token = advance();
//        if (token == null || token.getType() != type) {
//            addError(token != null ? token.getLine() : 0,
//                    "Se esperaba " + type + " pero se encontró " + (token != null ? token.getLexeme() : "fin de archivo"));
//        }
//    }
//
//    private void expect(TokenType type, String lexeme) {
//        Token token = advance();
//        if (token == null || token.getType() != type || !token.getLexeme().equals(lexeme)) {
//            addError(token != null ? token.getLine() : 0,
//                    "Se esperaba '" + lexeme + "' pero se encontró " + (token != null ? token.getLexeme() : "fin de archivo"));
//        }
//    }

    private void addError(int lineNumber, String message) {
        errors.add(String.format("Error en línea %04d: %s", lineNumber, message));
    }

    public List<String> getErrors() {
        return errors;
    }
}