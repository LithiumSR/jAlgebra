package core;

import helper.AlgebraHelper;
import model.AlgebraElement;
import model.AlgebraNode;
import model.AlgebraValue;
import model.LiteralPart;
import model.enumeration.NodeType;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ExpressionSolver {

    public static AlgebraElement solve(AlgebraElement root) {
        AlgebraElement plusTree = computePlusTree(root);
        AlgebraElement ret = mergePlusTree(plusTree);
        if (ret instanceof AlgebraNode && ((AlgebraNode) ret).getOperand2() == null)
            return ((AlgebraNode) ret).getOperand1();
        else return ret;
    }

    private static AlgebraElement mergePlusTree(AlgebraElement root) {
        HashMap<LiteralPart, List<AlgebraValue>> sameLiterals = new HashMap<>();
        List<AlgebraValue> values = AlgebraHelper.getValues(root);
        for (AlgebraValue value : values) {
            if (value.getNum() == 0) continue;
            sameLiterals.putIfAbsent(value.getLiteralPart(), new LinkedList<>());
            List<AlgebraValue> tmp = sameLiterals.get(value.getLiteralPart());
            tmp.add(value);
        }
        List<AlgebraValue> summed = sameLiterals.entrySet().stream().map(el -> {
            double newValue = el.getValue().stream().mapToDouble(AlgebraValue::getNum).sum();
            return new AlgebraValue(newValue, el.getKey().copy());
        }).collect(Collectors.toList());
        //System.out.println(summed);
        return AlgebraHelper.convertListToPlusTree(summed);
    }


    public static AlgebraElement computePlusTree(AlgebraElement root) {
        if (root instanceof AlgebraValue) {
            return root;
        }
        AlgebraNode node = (AlgebraNode) root;
        if (node.isUnaryOperation()) {
            return computeUnary(node);
        } else {
            AlgebraElement right = computePlusTree(node.getOperand2());
            AlgebraElement left = computePlusTree(node.getOperand1());
            return computeBinary(new AlgebraNode(node.getType(), node.getFunc(), left, right));
        }
    }

    private static AlgebraValue computeUnary(AlgebraNode node) {
        AlgebraValue op1 = ((AlgebraValue) node.getOperand1());
        switch (node.getFunc()) {
            case ABS:
                return new AlgebraValue(Math.abs(op1.getNum()), op1.getLiteralPart().copy());
            case SIZEOF:
                return new AlgebraValue((double) op1.getText().length(), new LiteralPart());
            default:
                return null;
        }
    }

    private static AlgebraElement computeBinary(AlgebraNode node) {
        AlgebraElement op1 = node.getOperand1();
        AlgebraElement op2 = node.getOperand2();
        switch (node.getType()) {
            case PLUS:
                return new AlgebraNode(node.getType(), node.getFunc(), op1, op2);
            case MINUS:
                return new AlgebraNode(NodeType.PLUS, null, op1, applyTransformationMinus(op2));
            case MULTIPLY:
                return applyTransformationMultiply(op1, op2);
            case DIVIDE:
                return applyTransformationDivide(op1, op2);
            default:
                return null;
        }
    }


    private static AlgebraElement applyTransformationMinus(AlgebraElement op) {
        if (op instanceof AlgebraValue) {
            var elem = (AlgebraValue) op;
            return new AlgebraValue(elem.getNum() * -1, elem.getLiteralPart().copy());
        } else {
            var elem = (AlgebraNode) op;
            AlgebraElement op1 = applyTransformationMinus(elem.getOperand1());
            AlgebraElement op2 = applyTransformationMinus(elem.getOperand2());
            return new AlgebraNode(NodeType.PLUS, elem.getFunc(), op1, op2);
        }
    }

    private static AlgebraElement applyTransformationMultiply(AlgebraElement op1, AlgebraElement op2) {
        List<AlgebraValue> nodes1 = AlgebraHelper.getValues(op1);
        List<AlgebraValue> nodes2 = AlgebraHelper.getValues(op2);
        LinkedList<AlgebraValue> tmp = new LinkedList<>();
        for (AlgebraValue n : nodes1) {
            LiteralPart litn = n.getLiteralPart();
            for (AlgebraValue k : nodes2) {
                LiteralPart litk = k.getLiteralPart();
                Map<String, Integer> newLitMap = new HashMap<>(litn.getLiterals());
                litk.getLiterals().forEach((key, value) -> newLitMap.put(key, newLitMap.getOrDefault(key, 0) + value));
                tmp.add(new AlgebraValue((n.getNum() * k.getNum()), new LiteralPart(newLitMap)));
            }
        }
        return AlgebraHelper.convertListToPlusTree(tmp);
    }

    private static AlgebraElement applyTransformationDivide(AlgebraElement op1, AlgebraElement op2) {
        List<AlgebraValue> nodes1 = AlgebraHelper.getValues(op1);
        List<AlgebraValue> nodes2 = AlgebraHelper.getValues(op2);

        LinkedList<AlgebraValue> tmp = new LinkedList<>();
        for (AlgebraValue n : nodes1) {
            LiteralPart litn = n.getLiteralPart();
            for (AlgebraValue k : nodes2) {
                LiteralPart litk = k.getLiteralPart();
                Map<String, Integer> newLitMap = new HashMap<>(litn.getLiterals());
                litk.getLiterals().forEach((key, value) -> newLitMap.put(key, newLitMap.getOrDefault(key, 0) - value));
                LinkedList<String> keysToRemove = new LinkedList<>();
                newLitMap.forEach((key, value) -> {
                    if (value == 0) keysToRemove.add(key);
                });
                keysToRemove.forEach(newLitMap::remove);
                tmp.add(new AlgebraValue(n.getNum() / k.getNum(), new LiteralPart(newLitMap)));
            }
        }
        return AlgebraHelper.convertListToPlusTree(tmp);
    }

}
