package compiladores1;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author jmong
 */
public class ErrorFileManager {
    private final String fileName;
    private List<String> errors = new ArrayList<>();
    private List<String> lines = new ArrayList<>();

    public ErrorFileManager(String fileName, List<String> errors, List<String> lines) {
        this.fileName = fileName;
        this.errors = errors;
        this.lines = lines;
    }
    
    public void writeErrors() {
        String baseName = fileName.substring(0, fileName.lastIndexOf("."));
        String errFileName = baseName + "-errores.err";

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(errFileName))) {
            if(!lines.isEmpty()){
                int lineNumber = 0;                
                for (String line : lines) {
                    String lineText = String.format("%04d %s", lineNumber, line);
                    bw.write(lineText);
                    bw.newLine();
                    lineNumber++;
                }
            }
            
            if (errors.isEmpty()) {
                bw.write("No se encontraron errores.");
            } else {
                for (String error : errors) {
                    bw.write(error);
                    bw.newLine();
                }
            }
            System.out.println("Archivo de errores generado: " + errFileName);
        } catch (IOException e) {
            System.out.println("Error al escribir el archivo de errores: " + e.getMessage());
        }
    }
}
