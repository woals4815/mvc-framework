package core.mvc;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import core.nmvc.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@WebServlet(name = "dispatcher", urlPatterns = "/", loadOnStartup = 1)
public class DispatcherServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final Logger logger = LoggerFactory.getLogger(DispatcherServlet.class);

    private List<HandlerMapping> handlerMappings = new ArrayList<HandlerMapping>();
    private AnnotationHandlerMapping am;
    private RequestMapping rm;
    private List<HandlerAdapter> adapters = new ArrayList<>();

    @Override
    public void init() throws ServletException {
        rm = new RequestMapping();
        am = new AnnotationHandlerMapping();
        rm.initMapping();
        am.initialize();

        handlerMappings.add(rm);
        handlerMappings.add(am);
        adapters.add(new HandlerExecutionHandlerAdapter());
        adapters.add(new ControllerHandlerAdapter());
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String requestUri = req.getRequestURI();
        logger.debug("Method : {}, Request URI : {}", req.getMethod(), requestUri);

        ModelAndView mav;
        Object handler = this.getHandler(req);
        try {
            mav = this.execute(handler, req, resp);
            View view = mav.getView();
            view.render(mav.getModel(), req, resp);
        } catch (Throwable e) {
            logger.error("Exception : {}", e);
            throw new ServletException(e.getMessage());
        }
    }
    private Object getHandler(HttpServletRequest req) {
        Object handler = null;
        for (HandlerMapping hm : handlerMappings) {
            handler = hm.getHandler(req);
        }
        return handler;
    }

    private ModelAndView execute(Object handler, HttpServletRequest req, HttpServletResponse resp) throws Exception {
        for (HandlerAdapter ha : adapters) {
            if (ha.supports(handler)) {
                return ha.handle(req, resp, handler);
            }
        }
        return null;
    }
}
