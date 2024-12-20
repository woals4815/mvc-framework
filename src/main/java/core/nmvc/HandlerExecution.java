package core.nmvc;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import core.mvc.Controller;
import core.mvc.ModelAndView;

import java.lang.reflect.Method;

public class HandlerExecution {
    private Method method;
    private Object controllerInstance;

    public HandlerExecution(
        Object controllerInstance,
        Method method
    ) {
        this.method = method;
        this.controllerInstance = controllerInstance;
    }
    public ModelAndView handle(HttpServletRequest request, HttpServletResponse response) throws Exception {
        return (ModelAndView) method.invoke(controllerInstance, request, response);
    }
}
