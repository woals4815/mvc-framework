package core.nmvc;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import com.google.common.collect.Maps;

import core.annotation.RequestMapping;
import core.annotation.RequestMethod;
import org.reflections.ReflectionUtils;

public class AnnotationHandlerMapping {
    private Object[] basePackage;

    private Map<HandlerKey, HandlerExecution> handlerExecutions = Maps.newHashMap();
    private ControllerScanner controllerScanner;

    public AnnotationHandlerMapping(Object... basePackage) {
        this.basePackage = basePackage;
        this.controllerScanner = new ControllerScanner(this.basePackage);
    }

    public void initialize() {
        Map<Class<?>, Object> controllers = controllerScanner.getControllers();
        Set<Map.Entry<Class<?>, Object>> entries = controllers.entrySet();
        for (Map.Entry<Class<?>, Object> entry : entries) {
            Class<?> clazz = entry.getKey();
            Object controllerInstance = entry.getValue();

            Set<Method> annotatedMethods = ReflectionUtils.getAllMethods(clazz, ReflectionUtils.withAnnotation(
                    RequestMapping.class
            ));

            annotatedMethods.stream().forEach((method) -> {
                HandlerKey handlerKey = createHandlerKey(method.getAnnotation(RequestMapping.class));
                this.handlerExecutions.put(handlerKey, new HandlerExecution(
                        controllerInstance,
                        method
                ));
            });
        }
    }

    public HandlerExecution getHandler(HttpServletRequest request) {
        String requestUri = request.getRequestURI();
        RequestMethod rm = RequestMethod.valueOf(request.getMethod().toUpperCase());
        return handlerExecutions.get(new HandlerKey(requestUri, rm));
    }

    private HandlerKey createHandlerKey(RequestMapping rm) {
        return new HandlerKey(rm.value(), rm.method());
    }
}
