package helper;

import model.AlgebraElement;
import model.AlgebraNode;
import model.AlgebraValue;
import model.LiteralPart;
import model.enumeration.NodeType;

import java.util.*;
import java.util.stream.Collectors;

public class AlgebraHelper {

    public static AlgebraElement convertListToPlusTree(List<AlgebraValue> input) {
        AlgebraNode elem = null;
        for (AlgebraValue k : input) {
            elem = new AlgebraNode(NodeType.PLUS, null, new AlgebraValue(k.getNum(), k.getLiteralPart().copy(), k.getDenom()), elem);
        }
        if (elem == null) return null;
        if (elem.getOperand2() == null) return elem.getOperand1();
        else return elem;
    }

    public static AlgebraElement replaceDenominatorWithMultiply(AlgebraElement element) {
        if (element == null) return null;
        if (element instanceof AlgebraNode) {
            AlgebraNode node = (AlgebraNode) element;
            return new AlgebraNode(node.getType(), node.getFunc(), replaceDenominatorWithMultiply(node.getOperand1()), replaceDenominatorWithMultiply(node.getOperand2()));
        } else {
            AlgebraValue value = (AlgebraValue) element;
            if (value.getDenom() == null) return new AlgebraValue(value.getNum(), value.getLiteralPart().copy(), null);
            else return new AlgebraNode(NodeType.MULTIPLY, null, new AlgebraValue(value.getNum(), value.getLiteralPart().copy(), null), replaceDenominatorWithMultiply(value.getDenom()));
        }
    }

    public static List<AlgebraValue> getValues(AlgebraElement op) {
        if (op == null) return List.of();
        List<AlgebraValue> ret = new LinkedList<>();
        Queue<AlgebraElement> toCheck = new LinkedList<>();
        toCheck.add(op);
        while (!toCheck.isEmpty()) {
            AlgebraElement el = toCheck.poll();
            if (el == null) continue;
            if (el instanceof AlgebraValue) ret.add((AlgebraValue) el);
            else {
                AlgebraNode node = (AlgebraNode) el;
                System.out.println(node);
                toCheck.add(node.getOperand1());
                if (node.getOperand2() != null) toCheck.add(node.getOperand2());
            }
        }
        return ret;
    }

    public static int gcd(int a, int b)
    {
        if (a == 0)
            return Math.abs(b);
        return Math.abs(gcd(b % a, a));
    }

    public static int findGCD(List<AlgebraValue> list)
    {
        if (list.isEmpty()) return 1;
        int result = 0;
        for (AlgebraValue element: list){
            result = gcd(result, element.getNum());

            if(result == 1)
            {
                return 1;
            }
        }

        return result;
    }

    public static Map<String, Integer> getCommonLiterals(List<AlgebraValue> values) {
        var commonLit = values.stream().map(it -> new HashSet<>(it.getLiteralPart().getLiterals().keySet())).reduce((it1, it2) -> {
            it1.retainAll(it2);
            return it1;
        });
        var minExponent = new HashMap<String, Integer>();
        values.forEach(it -> it.getLiteralPart().getLiterals().forEach((key, value) -> {
            if (minExponent.containsKey(key)) minExponent.put(key, Math.min(minExponent.get(key), value));
            else minExponent.put(key, value);
        }));
        if (commonLit.isEmpty()) return new HashMap<>();
        return commonLit.get().stream().map(it -> Map.entry(it, minExponent.get(it))).collect(Collectors.toMap(Map.Entry::getKey,
                Map.Entry::getValue));
    }


    public static LiteralPart subtractLiteral(LiteralPart lit, Map<String, Integer> subtractMap) {
        Map<String, Integer> tmpMap = new HashMap<>(lit.getLiterals());
        subtractMap.keySet().forEach((it) -> {if (tmpMap.containsKey(it)) tmpMap.put(it, tmpMap.get(it) - subtractMap.get(it));});
        return new LiteralPart(tmpMap.entrySet().stream().filter(it -> it.getValue()!=0).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue)));

    }



}
