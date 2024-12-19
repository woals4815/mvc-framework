package next.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ForwardController implements Controller {
    private String filePath;
    public ForwardController(String filePath) {
        this.filePath = filePath;

    }
    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        return filePath;
    }

}
