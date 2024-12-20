package core.mvc;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class JspView implements View {
    private String viewName;
    public JspView(String viewName) {
        this.viewName = viewName;
    }

    @Override
    public void render(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        if (viewName.startsWith("redirect")) {
            String[] tokens = viewName.split(":");
            resp.sendRedirect(tokens[1]);
            return;
        }

        RequestDispatcher rd = req.getRequestDispatcher(viewName);
        rd.forward(req, resp);

    }
}
