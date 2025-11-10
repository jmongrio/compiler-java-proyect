package compiladores1.Models;

public class IdentificationModel {
    private Token identifier;
    private Token type;
    private boolean isConstant;
    
    public Token getIdentifier() {
        return identifier;
    }
    public void setIdentifier(Token identifier) {
        this.identifier = identifier;
    }

    public Token getType() {
        return type;
    }
    public void setType(Token type) {
        this.type = type;
    }

    public boolean isConstant() {
        return isConstant;
    }
    public void setConstant(boolean isConstant) {
        this.isConstant = isConstant;
    }
}