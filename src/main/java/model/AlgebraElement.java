package model;

public abstract class AlgebraElement implements JsonSerializable {
}

interface JsonSerializable {
    String toJson();
}
