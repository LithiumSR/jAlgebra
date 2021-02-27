package core;

import exception.AlgebraParserError;
import model.AlgebraElement;
import model.AlgebraNode;
import model.AlgebraValue;
import model.LiteralPart;
import model.enumeration.NodeName;
import model.enumeration.NodeType;
import org.json.JSONArray;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class ExpressionParser {

    public static AlgebraElement getAlgebraElement(JSONObject object) throws AlgebraParserError {
        try {
            return _getAlgebraElement(object);
        } catch (ClassCastException e) {
            throw new AlgebraParserError("Invalid cast", e);
        }
    }
    private static AlgebraElement _getAlgebraElement(JSONObject object) throws AlgebraParserError {
        if (object.has("value") && object.has("literal")) {
            Map<String,Integer> literalMap = new HashMap<>();
            JSONArray arrayLiteral = object.getJSONArray("literal");
            for (int i = 0; i<arrayLiteral.length(); i++) {
                JSONObject literalObj = arrayLiteral.getJSONObject(i);
                if (literalMap.containsKey(literalObj.getString("value"))) throw new AlgebraParserError("Literals are not unique");
                literalMap.put(literalObj.getString("value"), literalObj.getInt("exponent"));
            }
            Object value = object.get("value");
            if (value instanceof String) return new AlgebraValue((String) value);
            else if (value instanceof Integer) return new AlgebraValue(Double.valueOf((Integer) value), new LiteralPart(literalMap));
            else if (value instanceof BigDecimal) return new AlgebraValue(((BigDecimal) value).doubleValue(), new LiteralPart(literalMap));
            else throw new ClassCastException("Invalid type for value");
        } else if (object.has("type")) {
            NodeType type = NodeType.valueOf(object.getString("type").toUpperCase());
            if (type == NodeType.FUNCTION) {
                if (!object.has("name") || !object.has("op1")) throw new AlgebraParserError("Missing op1 for unary function");
                NodeName name = NodeName.valueOf(object.getString("name").toUpperCase());
                AlgebraElement op1 = getAlgebraElement(object.getJSONObject("op1"));
                if (isFunctionCompatible(name,op1)) return new AlgebraNode(type, name, op1, null);
                else throw new ClassCastException("Invalid pair function-value");
            } else {
                if (!object.has("op2") || !object.has("op1")) throw new AlgebraParserError("Missing op1 or op2 for binary function");
                AlgebraElement op1 = getAlgebraElement(object.getJSONObject("op1"));
                AlgebraElement op2 = getAlgebraElement(object.getJSONObject("op2"));
                return new AlgebraNode(type, null, op1, op2);
            }
        } else throw new AlgebraParserError("Unknown algebra element");
    }

    private static boolean isFunctionCompatible(NodeName name, AlgebraElement element) {
        if (element instanceof AlgebraValue){
            switch (name) {
                case ABS:
                    return ((AlgebraValue) element).getNum()!=null;
                case SIZEOF:
                    return ((AlgebraValue) element).getText()!=null;
                default:
                    return false;
            }
        } else return false;
    }
}
