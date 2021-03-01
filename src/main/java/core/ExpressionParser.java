package core;

import exception.AlgebraParserException;
import model.AlgebraElement;
import model.AlgebraNode;
import model.AlgebraValue;
import model.LiteralPart;
import model.enumeration.FunctionName;
import model.enumeration.NodeType;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ExpressionParser {

    /**
     * @param object JSONObject representing the expression tree
     * @return AlgebraElement representing the root of the expression tree
     * @throws AlgebraParserException Thrown when input JSON is not following the correct schema
     */
    public static AlgebraElement getAlgebraElement(JSONObject object) throws AlgebraParserException {
        try {
            return auxGetAlgebraElement(object);
        } catch (ClassCastException e) {
            throw new AlgebraParserException("Invalid cast", e);
        }
    }

    private static AlgebraElement auxGetAlgebraElement(JSONObject object) throws AlgebraParserException {
        if (object.has("value") && object.has("literal")) {
            Map<String, Integer> literalMap = new HashMap<>();
            JSONArray arrayLiteral = object.getJSONArray("literal");
            for (int i = 0; i < arrayLiteral.length(); i++) {
                JSONObject literalObj = arrayLiteral.getJSONObject(i);
                if (literalMap.containsKey(literalObj.getString("value")))
                    throw new AlgebraParserException("Literals are not unique");
                literalMap.put(literalObj.getString("value"), literalObj.getInt("exponent"));
            }
            Object value = object.get("value");
            if (value instanceof String) return new AlgebraValue((String) value);
            else if (value instanceof Integer)
                return new AlgebraValue((Integer) value, new LiteralPart(literalMap));
            else throw new ClassCastException("Invalid type for value");
        } else if (object.has("type")) {
            NodeType type = NodeType.valueOf(object.getString("type").toUpperCase());
            if (type == NodeType.FUNCTION) {
                if (!object.has("name") || !object.has("op1"))
                    throw new AlgebraParserException("Missing op1 for unary function");
                FunctionName name = FunctionName.valueOf(object.getString("name").toUpperCase());
                AlgebraElement op1 = getAlgebraElement(object.getJSONObject("op1"));
                if (isFunctionCompatible(name, op1)) return new AlgebraNode(type, name, op1, null);
                else throw new ClassCastException("Invalid pair function-value");
            } else {
                if (!object.has("op2") || !object.has("op1"))
                    throw new AlgebraParserException("Missing op1 or op2 for binary function");
                AlgebraElement op1 = getAlgebraElement(object.getJSONObject("op1"));
                AlgebraElement op2 = getAlgebraElement(object.getJSONObject("op2"));
                return new AlgebraNode(type, null, op1, op2);
            }
        } else throw new AlgebraParserException("Unknown algebra element");
    }

    private static boolean isFunctionCompatible(FunctionName name, AlgebraElement element) {
        if (element instanceof AlgebraValue) {
            switch (name) {
                case ABS:
                    return ((AlgebraValue) element).getNum() != null;
                case SIZEOF:
                    return ((AlgebraValue) element).getText() != null;
                default:
                    return false;
            }
        } else return false;
    }
}
