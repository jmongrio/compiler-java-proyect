package compiladores1.Models;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author jmong
 */
public class ReservedWordDictionary {
    private static final Set<String> RESERVED_WORDS = new HashSet<>(Arrays.asList(
        "ABSOLUTE", "DOWNTO", "BEGIN", "DESTRUCTOR", "MOD",
        "AND", "ELSE", "CASE", "EXTERNAL", "NOT",
        "ARRAY", "END", "CONST", "DIV", "PACKED",
        "ASM", "FILE", "CONSTRUCTOR", "DO", "PROCEDURE",
        "FOR", "FORWARD", "FUNCTION", "GOTO", "RECORD",
        "IF", "IN", "OR", "PRIVATE", "UNTIL",
        "PROGRAM", "REPEAT", "STRING", "THEN", "VAR",
        "WHILE", "XOR", "WITH", "TYPE", "OF",
        "USES", "SET", "OBJECT", "TO"
    ));
    
    private static final Set<String> VARIABLE_TYPES = new HashSet<>(Arrays.asList(
    "INTEGER", "STRING", "WORD"));

    public static boolean isReserved(String word) {
        return RESERVED_WORDS.contains(word.toUpperCase());
    }
    
    public static boolean validVariableType(String word){
        return VARIABLE_TYPES.contains(word.toUpperCase());
    }
}