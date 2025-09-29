package compiladores1.Models;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author jmong
 */

public class ErrorDictionary {
    private final Map<Integer, String> compilerErrors = new HashMap<>();

    public ErrorDictionary() {
        // validateProgram
        compilerErrors.put(200, "No encuentra la palabra inicial program");
        compilerErrors.put(201, "Falta el identificador después de program");
        compilerErrors.put(202, "La declaración program no finaliza con punto y coma");
        compilerErrors.put(203, "El nombre del programa no coincide con el nombre del archivo");
        compilerErrors.put(204, "La palabra program no debe aparecer más de una vez");
        compilerErrors.put(205, "No deben haber comentarios antes de la palabra inicial program");

        // validateUses
        compilerErrors.put(300, "No debe haber líneas en blanco o comentarios entre program y uses");
        compilerErrors.put(301, "La sentencia uses debe finalizar con punto y coma");
        compilerErrors.put(302, "La sentencia uses debe incluir al menos un comando");
        compilerErrors.put(303, "No se encontró la sentencia uses después de program");

        // validateConst
        compilerErrors.put(400, "Constantes declaradas después de begin no son válidas");
        compilerErrors.put(401, "La declaración de constante debe finalizar con punto y coma");
        compilerErrors.put(402, "Constante sin signo '=' o sin valor asignado");
        compilerErrors.put(403, "Identificador de constante no válido");
        compilerErrors.put(404, "El identificador de constante usa una palabra reservada");
        compilerErrors.put(405, "Valor de constante inválido");

        // validateVar
        compilerErrors.put(500, "La declaración de variable debe finalizar con punto y coma");
        compilerErrors.put(501, "Formato inválido en declaración de variable, falta ':'");
        compilerErrors.put(502, "Identificador de variable no válido");
        compilerErrors.put(503, "El identificador de variable usa una palabra reservada");
        compilerErrors.put(504, "Tipo de variable no válido");
        compilerErrors.put(505, "La declaración de variable deben ubicarse después de declarar correctamente program y uses y antes del begin");
        compilerErrors.put(506, "Identificador de la variable solo debe iniciar con una letra o guión bajo");
        compilerErrors.put(507, "Identificador de la variable solo debe contener letras o guiones bajo");

        // validateBeginEnd
        compilerErrors.put(600, "Se encontró más de un begin en el archivo");
        compilerErrors.put(601, "La palabra begin debe estar sola en la línea sin otros caracteres");
        compilerErrors.put(602, "Se encontró end antes de begin");
        compilerErrors.put(603, "El fin del programa debe ser 'end.' exactamente, con punto al final");
        compilerErrors.put(604, "No puede haber líneas ni comentarios después de end");
        compilerErrors.put(605, "No se encontró la palabra begin en el archivo");
        compilerErrors.put(606, "No se encontró 'end.' al final del archivo");

        // validateWrite
        compilerErrors.put(700, "La instrucción Write/WriteLn debe finalizar con punto y coma");
        compilerErrors.put(701, "La instrucción Write/WriteLn debe tener contenido dentro de paréntesis");
        compilerErrors.put(702, "Los paréntesis de Write/WriteLn no pueden estar vacíos");
        compilerErrors.put(703, "Contenido inválido en Write/WriteLn");

        // validateComments
        compilerErrors.put(800, "No se permiten comentarios antes de program");
        compilerErrors.put(801, "No se permiten comentarios después de end");
        compilerErrors.put(802, "Comentario inválido, debe empezar con // sin espacios");
        compilerErrors.put(803, "Comentarios entre { } deben cerrarse en la misma línea (no multilínea)");
        compilerErrors.put(804, "No se permiten comentarios después de un punto y coma en la misma línea");
        compilerErrors.put(805, "Comentarios con / deben tener 2 / en la forma //");
        compilerErrors.put(806, "No se permiten comentarios despues de end.");
    }

    public String getError(int code) {
        return compilerErrors.get(code);
    }

    public boolean hasError(int code) {
        return compilerErrors.containsKey(code);
    }
}