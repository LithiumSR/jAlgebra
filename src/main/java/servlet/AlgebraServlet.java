package servlet;

import controller.AlgebraController;
import io.javalin.Javalin;
import io.javalin.http.JavalinServlet;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(urlPatterns = {"/algebra/solver"}, name = "AlgebraServlet")
public class AlgebraServlet extends HttpServlet {
    private final JavalinServlet javalinServlet;


    public AlgebraServlet() {
        Javalin javalin = Javalin.createStandalone(config -> {
            config.defaultContentType = "application/json";
        });
        javalinServlet = attachController(javalin).servlet();
    }


    private Javalin attachController(Javalin javalin){
        AlgebraController.attach(javalin);
        // I only have one controller :(

        return javalin;
    }


    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        javalinServlet.service(req,resp);
    }

}
