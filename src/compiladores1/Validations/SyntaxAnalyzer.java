package compiladores1.Validations;

import compiladores1.Models.IdentificationModel;
import compiladores1.Models.Token;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author jmong
 */
public class SyntaxAnalyzer {
    private final List<Token> tokens;
    private List<IdentificationModel> identificationList = new ArrayList<>();
    private final String fileName;
    private final List<String> errors = new ArrayList<>();
    private boolean programIsValid = false;
    private Token beginToken;
    private Token endToken;

    public SyntaxAnalyzer(List<Token> tokens, String fileName) {
        this.tokens = tokens;
        this.fileName = fileName;
    }

    public void analize() {
        parseProgram(tokens, fileName);
        parseUses(tokens, programIsValid);
        parseVar(tokens, beginToken);
        parseConst(tokens);
        parseBeginAndEndBlocks(tokens, programIsValid);
        parseWriteAndWriteLnBlock(tokens, programIsValid);
        parseForBlock(tokens);
    }

    private void parseProgram(List<Token> tokens, String fileName) {
        ValidateProgramBlock validateProgram = new ValidateProgramBlock(tokens, fileName);
        validateProgram.parseProgram();
        programIsValid = validateProgram.getProgramIsValid();

        if (!validateProgram.getErrors().isEmpty()) {
            errors.addAll(validateProgram.getErrors());
        }
    }

    private void parseUses(List<Token> tokens, boolean programValid) {
        ValidateUsesBlock validateUses = new ValidateUsesBlock(tokens, programValid);
        validateUses.parseUses();

        if (!validateUses.getErrors().isEmpty()) {
            errors.addAll(validateUses.getErrors());
        }
    }

    private void parseVar(List<Token> tokens, Token beginToken) {
        ValidateVarBlock validateVar = new ValidateVarBlock(tokens, beginToken);
        validateVar.parseVarBlock();
        identificationList = validateVar.getIdentificationList();

        if (!validateVar.getErrors().isEmpty()) {
            errors.addAll(validateVar.getErrors());
        }
    }

    private void parseConst(List<Token> tokens) {
        ValidateConstBlock validateConst = new ValidateConstBlock(tokens);
        validateConst.parseConstBlock();

        if (!validateConst.getErrors().isEmpty()) {
            errors.addAll(validateConst.getErrors());
        }
    }

    private void parseBeginAndEndBlocks(List<Token> tokens, boolean programValid) {
        ValidateBeginAndEndBlock validateBeginEnd = new ValidateBeginAndEndBlock(tokens, programValid);
        validateBeginEnd.parseUses();
        beginToken = validateBeginEnd.getBeginToken();
        endToken = validateBeginEnd.getEndToken();

        if (!validateBeginEnd.getErrors().isEmpty()) {
            errors.addAll(validateBeginEnd.getErrors());
        }
    }

    private void parseWriteAndWriteLnBlock(List<Token> tokens, boolean programValid) {
        ValidateWriteAndWriteLnBlock validateWrite = new ValidateWriteAndWriteLnBlock(tokens, programValid);
        validateWrite.parseWriteAndWriteLn();

        if (!validateWrite.getErrors().isEmpty()) {
            errors.addAll(validateWrite.getErrors());
        }
    }

    private void parseForBlock(List<Token> tokens) {
        ValidateForBlock validateFor = new ValidateForBlock(tokens, beginToken);
        validateFor.parseForBlock();

        if (!validateFor.getErrors().isEmpty()) {
            errors.addAll(validateFor.getErrors());
        }
    }

    public List<String> getErrors() {
        return errors;
    }
}