package compiladores1;

import compiladores1.Models.ErrorDictionary;
import compiladores1.Models.ResponseModel;
import compiladores1.Validations.Program;
import compiladores1.Validations.Uses;
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
        Program programValidation = new Program(lines, baseName);
        programValidation.validateProgram();
        errors.addAll(programValidation.getErrors());

        // Validation use
        Uses useValidation = new Uses(lines, baseName);
        useValidation.validateUses();
        errors.addAll(useValidation.getErrors());
//        validateUses();
//        validateConst();
//        validateVar();
//        validateBeginEnd();
//        validateWrite();
//        validateComments();
        ErrorFileManager errorFile = new ErrorFileManager(fileName, errors, lines);
        errorFile.writeErrors();
    }

//    private void validateConst() {
//        boolean insideConst = false;
//        boolean beforeVar = true;
//
//        for (int i = 0; i < lines.size(); i++) {
//            String line = lines.get(i).trim();
//
//            if (line.toLowerCase().startsWith("var")) {
//                beforeVar = false;
//            }
//
//            if (line.toLowerCase().startsWith("begin")) {
//                if (insideConst) {
//                    addError(400, i, null); // mensaje del diccionario
//                }
//                break; // Ya no buscamos más
//            }
//
//            if (line.toLowerCase().startsWith("const")) {
//                insideConst = true;
//
//                // Extraer parte después de const
//                String declaration = line.substring(5).trim();
//
//                // Validar formato
//                if (!declaration.endsWith(";")) {
//                    addError(401, i, null);
//                }
//
//                // Identificador
//                String withoutSemicolon = declaration.replace(";", "").trim();
//                String[] parts = withoutSemicolon.split("=", 2);
//
//                if (parts.length < 2) {
//                    addError(402, i, null);
//                    continue;
//                }
//
//                String identifier = parts[0].trim();
//                String value = parts[1].trim();
//
//                // Validar identificador
//                if (!identifier.matches("^[a-zA-Z][a-zA-Z_]*$")) {
//                    addError(403, i, identifier);
//                } else if (Helpers.isReservedWord(identifier)) {
//                    addError(404, i, identifier);
//                }
//
//                // Validar valor (número o string entre comillas)
//                if (!(value.matches("^[0-9]+$") || value.matches("^'.*'$") || value.contains("array"))) {
//                    addError(405, i, value);
//                }
//            }
//        }
//    }
//
//    private void validateVar() {
//        boolean insideVar = false;
//
//        for (int i = 0; i < lines.size(); i++) {
//            String line = lines.get(i).trim().toLowerCase();
//
//            if (line.startsWith("begin")) {
//                insideVar = false;
//                break; // ya no procesamos más variables
//            }
//
//            if (line.startsWith("var")) {
//                insideVar = true;
//                continue; // línea "var" sola
//            }
//
//            if (insideVar && !line.isEmpty()) {
//                // Validar declaración de variable
//                if (!line.endsWith(";")) {
//                    addError(500, i, null);
//                    continue;
//                }
//
//                String withoutSemicolon = line.substring(0, line.length() - 1).trim();
//                String[] parts = withoutSemicolon.split(":");
//
//                if (parts.length != 2) {
//                    addError(501, i, null);
//                    continue;
//                }
//
//                String identifier = parts[0].trim();
//                String type = parts[1].trim();
//
//                // Validar identificador
//                if (!identifier.matches("^[a-zA-Z][a-zA-Z_]*$")) {
//                    addError(502, i, identifier);
//                } else if (Helpers.isReservedWord(identifier)) {
//                    addError(503, i, identifier);
//                }
//
//                // Validar tipo
//                if (!(type.equals("integer") || type.equals("string") || type.equals("word") || type.contains("array"))) {
//                    addError(504, i, type);
//                }
//            }
//        }
//    }
//
//    private void validateBeginEnd() {
//        boolean foundBegin = false;
//        boolean foundEnd = false;
//
//        for (int i = 0; i < lines.size(); i++) {
//            String line = lines.get(i).trim().toLowerCase();
//
//            if (line.startsWith("begin")) {
//                if (foundBegin) {
//                    addError(600, i, null);
//                }
//                if (!line.equals("begin")) {
//                    addError(601, i, null);
//                }
//                foundBegin = true;
//            }
//
//            if (line.startsWith("end")) {
//                if (!foundBegin) {
//                    addError(602, i, null);
//                }
//
//                if (!line.equals("end.")) {
//                    addError(603, i, null);
//                }
//
//                // Verificar si hay más líneas después de end.
//                if (i != lines.size() - 1) {
//                    addError(604, i, null);
//                }
//
//                foundEnd = true;
//            }
//        }
//
//        if (!foundBegin) {
//            addError(605, 0, null);
//        }
//        if (!foundEnd) {
//            addError(606, 0, null);
//        }
//    }
//
//    private void validateWrite() {
//        boolean insideProgram = false;
//
//        for (int i = 0; i < lines.size(); i++) {
//            String line = lines.get(i).trim();
//
//            if (line.equalsIgnoreCase("begin")) {
//                insideProgram = true;
//                continue;
//            }
//            if (line.equalsIgnoreCase("end.")) {
//                insideProgram = false;
//            }
//
//            if (insideProgram && (line.toLowerCase().startsWith("write") || line.toLowerCase().startsWith("writeln"))) {
//                // Validar que termine en ;
//                if (!line.endsWith(";")) {
//                    addError(700, i, null);
//                }
//
//                // Extraer contenido entre paréntesis
//                int open = line.indexOf("(");
//                int close = line.lastIndexOf(")");
//                if (open == -1 || close == -1 || close <= open + 1) {
//                    addError(701, i, null);
//                    continue;
//                }
//
//                String content = line.substring(open + 1, close).trim();
//
//                if (content.isEmpty()) {
//                    addError(702, i, null);
//                } else if (content.startsWith("'") && content.endsWith("'")) {
//                    // Texto válido
//                } else {
//                    // Verificar si es un identificador válido
//                    if (!content.matches("^[a-zA-Z][a-zA-Z_]*$")) {
//                        addError(703, i, content);
//                    }
//                    // ⚠️ Aquí podrías agregar validación cruzada con variables declaradas
//                }
//            }
//        }
//    }
//
//    private void validateComments() {
//        boolean foundProgram = false;
//        boolean foundEnd = false;
//
//        for (int i = 0; i < lines.size(); i++) {
//            String line = lines.get(i);
//            String trimmed = line.trim();
//
//            // Detectar si program ya apareció
//            if (trimmed.toLowerCase().startsWith("program")) {
//                foundProgram = true;
//            }
//
//            // Detectar si end. ya apareció
//            if (trimmed.equalsIgnoreCase("end.")) {
//                foundEnd = true;
//            }
//
//            // 1. Comentarios antes de program
//            if (!foundProgram && (trimmed.startsWith("//") || trimmed.startsWith("{"))) {
//                addError(800, i, null);
//            }
//
//            // 2. Comentarios después de end.
//            if (foundEnd && (trimmed.startsWith("//") || trimmed.startsWith("{"))) {
//                addError(801, i, null);
//            }
//
//            // 3. Comentarios con doble slash deben empezar con //
//            if (trimmed.contains("/") && !trimmed.startsWith("//") && trimmed.contains("//")) {
//                addError(802, i, null);
//            }
//
//            // 4. Comentarios entre {}
//            if (trimmed.contains("{") && !trimmed.contains("}")) {
//                addError(803, i, null);
//            }
//
//            // 5. Comentarios después de punto y coma
//            int semicolonIndex = trimmed.indexOf(";");
//            if (semicolonIndex != -1) {
//                String afterSemicolon = trimmed.substring(semicolonIndex + 1).trim();
//                if (afterSemicolon.startsWith("//") || afterSemicolon.startsWith("{")) {
//                    addError(804, i, null);
//                }
//            }
//        }
//    }
}
