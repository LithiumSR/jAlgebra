package controller;

import core.ExpressionParser;
import core.ExpressionSolver;
import io.javalin.Javalin;
import io.javalin.http.Context;
import model.AlgebraElement;
import org.json.JSONException;
import org.json.JSONObject;

public class AlgebraController {

    public static void attach(Javalin javalinInstnace) {
        javalinInstnace.get("/algebra/solver", AlgebraController::get).servlet();
    }

    private static void get(Context ctx) {
        String body = ctx.body();
        try {
            JSONObject expression = new JSONObject(body);
            if (expression.isEmpty()) {
                ctx.json(new JSONObject());
                return;
            }
            AlgebraElement root = ExpressionParser.getAlgebraElement(expression);
            AlgebraElement solution = ExpressionSolver.solve(root);
            ctx.result("TODO");
        } catch (JSONException exception) {
            JSONObject obj = new JSONObject();
            obj.put("error", 400);
            obj.put("message", "Invalid JSON input file");
            ctx.result(obj.toString());
        }
    }
}