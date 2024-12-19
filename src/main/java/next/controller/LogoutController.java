package next.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/users/logout")
public class LogoutController implements Controller {
    private static final long serialVersionUID = 1L;

    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        HttpSession session = req.getSession();
        session.removeAttribute(UserSessionUtils.USER_SESSION_KEY);
        return "redirect:/";
    }
}
