package core.mvc;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;
import java.util.Set;

public class JspView implements View {
    private String viewName;

    public JspView(String viewName) {
        this.viewName = viewName;
    }

    @Override
    public void render(Map<String, ?> model, HttpServletRequest req, HttpServletResponse resp) throws Exception {
        if (viewName.startsWith("redirect")) {
            String[] tokens = viewName.split(":");
            resp.sendRedirect(tokens[1]);
            return;
        }
        Set<String> keys = model.keySet();

        for (String key : keys) {
            req.setAttribute(key, model.get(key));
        }

        RequestDispatcher rd = req.getRequestDispatcher(viewName);
        rd.forward(req, resp);
    }
}
