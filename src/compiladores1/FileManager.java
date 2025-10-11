package compiladores1;

import compiladores1.Models.ResponseModel;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author jmong
 */
public class FileManager {
    private final String fileName;
    private final List<String> lines = new ArrayList<>();

    public FileManager(String fileName) {
        this.fileName = fileName;
    }
    
    public ResponseModel readFile() {        
        ResponseModel response = new ResponseModel<List<String>>();
        
        File file = new File(fileName);
        if (!file.exists()) {
            response.setIsSuccess(false);
            response.setMessage("El archivo no existe.");
            return response;
        }
        
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                lines.add(line);
            }
        } catch (IOException e) {
            response.setIsSuccess(false);
            response.setMessage("Error al leer el archivo: " + e.getMessage());
            return response;
        }
        
        response.setIsSuccess(true);
        response.setObjectResult(lines);
        
        return response;
    }
}
