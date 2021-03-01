package model.enumeration;

public enum FunctionName {
    ABS, SIZEOF;

    public boolean isUnary() {
        return this == ABS || this == SIZEOF;
    }
}
