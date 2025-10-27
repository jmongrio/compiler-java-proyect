package compiladores1.Validations;

import java.util.List;

/**
 *
 * @author jmong
 */
public class CommentValidator {
    private final List<String> lines;
    private boolean programFound = false;
    private boolean endFound = false;

    public CommentValidator(List<String> lines) {
        this.lines = lines;
    }

    public void validate() {
        for (int i = 0; i < lines.size(); i++) {
            String line = lines.get(i);
            String trimmed = line.trim();
            int lineNumber = i + 1;

            // Detectar program y end.
            if (trimmed.toLowerCase().startsWith("program")) programFound = true;
            if (trimmed.toLowerCase().contains("end.")) endFound = true;

            // Comentarios antes de program
            if (!programFound && (trimmed.startsWith("//") || trimmed.startsWith("{"))) {
                reportError(lineNumber, "No se permiten comentarios antes de 'program'.");
            }

            // Comentarios después de end.
            if (endFound && (trimmed.startsWith("//") || trimmed.startsWith("{"))) {
                reportError(lineNumber, "No se permiten comentarios después de 'end.'.");
            }

            // Validar comentarios de tipo "//"
            if (trimmed.contains("/")) {
                validateSlashComment(trimmed, lineNumber);
            }

            // Validar comentarios con "{ }"
            if (trimmed.contains("{") || trimmed.contains("}")) {
                validateBraceComment(trimmed, lineNumber);
            }
        }
    }

    private void validateSlashComment(String line, int lineNumber) {
        if (line.contains("/") && !line.contains("//")) {
            reportError(lineNumber, "Comentario inválido. Debe usar '//' sin espacios ni un solo '/'.");
        }

        int index = line.indexOf("//");
        if (index > 0 && line.substring(0, index).contains(";")) {
            reportError(lineNumber, "No se permiten comentarios después de ';'.");
        }
    }

    private void validateBraceComment(String line, int lineNumber) {
        int openIndex = line.indexOf("{");
        int closeIndex = line.indexOf("}");

        if (openIndex != -1 && closeIndex == -1) {
            reportError(lineNumber, "Comentario entre llaves no cerrado.");
        }
        if (openIndex == -1 && closeIndex != -1) {
            reportError(lineNumber, "Comentario entre llaves sin apertura.");
        }

        if (line.contains("}") && !line.endsWith("}")) {
            reportError(lineNumber, "No se permiten comentarios después de código.");
        }

        if (openIndex > 0 && line.substring(0, openIndex).contains(";")) {
            reportError(lineNumber, "No se permiten comentarios después de ';'.");
        }
    }

    private void reportError(int lineNumber, String message) {
        System.out.println("Error semántico (comentarios) en línea " + lineNumber + ": " + message);
    }
}