package helper;

import model.AlgebraElement;
import model.AlgebraNode;
import model.AlgebraValue;
import model.enumeration.NodeType;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class AlgebraHelper {

    public static AlgebraElement convertListToPlusTree(List<AlgebraValue> input) {
        AlgebraNode elem = null;
        for (AlgebraValue k : input) {
            elem = new AlgebraNode(NodeType.PLUS, null, new AlgebraValue(k.getNum(), k.getLiteralPart().copy()), elem);
        }
        return elem;
    }

    public static List<AlgebraValue> getValues(AlgebraElement op) {
        List<AlgebraValue> ret = new LinkedList<>();
        Queue<AlgebraElement> toDo = new LinkedList<>();
        toDo.add(op);
        while (!toDo.isEmpty()) {
            AlgebraElement el = toDo.poll();
            if (el instanceof AlgebraValue) ret.add((AlgebraValue) el);
            else {
                AlgebraNode node = (AlgebraNode) el;
                toDo.add(node.getOperand1());
                if (node.getOperand2() != null) toDo.add(node.getOperand2());
            }
        }
        return ret;
    }

}
