package compiladores1.Models;

/**
 *
 * @author jmong
 */
public class Symbol {
    private String name;
    private String type;
    private int lineDeclared;
    private boolean initialized;
    private boolean used;

    public Symbol(String name, String type, int lineDeclared) {
        this.name = name;
        this.type = type;
        this.lineDeclared = lineDeclared;
        this.initialized = false;
        this.used = false;
    }

    public String getName() { return name; }
    public String getType() { return type; }
    public int getLineDeclared() { return lineDeclared; }
    public boolean isInitialized() { return initialized; }
    public boolean isUsed() { return used; }

    public void setInitialized(boolean initialized) {
        this.initialized = initialized;
    }

    public void setUsed(boolean used) {
        this.used = used;
    }
}
