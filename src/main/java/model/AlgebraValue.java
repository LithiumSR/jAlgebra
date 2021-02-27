package model;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Objects;

public class AlgebraValue extends AlgebraElement {
    private Double num;
    private String text;
    private LiteralPart literal;

    public AlgebraValue(double num, LiteralPart literal) {
        this.num = num;
        this.literal = literal;
    }

    public AlgebraValue(int num, LiteralPart literal) {
        this.num = (double) num;
        this.literal = literal;
    }

    public AlgebraValue(String text) {
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
        return Objects.equals(num, that.num) && Objects.equals(text, that.text) && Objects.equals(literal, that.literal);
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

    @Override
    public String toJson() {
        JSONObject ret = new JSONObject();
        ret.put("value", (num == null) ? text : num);
        ret.put("literal", new JSONArray(literal.toJson()));
        return ret.toString();
    }

}
