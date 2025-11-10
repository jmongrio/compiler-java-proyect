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
        compilerErrors.put(205, "No deben haber comentarios antes de la palabra inicial program"); // falta implementar

        // validateUses
        compilerErrors.put(300, "No debe haber líneas en blanco o comentarios entre program y uses");
        compilerErrors.put(301, "La sentencia uses debe finalizar con punto y coma");
        compilerErrors.put(302, "La sentencia uses debe incluir al menos un comando");
        compilerErrors.put(303, "No se encontró la sentencia uses después de program");
        compilerErrors.put(304, "Error en la sentencia program, no se puede validar correctamente la sentencia USES");

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
        compilerErrors.put(505,
                "La declaración de variable deben ubicarse después de declarar correctamente program y uses y antes del begin");
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
        compilerErrors.put(607, "No estan todos los begin o los end cerrados correctamente");

        // validateWrite
        compilerErrors.put(700, "La instrucción Write/WriteLn debe finalizar con punto y coma");
        compilerErrors.put(701, "La instrucción Write/WriteLn debe tener contenido dentro de paréntesis");
        compilerErrors.put(702, "Los paréntesis de Write/WriteLn no pueden estar vacíos");
        compilerErrors.put(703, "Contenido inválido en Write/WriteLn");
        compilerErrors.put(704, "La sentencia Write/WriteLn debe tener los simbolos paréntesis ( y )");
        compilerErrors.put(705, "La sentencia Write/WriteLn no debe estar antes de la sentencia program");
        compilerErrors.put(706, "La sentencia Write/WriteLn no debe estar despues de la sentencia end.");
        compilerErrors.put(707, "La sentencia Write/WriteLn debe estar entre la sentencia begin y la sentencia end.");

        // validateComments
        compilerErrors.put(800, "No se permiten comentarios antes de program");
        compilerErrors.put(801, "No se permiten comentarios después de end");
        compilerErrors.put(802, "Comentario inválido, debe empezar con // sin espacios");
        compilerErrors.put(803, "Comentarios entre { } deben cerrarse en la misma línea (no multilínea)");
        compilerErrors.put(804, "No se permiten comentarios después de un punto y coma en la misma línea");
        compilerErrors.put(805, "Comentarios con / deben tener 2 / en la forma //");
        compilerErrors.put(806, "No se permiten comentarios despues de end.");

        // validateForBlock
        compilerErrors.put(900, "No se permite declarar 'for' después de 'begin'");
        compilerErrors.put(901, "Falta identificador de variable después de 'for'");
        compilerErrors.put(902, "Variable usada en 'for' no fue declarada o su declaración es inválida");
        compilerErrors.put(903, "Falta o formato inválido del operador de asignación ':='");
        compilerErrors.put(904, "Falta el valor inicial en la declaración 'for'");
        compilerErrors.put(905, "Falta la palabra reservada 'to' después del valor inicial");
        compilerErrors.put(906, "Falta el valor final en la declaración 'for'");
        compilerErrors.put(907, "Falta la palabra reservada 'do' después del valor final");
        compilerErrors.put(908, "La palabra 'begin' debe aparecer en la siguiente línea con 4 espacios de tabulación");
        compilerErrors.put(909,
                "La sentencia dentro del for debe existir, empezar en la línea siguiente, llevar 4 espacios de tabulación y terminar con ';'");
        compilerErrors.put(910, "Falta 'end;' que cierre el for o 'end' no termina con punto y coma");
        compilerErrors.put(911, "La sentencia 'for' debe estar en una sola línea");
        compilerErrors.put(912, "No se permiten 'for' anidados");
        compilerErrors.put(913, "Formato inválido en la sentencia 'for'");

        // validateIfBlock
        compilerErrors.put(1000, "Falta la condición en la sentencia 'if'");
        compilerErrors.put(1001, "Falta la palabra reservada 'then' después de la condición");
        compilerErrors.put(1002, "Falta 'begin' después de 'then' en la siguiente línea (tabulación esperada)");
        compilerErrors.put(1003, "La sentencia dentro del bloque 'then' debe existir y terminar con ';'");
        compilerErrors.put(1004, "Falta 'end' que cierre el bloque 'then' o 'end' mal formado");
        compilerErrors.put(1005, "La palabra 'else' debe alinearse con el 'if' y venir en la posición correcta");
        compilerErrors.put(1006, "Falta 'begin' después de 'else' en la siguiente línea (tabulación esperada)");
        compilerErrors.put(1007, "La sentencia dentro del bloque 'else' debe existir y terminar con ';'");
        compilerErrors.put(1008, "Falta 'end' que cierre el bloque 'else' o 'end' mal formado");
        compilerErrors.put(1009, "No se permiten 'if' anidados");
        compilerErrors.put(1010, "Formato inválido en la sentencia 'if'");
        
    // validateRepeatBlock
    compilerErrors.put(1100, "La palabra 'repeat' no debe llevar ';' ni otros tokens en la misma línea");
    compilerErrors.put(1101, "Falta 'begin' después de 'repeat' en la siguiente línea");
    compilerErrors.put(1102, "La sentencia dentro del repeat debe existir y terminar con ';'");
    compilerErrors.put(1103, "Falta 'end;' que cierre el repeat o 'end' no termina con ';'");
    compilerErrors.put(1104, "Falta 'until' con condición o no finaliza con ';'");
    compilerErrors.put(1105, "Formato inválido en la sentencia 'repeat'");
    }

    public String getError(int code) {
        return compilerErrors.get(code);
    }

    public boolean hasError(int code) {
        return compilerErrors.containsKey(code);
    }
}