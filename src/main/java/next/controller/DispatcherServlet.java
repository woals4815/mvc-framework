package next.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(urlPatterns = "/", loadOnStartup = 0)
public class DispatcherServlet extends HttpServlet {

    private static final Logger log = LoggerFactory.getLogger(DispatcherServlet.class);

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp)  {
        try {
            Controller controller = RequestMapping.getController(req.getRequestURI());
            String result = controller.execute(req, resp);
            responseResult(result, req, resp);
        }catch(
               Exception e
        ) {
            log.error(e.getMessage(), e);
        }
    }

    private void responseResult(String result, HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        if (result.startsWith("redirect")) {
            String[] tokens = result.split(":");
            resp.sendRedirect(tokens[1]);
            return;
        }
        RequestDispatcher rd = req.getRequestDispatcher(result);
        rd.forward(req, resp);
    }
}
