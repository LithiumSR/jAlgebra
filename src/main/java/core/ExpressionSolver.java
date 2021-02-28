package core;

import helper.AlgebraHelper;
import model.AlgebraElement;
import model.AlgebraNode;
import model.AlgebraValue;
import model.LiteralPart;
import model.enumeration.NodeType;

import java.util.*;
import java.util.stream.Collectors;

public class ExpressionSolver {

    public static AlgebraElement solve(AlgebraElement root) {
        AlgebraElement plusTree = computePlusTree(root);
        return mergePlusTree(plusTree);
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
        List<AlgebraValue> ret = new LinkedList<>();
        var sameLiteralsGroupedByDenom = sameLiterals.entrySet().stream().map(it -> Map.entry(it.getKey(), it.getValue().stream().collect(Collectors.toMap(AlgebraValue::getDenom, x -> {
            List<AlgebraValue> list = new ArrayList<>();
            list.add(x);
            return list;
        }, (left, right) -> {
            left.addAll(right);
            return left;
        }, HashMap::new)))).collect(Collectors.toMap(Map.Entry::getKey,
                Map.Entry::getValue));

        sameLiteralsGroupedByDenom.forEach((key, value) -> value.forEach((key2, value2) -> {
            int sum = value2.stream().mapToInt(AlgebraValue::getNum).sum();
            ret.add(new AlgebraValue(sum, key.copy(), key2));
        }));

        return AlgebraHelper.replaceDenominatorWithMultiply(AlgebraHelper.convertListToPlusTree(ret));
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
                return new AlgebraValue(op1.getText().length(), new LiteralPart());
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
                return applyTransformationMinus(op1, op2);
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
            AlgebraElement op1 = flipSign(elem.getOperand1());
            AlgebraElement op2 = flipSign(elem.getOperand2());
            return new AlgebraNode(NodeType.PLUS, elem.getFunc(), op1, op2);
        }
    }

    private static AlgebraElement applyTransformationMultiply(AlgebraElement op1, AlgebraElement op2) {
        if (op1 == null && op2 == null) return null;
        List<AlgebraValue> nodes1 = (op1 == null) ? List.of(new AlgebraValue(1,new LiteralPart())) : AlgebraHelper.getValues(op1);
        List<AlgebraValue> nodes2 = (op2 == null) ? List.of(new AlgebraValue(1,new LiteralPart())) : AlgebraHelper.getValues(op2);
        LinkedList<AlgebraValue> tmp = new LinkedList<>();
        for (AlgebraValue n : nodes1) {
            LiteralPart litn = n.getLiteralPart();
            for (AlgebraValue k : nodes2) {
                LiteralPart litk = k.getLiteralPart();
                Map<String, Integer> newLitMap = new HashMap<>(litn.getLiterals());
                litk.getLiterals().forEach((key, value) -> newLitMap.put(key, newLitMap.getOrDefault(key, 0) + value));
                tmp.add(new AlgebraValue((n.getNum() * k.getNum()), new LiteralPart(newLitMap), applyTransformationMultiply(n.getDenom(),k.getDenom())));
            }
        }
        return AlgebraHelper.convertListToPlusTree(tmp);
    }

    private static AlgebraElement applyTransformationDivide(AlgebraElement op1, AlgebraElement op2) {
        List<AlgebraValue> nodes1 = AlgebraHelper.getValues(op1);
        List<AlgebraValue> nodes2 = AlgebraHelper.getValues(op2);
        Map<String, Integer> commonOp2 = AlgebraHelper.getCommonLiterals(nodes2);
        int gcdOp2 = AlgebraHelper.findGCD(nodes2);
        LinkedList<AlgebraValue> tmp = new LinkedList<>();
        for (AlgebraValue n : nodes1) {
            LiteralPart litn = n.getLiteralPart();
            Map<String, Integer> adjustedCommonOp2 = commonOp2.entrySet().stream().filter(it -> litn.getLiterals().containsKey(it.getKey())).map(it -> Map.entry(it.getKey(), Math.min(litn.getExponent(it.getKey()), it.getValue()))).collect(Collectors.toMap(Map.Entry::getKey,
                    Map.Entry::getValue));
            LiteralPart newLiteral = AlgebraHelper.subtractLiteral(litn, adjustedCommonOp2);
            int gcd = AlgebraHelper.gcd(n.getNum(), gcdOp2);

            List<AlgebraValue> newDenominator = nodes2.stream().map((it) -> applyTransformationDivide(new AlgebraValue(it.getNum() / gcd, AlgebraHelper.subtractLiteral(it.getLiteralPart(), adjustedCommonOp2)), it.getDenom())).map(AlgebraHelper::getValues).flatMap(List::stream).collect(Collectors.toList());
            tmp.add(new AlgebraValue(n.getNum() / gcd, newLiteral, AlgebraHelper.convertListToPlusTree(newDenominator)));
        }
        return AlgebraHelper.convertListToPlusTree(tmp);
    }



}
