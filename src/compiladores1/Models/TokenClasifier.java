package compiladores1.Models;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author jmong
 */
public class TokenClasifier {
    private static final Set<String> PATTERNS = new HashSet<>(Arrays.asList(
    "RESERVED_WORD", "SEMI_COLON", "IDENTIFIER", "COLON"));
    
    
}
