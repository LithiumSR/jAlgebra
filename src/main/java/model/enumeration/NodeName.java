package model.enumeration;

public enum NodeName {
    ABS, SIZEOF;

    public boolean isUnary() { return this == ABS || this == SIZEOF; }
}
