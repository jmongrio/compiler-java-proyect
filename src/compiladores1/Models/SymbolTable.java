package compiladores1.Models;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author jmong
 */
public class SymbolTable {
    private final Map<String, Symbol> symbols = new HashMap<>();

    public void addSymbol(Symbol symbol) throws Exception {
        if (symbols.containsKey(symbol.getName())) {
            throw new Exception("Símbolo duplicado: " + symbol.getName());
        }
        symbols.put(symbol.getName(), symbol);
    }

    public Symbol getSymbol(String name) {
        return symbols.get(name);
    }

    public boolean exists(String name) {
        return symbols.containsKey(name);
    }

    public Map<String, Symbol> getAll() { return symbols; }
}