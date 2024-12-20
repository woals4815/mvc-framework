package core.nmvc;


import core.annotation.Controller;
import org.reflections.ReflectionUtils;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class ControllerScanner {
    private static final Logger log = LoggerFactory.getLogger(ControllerScanner.class);
    private Map<Class<?>, Object> controllers = new HashMap<>();
    private Object[] basePackages;

    public ControllerScanner(Object... basePackages) {
        this.basePackages = basePackages;
        try {
            Reflections reflections = new Reflections(this.basePackages);
            Set<Class<?>> clazzes = reflections.getTypesAnnotatedWith(Controller.class);

            for (Class<?> clazz : clazzes) {
                controllers.put(clazz, clazz.newInstance());
            }
        } catch(Exception e) {
            log.error(e.getMessage() + "Controller scanner failed", e);
        }
    }

    public Map<Class<?>, Object> getControllers() {
        return controllers;
    }
}
