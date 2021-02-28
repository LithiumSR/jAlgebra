import core.ExpressionSolver;
import helper.AlgebraHelper;
import model.AlgebraElement;
import model.AlgebraNode;
import model.AlgebraValue;
import model.LiteralPart;
import model.enumeration.NodeName;
import model.enumeration.NodeType;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class ExpressionSolverTest {


    @Test
    public void testSolverAbsValue() {
        AlgebraElement ret = ExpressionSolver.solve(new AlgebraNode(NodeType.FUNCTION, NodeName.ABS, new AlgebraValue(-12, new LiteralPart(Map.of("a", 3))), null));
        assertTrue(ret instanceof AlgebraValue && ((AlgebraValue) ret).getNum() == 12 && ((AlgebraValue) ret).getLiteralPart().getExponent("a") == 3);
    }

    @Test
    public void testSolverSizeOfValue() {
        AlgebraElement ret = ExpressionSolver.solve(new AlgebraNode(NodeType.FUNCTION, NodeName.SIZEOF, new AlgebraValue("ciao"), null));
        assertTrue(ret instanceof AlgebraValue && ((AlgebraValue) ret).getNum() == 4);
    }

    @Test
    public void testSolverMinusTransformation() {
        AlgebraElement ret = ExpressionSolver.solve(new AlgebraNode(NodeType.MINUS, null, new AlgebraValue(-12, null), new AlgebraValue(3, null)));
        assertTrue(ret instanceof AlgebraValue
                && ((AlgebraValue) ret).getNum() == -15.0 && ((AlgebraValue) ret).getLiteralPart().getLiterals().keySet().isEmpty());
    }

    @Test
    public void testSolverMultiply() {
        AlgebraValue n1 = new AlgebraValue(-3, null);
        AlgebraValue n2 = new AlgebraValue(5, null);
        AlgebraNode n3 = new AlgebraNode(NodeType.PLUS, null, n1, n2);
        AlgebraValue n4 = new AlgebraValue(2, null);
        AlgebraValue n5 = new AlgebraValue(1, null);
        AlgebraNode n6 = new AlgebraNode(NodeType.PLUS, null, n4, n5);
        AlgebraNode n7 = new AlgebraNode(NodeType.MULTIPLY, null, n3, n6);
        var ret = (AlgebraValue) ExpressionSolver.solve(n7);
        assertTrue(ret.getNum() == 6.0 && ret.getLiteralPart().getLiterals().keySet().isEmpty());
    }

    @Test
    public void testSolverMultiplyLiterals() {
        var lit = new LiteralPart(Map.of("a", 3));
        AlgebraValue n1 = new AlgebraValue(-3, lit.copy());
        AlgebraValue n2 = new AlgebraValue(5, lit.copy());
        AlgebraNode n3 = new AlgebraNode(NodeType.PLUS, null, n1, n2);
        AlgebraValue n4 = new AlgebraValue(2, lit.copy());
        AlgebraValue n5 = new AlgebraValue(1, lit.copy());
        AlgebraNode n6 = new AlgebraNode(NodeType.PLUS, null, n4, n5);
        AlgebraNode n7 = new AlgebraNode(NodeType.MULTIPLY, null, n3, n6);
        var ret = (AlgebraValue) ExpressionSolver.solve(n7);
        assertTrue(ret.getNum() == 6.0 && ret.getLiteralPart().getLiterals().keySet().size() == 1 && ret.getLiteralPart().getLiterals().get("a") == 6);
    }

    @Test
    public void testSolverMultiplyNonMatchingLiterals() {
        var lit = new LiteralPart(Map.of("a", 3));
        var lit2 = new LiteralPart(Map.of("c", 1));
        AlgebraValue n1 = new AlgebraValue(-3, lit.copy());
        AlgebraValue n2 = new AlgebraValue(5, lit2.copy());
        AlgebraNode n3 = new AlgebraNode(NodeType.PLUS, null, n1, n2);
        AlgebraValue n4 = new AlgebraValue(2, lit.copy());
        AlgebraValue n5 = new AlgebraValue(1, lit2.copy());
        AlgebraNode n6 = new AlgebraNode(NodeType.PLUS, null, n4, n5);
        AlgebraNode n7 = new AlgebraNode(NodeType.MULTIPLY, null, n3, n6);
        var ret = AlgebraHelper.getValues(ExpressionSolver.solve(n7));
        assertTrue(ret.size() == 3 && ret.contains(new AlgebraValue(-6, new LiteralPart(Map.of("a", 6)))
        ) && ret.contains(new AlgebraValue(7, new LiteralPart(Map.ofEntries(Map.entry("a", 3), Map.entry("c", 1)))))
                && ret.contains(new AlgebraValue(5, new LiteralPart(Map.of("c", 2)))));
    }


    @Test
    public void testGCD() {
        var ret = AlgebraHelper.findGCD(List.of(new AlgebraValue(3, null), new AlgebraValue(-3, null), new AlgebraValue(5, null)));
        assertEquals(ret,1);
    }

    @Test
    public void testSolverDivide() {
        AlgebraValue n1 = new AlgebraValue(-3, null);
        AlgebraValue n2 = new AlgebraValue(5, null);
        AlgebraNode n3 = new AlgebraNode(NodeType.PLUS, null, n1, n2);
        AlgebraValue n4 = new AlgebraValue(2, null);
        AlgebraValue n5 = new AlgebraValue(1, null);
        AlgebraNode n6 = new AlgebraNode(NodeType.PLUS, null, n4, n5);
        AlgebraNode n7 = new AlgebraNode(NodeType.DIVIDE, null, n3, n6);
        assertEquals(ExpressionSolver.solve(n7), new AlgebraNode(NodeType.MULTIPLY, null, new AlgebraValue(2, null), new AlgebraNode(NodeType.DIVIDE, null, new AlgebraValue(1, null), new AlgebraValue(3, null))));
    }


    @Test
    public void getCommonLiterals() {
        var lit = new LiteralPart(Map.of("a", 3));
        var lit2 = new LiteralPart(Map.ofEntries(Map.entry("c", 1), Map.entry("a", 1)));
        AlgebraValue n1 = new AlgebraValue(-3, lit.copy());
        AlgebraValue n2 = new AlgebraValue(5, lit2.copy());
        assertEquals(AlgebraHelper.getCommonLiterals(List.of(n1, n2)), Map.of("a", 1));
    }


    @Test
    public void testDivideByZero() {
        AlgebraValue n1 = new AlgebraValue(2, null);
        AlgebraValue n2 = new AlgebraValue(5, null);
        AlgebraValue n3 = new AlgebraValue(5, null);
        assertThrows(ArithmeticException.class, () -> ExpressionSolver.solve(new AlgebraNode(NodeType.DIVIDE, null, n1, new AlgebraNode(NodeType.MINUS,null,n2,n3))));
    }

    @Test
    public void testDivideByZero2() {
        AlgebraValue n1 = new AlgebraValue(2, null);
        AlgebraValue n2 = new AlgebraValue(0, null);
        assertThrows(ArithmeticException.class, () -> ExpressionSolver.solve(new AlgebraNode(NodeType.DIVIDE, null, n1, n2)));
    }

    @Test
    public void testSolverDivideLiterals() {
        var lit = new LiteralPart(Map.of("a", 3));
        var lit2 = new LiteralPart(Map.ofEntries(Map.entry("c", 1), Map.entry("a", 1)));
        AlgebraValue n1 = new AlgebraValue(-3, lit.copy());
        AlgebraValue n2 = new AlgebraValue(5, lit2.copy());
        var ret = ExpressionSolver.solve(new AlgebraNode(NodeType.DIVIDE, null, n1, n2));
        assertEquals(ret, new AlgebraNode(NodeType.MULTIPLY, null, new AlgebraValue(-3, new LiteralPart(Map.of("a", 2))), new AlgebraNode(NodeType.DIVIDE, null, new AlgebraValue(1, null), new AlgebraValue(5, new LiteralPart(Map.of("c", 1))))));
    }

    @Test
    public void testDivideSimplification() {
        var lit = new LiteralPart(Map.of("a", 3));
        AlgebraValue n1 = new AlgebraValue(-3, null);
        AlgebraValue n2 = new AlgebraValue(5, null);
        var ret = ExpressionSolver.solve(new AlgebraNode(NodeType.DIVIDE, null, new AlgebraValue(5, lit), new AlgebraNode(NodeType.PLUS, null, n1, n2)));
        assertEquals(ret, new AlgebraNode(NodeType.MULTIPLY, null, new AlgebraValue(5, new LiteralPart(Map.of("a", 3))), new AlgebraNode(NodeType.DIVIDE, null, new AlgebraValue(1, null), new AlgebraValue(2, null))));
    }

    @Test
    public void testDivideNoSimplification() {
        var lit = new LiteralPart(Map.of("a", 3));
        var lit2 = new LiteralPart(Map.ofEntries(Map.entry("c", 1), Map.entry("a", 1)));
        var lit3 = new LiteralPart(Map.ofEntries(Map.entry("d", 1)));
        AlgebraValue n1 = new AlgebraValue(-3, lit.copy());
        AlgebraValue n2 = new AlgebraValue(7, lit2.copy());
        AlgebraValue n3 = new AlgebraValue(5, lit3.copy());
        assertEquals(ExpressionSolver.solve(new AlgebraNode(NodeType.DIVIDE, null, n1, new AlgebraNode(NodeType.PLUS, null, n2, n3))),
                new AlgebraNode(NodeType.MULTIPLY, null, new AlgebraValue(-3, lit.copy()), new AlgebraNode(NodeType.DIVIDE, null, new AlgebraValue(1, null),
                        new AlgebraNode(NodeType.PLUS, null, new AlgebraValue(7, lit2.copy()), new AlgebraNode(NodeType.PLUS, null, new AlgebraValue(5, lit3.copy()), null)))));
    }

}
