package compiladores1;

import compiladores1.Models.ResponseModel;
import compiladores1.Models.Token;
import compiladores1.Validations.CommentValidator;
import compiladores1.Validations.LexicalAnalyzer;
import compiladores1.Validations.SyntaxAnalyzer;
import java.util.*;

public class PascalInterpreter {

    private final String fileName;
    private List<String> lines = new ArrayList<>();
    private final List<String> errors = new ArrayList<>();

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

        CommentValidator validator = new CommentValidator(lines);
        validator.validate();

        LexicalAnalyzer lexer = new LexicalAnalyzer(lines);
        lexer.analyze();
        if(!lexer.getErrors().isEmpty()){
            errors.addAll(lexer.getErrors());
        }
        
        List<Token> tokens = lexer.getTokens();
        for(Token token : tokens){
            System.out.println(token);
        }

        if (lexer.getErrors().isEmpty()) {
            SyntaxAnalyzer parser = new SyntaxAnalyzer(lexer.getTokens(), baseName);
            parser.analize();
            
            if(!parser.getErrors().isEmpty()){
                errors.addAll(parser.getErrors());
            }
        } else {
            lexer.getErrors().forEach(System.out::println);
        }

        ErrorFileManager errorFile = new ErrorFileManager(fileName, errors, lines);
        errorFile.writeErrors();
    }
}
