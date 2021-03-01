package model;

import model.enumeration.FunctionName;
import model.enumeration.NodeType;
import org.jetbrains.annotations.Nullable;
import org.json.JSONObject;

import java.util.Objects;


public class AlgebraNode extends AlgebraElement {
    private final NodeType type;
    private final FunctionName functionName;
    private final AlgebraElement operand1;
    private final AlgebraElement operand2;

    public AlgebraNode(NodeType type, @Nullable FunctionName functionName, AlgebraElement operand1, @Nullable AlgebraElement operand2) {
        this.operand1 = operand1;
        this.operand2 = operand2;
        this.type = type;
        this.functionName = functionName;
    }

    public NodeType getType() {
        return type;
    }

    public FunctionName getFunctionName() {
        return functionName;
    }

    public AlgebraElement getOperand1() {
        return operand1;
    }

    public AlgebraElement getOperand2() {
        return operand2;
    }

    public boolean isUnaryOperation() {
        return type == NodeType.FUNCTION && Objects.requireNonNull(functionName).isUnary();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AlgebraNode that = (AlgebraNode) o;
        return type == that.type && functionName == that.functionName && Objects.equals(operand1, that.operand1) && Objects.equals(operand2, that.operand2);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, functionName, operand1, operand2);
    }

    @Override
    public String toString() {
        return "AlgebraNode{" +
                "type=" + type +
                ", func=" + functionName +
                ", operand1=" + operand1 +
                ", operand2=" + operand2 +
                '}';
    }

    @Override
    public String toJson() {
        JSONObject ret = new JSONObject();
        ret.put("type", this.type.toString());
        if (functionName != null) ret.put("name", functionName);
        ret.put("op1", new JSONObject(operand1.toJson()));
        if (operand2 != null) ret.put("op2", new JSONObject(operand2.toJson()));
        return ret.toString();
    }
}