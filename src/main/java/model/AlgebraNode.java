package model;

import model.enumeration.NodeName;
import model.enumeration.NodeType;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;


public class AlgebraNode extends AlgebraElement{
    private final NodeType type;
    private final NodeName func;
    private final AlgebraElement operand1;
    private final AlgebraElement operand2;

    public AlgebraNode(NodeType type, @Nullable NodeName func, AlgebraElement operand1, @Nullable AlgebraElement operand2) {
        this.operand1 = operand1;
        this.operand2 = operand2;
        this.type = type;
        this.func = func;
    }

    public NodeType getType() {
        return type;
    }

    public NodeName getFunc() {
        return func;
    }

    public AlgebraElement getOperand1() {
        return operand1;
    }

    public AlgebraElement getOperand2() {
        return operand2;
    }

    public boolean isUnaryOperation(){
        return type == NodeType.FUNCTION && Objects.requireNonNull(func).isUnary();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AlgebraNode that = (AlgebraNode) o;
        return type == that.type && func == that.func && Objects.equals(operand1, that.operand1) && Objects.equals(operand2, that.operand2);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, func, operand1, operand2);
    }

    @Override
    public String toString() {
        return "AlgebraNode{" +
                "type=" + type +
                ", func=" + func +
                ", operand1=" + operand1 +
                ", operand2=" + operand2 +
                '}';
    }
}