package compiladores1;

public class Compiladores1 {
    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("Debe incluir el nombre del archivo como argumento.");
            return;
        }

        String fileName = args[0];
        if (!fileName.endsWith("." + Helpers.PASCAL_EXTENSION)) {
            System.out.println("El archivo debe tener extensión ." + Helpers.PASCAL_EXTENSION);
            return;
        }

        PascalInterpreter interpreter = new PascalInterpreter(fileName);
        interpreter.processFile();  
    }
}