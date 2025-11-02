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
        parseConst(tokens);
        parseBeginAndEndBlocks(tokens, programIsValid);
        parseWriteAndWriteLnBlock(tokens, programIsValid);
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
    
    private void parseConst(List<Token> tokens){
        ValidateConstBlock validateConst = new ValidateConstBlock(tokens);
        validateConst.parseConstBlock();
        
        if(!validateConst.getErrors().isEmpty()){
            errors.addAll(validateConst.getErrors());
        }
    }

    private void parseBeginAndEndBlocks(List<Token> tokens, boolean programValid){
        ValidateBeginAndEndBlock validateBeginEnd = new ValidateBeginAndEndBlock(tokens, programValid);
        validateBeginEnd.parseUses();
        
        if(!validateBeginEnd.getErrors().isEmpty()){
            errors.addAll(validateBeginEnd.getErrors());
        }
    }

    private void parseWriteAndWriteLnBlock(List<Token> tokens, boolean programValid){
        ValidateWriteAndWriteLnBlock validateWrite = new ValidateWriteAndWriteLnBlock(tokens, programValid);
        validateWrite.parseWriteAndWriteLn();
        
        if(!validateWrite.getErrors().isEmpty()){
            errors.addAll(validateWrite.getErrors());
        }
    }

    public List<String> getErrors() {
        return errors;
    }
}