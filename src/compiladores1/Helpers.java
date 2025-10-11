package compiladores1;

import java.util.Arrays;
import java.util.List;

public class Helpers {
    public static final String PASCAL_EXTENSION = "pas";

    // Lista de palabras reservadas que pide la rúbrica
    public static final List<String> RESERVED_WORDS = Arrays.asList(
        "ABSOLUTE", "DOWNTO", "BEGIN", "DESTRUCTOR", "MOD",
        "AND", "ELSE", "CASE", "EXTERNAL", "NOT",
        "ARRAY", "END", "CONST", "DIV", "PACKED",
        "ASM", "FILE", "CONSTRUCTOR", "DO", "PROCEDURE",
        "FOR", "FORWARD", "FUNCTION", "GOTO", "RECORD",
        "IF", "IN", "OR", "PRIVATE", "UNTIL",
        "PROGRAM", "REPEAT", "STRING", "THEN", "VAR",
        "WHILE", "XOR", "WITH", "TYPE", "OF",
        "USES", "SET", "OBJECT", "TO", "FOR"
    );

    public static boolean isReservedWord(String word) {
        return RESERVED_WORDS.contains(word.toUpperCase());
    }
}