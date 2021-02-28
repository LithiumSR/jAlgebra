package controller;

import core.ExpressionParser;
import core.ExpressionSolver;
import exception.AlgebraParserException;
import io.javalin.Javalin;
import io.javalin.http.Context;
import model.AlgebraElement;
import org.json.JSONException;
import org.json.JSONObject;

public class AlgebraController {

    public static void attach(Javalin javalinInstnace) {
        javalinInstnace.post("/algebra/solver", AlgebraController::post).servlet();
    }

    private static void post(Context ctx) {
        String body = ctx.body();
        try {
            JSONObject expression = new JSONObject(body);
            if (expression.isEmpty()) {
                ctx.json(new JSONObject());
                return;
            }
            AlgebraElement root = ExpressionParser.getAlgebraElement(expression);
            AlgebraElement solution = ExpressionSolver.solve(root);
            if (solution == null) ctx.result("");
            else ctx.result(solution.toJson());
        } catch (JSONException| AlgebraParserException e) {
            JSONObject obj = new JSONObject();
            obj.put("exception", 400);
            obj.put("message", "Invalid JSON input file");
            ctx.result(obj.toString());
        } catch (ArithmeticException e) {
            JSONObject obj = new JSONObject();
            obj.put("exception", 400);
            obj.put("message", "Division by 0");
            ctx.result(obj.toString());
        } catch (Exception e) {
            JSONObject obj = new JSONObject();
            obj.put("exception", 500);
            obj.put("message", "Internal error");
            ctx.result(obj.toString());
        }
    }
}