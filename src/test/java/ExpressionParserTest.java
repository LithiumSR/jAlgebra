import error.AlgebraParserError;
import model.AlgebraNode;
import model.AlgebraValue;
import model.LiteralPart;
import model.enumeration.NodeName;
import model.enumeration.NodeType;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import core.ExpressionParser;

import static org.junit.jupiter.api.Assertions.*;

public class ExpressionParserTest {


    @Test
    public void testNodeNumeric() {
        AlgebraValue ret = (AlgebraValue) ExpressionParser.getAlgebraElement(
                new JSONObject("{\n" +
                "    \"value\": 12,\n" +
                "    \"literal\": []\n" +
                "}"));
        assertTrue(ret.getNum()!=null && ret.getText()==null);
    }

    @Test
    public void testNodeNumericWithLiterals() {
        AlgebraValue ret = (AlgebraValue) ExpressionParser.getAlgebraElement(
                new JSONObject("{\n" +
                        "    \"value\": 12,\n" +
                        "    \"literal\": [{value: \"a\", exponent: 3}]\n" +
                        "}"));
        assertTrue(ret.getNum()!=null && ret.getText()==null && ret.getLiteralPart().getExponent("a")==3);
    }

    @Test
    public void testNodeNumericWithLiteralsErrorRepeat() {
        assertThrows(AlgebraParserError.class, () -> ExpressionParser.getAlgebraElement(
                new JSONObject("{\n" +
                        "    \"value\": 12,\n" +
                        "    \"literal\": [{value: \"a\", exponent: 3}, {value: \"a\", exponent: 1}]\n" +
                        "}")));
    }



    @Test
    public void testNodeString() {
        AlgebraValue ret = (AlgebraValue) ExpressionParser.getAlgebraElement(
                new JSONObject("{\n" +
                        "    \"value\": \"hello\",\n" +
                        "    \"literal\": []\n" +
                        "}"));
        assertTrue(ret.getNum()==null && ret.getText()!=null);
    }

    @Test
    public void testValidityAbs() {
        AlgebraNode ret = (AlgebraNode) ExpressionParser.getAlgebraElement(
                new JSONObject("{\n" +
                        "   \"type\":\"function\",\n" +
                        "   \"name\":\"abs\",\n" +
                        "   \"op1\":{\n" +
                        "      \"value\":10,\n" +
                        "      \"literal\":[]" +
                        "   }\n" +
                        "}"));
        assertEquals(new AlgebraNode(NodeType.FUNCTION, NodeName.ABS, new AlgebraValue(10, null), null), ret);
    }

    @Test
    public void testValidityAbsError() {
        assertThrows(AlgebraParserError.class, () -> ExpressionParser.getAlgebraElement(
                new JSONObject("{\n" +
                        "   \"type\":\"function\",\n" +
                        "   \"name\":\"abs\",\n" +
                        "   \"op1\":{\n" +
                        "      \"value\":\"hello\",\n" +
                        "      \"literal\":[]" +
                        "   }\n" +
                        "}")));
    }



    @Test
    public void testValiditySizeOf() {
        AlgebraNode ret = (AlgebraNode) ExpressionParser.getAlgebraElement(
                new JSONObject("{\n" +
                        "   \"type\":\"function\",\n" +
                        "   \"name\":\"sizeof\",\n" +
                        "   \"op1\":{\n" +
                        "      \"value\":\"hello\",\n" +
                        "      \"literal\":[]" +
                        "   }\n" +
                        "}"));
        assertEquals(new AlgebraNode(NodeType.FUNCTION, NodeName.SIZEOF, new AlgebraValue("hello"), null), ret);
    }

    @Test
    public void testValiditySizeOfError() {
        assertThrows(AlgebraParserError.class, () -> ExpressionParser.getAlgebraElement(
                new JSONObject("{\n" +
                        "   \"type\":\"function\",\n" +
                        "   \"name\":\"sizeof\",\n" +
                        "   \"op1\":{\n" +
                        "      \"value\": 10,\n" +
                        "      \"literal\":[]" +
                        "   }\n" +
                        "}")));
    }
}
