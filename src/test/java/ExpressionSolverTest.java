import core.ExpressionSolver;
import helper.AlgebraHelper;
import model.AlgebraElement;
import model.AlgebraNode;
import model.AlgebraValue;
import model.LiteralPart;
import model.enumeration.NodeName;
import model.enumeration.NodeType;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class ExpressionSolverTest {


    @Test
    public void testSolverAbsValue() {
        AlgebraElement ret = ExpressionSolver.solve(new AlgebraNode(NodeType.FUNCTION, NodeName.ABS,new AlgebraValue(-12.0,new LiteralPart(Map.of("a",3))), null));
        assertTrue(ret instanceof AlgebraValue && ((AlgebraValue) ret).getNum() == 12 && ((AlgebraValue) ret).getLiteralPart().getExponent("a") == 3);
    }

    @Test
    public void testSolverSizeOfValue() {
        AlgebraElement ret = ExpressionSolver.solve(new AlgebraNode(NodeType.FUNCTION, NodeName.SIZEOF,new AlgebraValue("ciao"), null));
        assertTrue(ret instanceof AlgebraValue && ((AlgebraValue) ret).getNum() == 4);
    }

    @Test
    public void testSolverMinusTransformation() {
        AlgebraElement ret = ExpressionSolver.solve(new AlgebraNode(NodeType.MINUS, null,new AlgebraValue(-12.0,new LiteralPart()), new AlgebraValue(3.0,new LiteralPart())));
        assertTrue(ret instanceof AlgebraValue
                && ((AlgebraValue) ret).getNum() == -15.0 && ((AlgebraValue) ret).getLiteralPart().getLiterals().keySet().isEmpty());
    }

    @Test
    public void testSolverMultiply() {
        AlgebraValue n1 = new AlgebraValue(-3.0,new LiteralPart());
        AlgebraValue n2 = new AlgebraValue(5.0,new LiteralPart());
        AlgebraNode n3 = new AlgebraNode(NodeType.PLUS,null,n1,n2);
        AlgebraValue n4 = new AlgebraValue(2.0,new LiteralPart());
        AlgebraValue n5 = new AlgebraValue(1.0,new LiteralPart());
        AlgebraNode n6 = new AlgebraNode(NodeType.PLUS,null,n4,n5);
        AlgebraNode n7 = new AlgebraNode(NodeType.MULTIPLY,null,n3,n6);
        var ret = (AlgebraValue) ExpressionSolver.solve(n7);
        assertTrue(ret.getNum() == 6.0 && ret.getLiteralPart().getLiterals().keySet().isEmpty());
    }

    @Test
    public void testSolverMultiplyLiterals() {
        var lit = new LiteralPart(Map.of("a",3));
        AlgebraValue n1 = new AlgebraValue(-3.0,lit.copy());
        AlgebraValue n2 = new AlgebraValue(5.0,lit.copy());
        AlgebraNode n3 = new AlgebraNode(NodeType.PLUS,null,n1,n2);
        AlgebraValue n4 = new AlgebraValue(2.0,lit.copy());
        AlgebraValue n5 = new AlgebraValue(1.0,lit.copy());
        AlgebraNode n6 = new AlgebraNode(NodeType.PLUS,null,n4,n5);
        AlgebraNode n7 = new AlgebraNode(NodeType.MULTIPLY,null,n3,n6);
        var ret = (AlgebraValue) ExpressionSolver.solve(n7);
        System.out.println(ret.toJson());
        assertTrue(ret.getNum() == 6.0 && ret.getLiteralPart().getLiterals().keySet().size() == 1 && ret.getLiteralPart().getLiterals().get("a") == 6);
    }

    @Test
    public void testSolverMultiplyNonMatchingLiterals() {
        var lit = new LiteralPart(Map.of("a",3));
        var lit2 = new LiteralPart(Map.of("c",1));
        AlgebraValue n1 = new AlgebraValue(-3.0,lit.copy());
        AlgebraValue n2 = new AlgebraValue(5.0,lit2.copy());
        AlgebraNode n3 = new AlgebraNode(NodeType.PLUS,null,n1,n2);
        AlgebraValue n4 = new AlgebraValue(2.0,lit.copy());
        AlgebraValue n5 = new AlgebraValue(1.0,lit2.copy());
        AlgebraNode n6 = new AlgebraNode(NodeType.PLUS,null,n4,n5);
        AlgebraNode n7 = new AlgebraNode(NodeType.MULTIPLY,null,n3,n6);
        var ret = AlgebraHelper.getValues(ExpressionSolver.solve(n7));
        assertTrue(ret.size() == 3 && ret.contains(new AlgebraValue(-6,new LiteralPart(Map.of("a",6)))
        )           && ret.contains(new AlgebraValue(7,new LiteralPart(Map.ofEntries(Map.entry("a",3), Map.entry("c",1)))))
                    && ret.contains(new AlgebraValue(5,new LiteralPart(Map.of("c",2)))));
    }

    @Test
    public void testSolverDivide() {
        AlgebraValue n1 = new AlgebraValue(-3.0,new LiteralPart());
        AlgebraValue n2 = new AlgebraValue(5.0,new LiteralPart());
        AlgebraNode n3 = new AlgebraNode(NodeType.PLUS,null,n1,n2);
        AlgebraValue n4 = new AlgebraValue(2.0,new LiteralPart());
        AlgebraValue n5 = new AlgebraValue(1.0,new LiteralPart());
        AlgebraNode n6 = new AlgebraNode(NodeType.PLUS,null,n4,n5);
        AlgebraNode n7 = new AlgebraNode(NodeType.DIVIDE,null,n3,n6);
        // TODO
    }

}
