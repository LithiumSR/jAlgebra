package helper;

import model.AlgebraElement;
import model.AlgebraNode;
import model.AlgebraValue;
import model.LiteralPart;
import model.enumeration.NodeType;

import java.util.*;
import java.util.stream.Collectors;

public class AlgebraHelper {

    /**
     * @param input List of AlgebraValue elements
     * @return AlgebraElement representing an expression tree where the input values are chained with PLUS operations
     */
    public static AlgebraElement convertListToPlusTree(List<AlgebraValue> input) {
        AlgebraNode elem = null;
        for (AlgebraValue k : input) {
            elem = new AlgebraNode(NodeType.PLUS, null, new AlgebraValue(k.getNum(), k.getLiteralPart().copy(), k.getDenom()), elem);
        }
        if (elem == null) return null;
        if (elem.getOperand2() == null) return elem.getOperand1();
        else return elem;
    }

    /**
     * @param element AlgebraElement representing an expression tree
     * @return AlgebraElement where denominators are replaced with a multiply operations with their reverse
     */
    public static AlgebraElement replaceDenominatorWithMultiply(AlgebraElement element) {
        if (element == null) return null;
        if (element instanceof AlgebraNode) {
            AlgebraNode node = (AlgebraNode) element;
            return new AlgebraNode(node.getType(), node.getFunc(), replaceDenominatorWithMultiply(node.getOperand1()), replaceDenominatorWithMultiply(node.getOperand2()));
        } else {
            AlgebraValue value = (AlgebraValue) element;
            if (value.getDenom() == null) return new AlgebraValue(value.getNum(), value.getLiteralPart().copy(), null);
            else
                return new AlgebraNode(NodeType.MULTIPLY, null, new AlgebraValue(value.getNum(), value.getLiteralPart().copy(), null), new AlgebraNode(NodeType.DIVIDE, null, new AlgebraValue(1, null), replaceDenominatorWithMultiply(value.getDenom())));
        }
    }

    /**
     * @param root AlgebraElement representing an expression tree
     * @return List of AlgebraValue found in the expression tree
     */
    public static List<AlgebraValue> getValues(AlgebraElement root) {
        if (root == null) return List.of();
        List<AlgebraValue> ret = new LinkedList<>();
        Queue<AlgebraElement> toCheck = new LinkedList<>();
        toCheck.add(root);
        while (!toCheck.isEmpty()) {
            AlgebraElement el = toCheck.poll();
            if (el == null) continue;
            if (el instanceof AlgebraValue) ret.add((AlgebraValue) el);
            else {
                AlgebraNode node = (AlgebraNode) el;
                toCheck.add(node.getOperand1());
                if (node.getOperand2() != null) toCheck.add(node.getOperand2());
            }
        }
        return ret;
    }

    /**
     * @param a Integer representing the first operand
     * @param b Integer representing the second operand
     * @return GCD between the two input operands
     */
    public static int gcd(int a, int b) {
        if (a == 0)
            return Math.abs(b);
        return Math.abs(gcd(b % a, a));
    }

    /**
     * @param list List of AlgebraValue
     * @return GCD across the elements of the input list
     */
    public static int findGCD(List<AlgebraValue> list) {
        if (list.isEmpty()) return 1;
        int result = 0;
        for (AlgebraValue element : list) {
            result = gcd(result, element.getNum());

            if (result == 1) {
                return 1;
            }
        }

        return result;
    }

    /**
     * @param values List of AlgebraValues
     * @return Map representing literal values that are common to every values. Literals are the key of the map and the values of those keys are the exponent
     */
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


    /**
     * @param lit         LiteralPart from which literals are going to be substracted from
     * @param subtractMap Map representing the literals (with their exponent) that are going to be removed
     * @return LiteralPart with the chosen literals removed
     */
    public static LiteralPart subtractLiteral(LiteralPart lit, Map<String, Integer> subtractMap) {
        Map<String, Integer> tmpMap = new HashMap<>(lit.getLiterals());
        subtractMap.keySet().forEach((it) -> {
            if (tmpMap.containsKey(it)) tmpMap.put(it, tmpMap.get(it) - subtractMap.get(it));
        });
        return new LiteralPart(tmpMap.entrySet().stream().filter(it -> it.getValue() != 0).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue)));

    }


}
