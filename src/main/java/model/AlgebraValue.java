package model;

import java.util.Objects;

public class AlgebraValue extends AlgebraElement {
    private double num;
    private String text;
    private LiteralPart literal;
    public AlgebraValue (double num, LiteralPart literal) {
        this.num = num;
        this.literal = literal;
    }

    public AlgebraValue (int num, LiteralPart literal) {
        this.num = num;
        this.literal = literal;
    }

    public AlgebraValue (String text) {
        this.text = text;
    }

    public Double getNum() {
        return num;
    }

    public String getText() {
        return text;
    }

    public LiteralPart getLiteralPart() {
        return literal;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AlgebraValue that = (AlgebraValue) o;

        if (num != that.num) return false;
        return Objects.equals(literal, that.literal);
    }

    @Override
    public int hashCode() {
        return Objects.hash(num, text, literal);
    }

    @Override
    public String toString() {
        return "AlgebraValue{" +
                "num=" + num +
                ", text='" + text + '\'' +
                ", literal=" + literal +
                '}';
    }
}
