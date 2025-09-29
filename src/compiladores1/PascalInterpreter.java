package compiladores1;

import compiladores1.Models.ErrorDictionary;
import compiladores1.Models.ResponseModel;
import compiladores1.Validations.VBeginAndEnd;
import compiladores1.Validations.VComments;
import compiladores1.Validations.VConstants;
import compiladores1.Validations.VVariables;
import compiladores1.Validations.VProgram;
import compiladores1.Validations.VUses;
import compiladores1.Validations.VWrite;
import java.util.*;

public class PascalInterpreter {

    private final String fileName;
    private List<String> lines = new ArrayList<>();
    private final List<String> errors = new ArrayList<>();
    private final ErrorDictionary errorDict = new ErrorDictionary();

    public PascalInterpreter(String fileName) {
        this.fileName = fileName;
    }

    public void processFile() {
        FileManager fileManager = new FileManager(fileName);
        ResponseModel<List<String>> fileResponse = fileManager.readFile();
        if (!fileResponse.isIsSuccess()) {
            System.out.println(String.format("No se encontró ningún archivo con le nombre %s", fileName));
            return;
        }

        lines = fileResponse.getObjectResult();

        String[] splitFileName = fileName.split("\\.");
        String baseName = splitFileName[0];
        if (baseName.contains(".")) {
            baseName = baseName.substring(0, baseName.lastIndexOf("."));
        }

        // Validation program
        VProgram programValidation = new VProgram(lines, baseName);
        programValidation.validateProgram();
        errors.addAll(programValidation.getErrors());

        // Validation use
        VUses useValidation = new VUses(lines, baseName);
        useValidation.validateUses();
        errors.addAll(useValidation.getErrors());

        // Validation variables
        VVariables identifierValidation = new VVariables(lines);
        identifierValidation.validateIdentifier();
        errors.addAll(identifierValidation.getErrors());
        
        // Validation constants
        VConstants constantsValidation = new VConstants(lines);
        constantsValidation.validateConstants();
        errors.addAll(constantsValidation.getErrors());
        
        // Validation Begin and End
        VBeginAndEnd beginAndEndValidation = new VBeginAndEnd(lines);
        beginAndEndValidation.validateBeginAndEnd();
        errors.addAll(beginAndEndValidation.getErrors());
        
        // Validation Write
        VWrite writeValidation = new VWrite(lines);
        writeValidation.validateWrite();
        errors.addAll(writeValidation.getErrors());
        
        // Validation Comments
        VComments commentsValidation = new VComments(lines);
        commentsValidation.validateComments();
        errors.addAll(commentsValidation.getErrors());

        ErrorFileManager errorFile = new ErrorFileManager(fileName, errors, lines);
        errorFile.writeErrors();
    }
}
