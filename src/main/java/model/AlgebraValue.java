package model;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Objects;

public class AlgebraValue extends AlgebraElement {
    private Integer num;
    private String text;
    private LiteralPart literal;
    private AlgebraElement denom;

    public AlgebraValue(int num, LiteralPart literal) {
        this.num = num;
        this.literal = (literal == null) ? new LiteralPart() : literal;
    }


    public AlgebraValue(int num, LiteralPart literal, AlgebraElement denom) {
        this.num = num;
        this.literal = (literal == null) ? new LiteralPart() : literal;
        this.denom = denom;
    }

    public AlgebraValue(String text) {
        this.text = text;
    }

    public Integer getNum() {
        return num;
    }

    public String getText() {
        return text;
    }

    public LiteralPart getLiteralPart() {
        return literal;
    }

    public AlgebraElement getDenom() {
        return denom;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AlgebraValue that = (AlgebraValue) o;
        return Objects.equals(num, that.num) && Objects.equals(text, that.text) && Objects.equals(literal, that.literal) && Objects.equals(denom, that.denom);
    }

    @Override
    public int hashCode() {
        return Objects.hash(num, text, literal, denom);
    }

    @Override
    public String toString() {
        return "AlgebraValue{" +
                "num=" + num +
                ", text='" + text + '\'' +
                ", literal=" + literal +
                ", denom=" + denom +
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
