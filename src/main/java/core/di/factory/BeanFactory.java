package core.di.factory;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.common.collect.Lists;
import core.annotation.Controller;
import core.annotation.Inject;
import org.reflections.ReflectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Maps;

public class BeanFactory {
    private static final Logger logger = LoggerFactory.getLogger(BeanFactory.class);

    private Set<Class<?>> preInstanticateBeans;

    private Map<Class<?>, Object> beans = Maps.newHashMap();

    public BeanFactory(Set<Class<?>> preInstanticateBeans) {
        this.preInstanticateBeans = preInstanticateBeans;
    }

    @SuppressWarnings("unchecked")
    public <T> T getBean(Class<T> requiredType) {
        return (T) beans.get(requiredType);
    }

    public void initialize() {
        try {
            for(Class<?> clazz : preInstanticateBeans) {
                Class<?> concreteClazz = BeanFactoryUtils.findConcreteClass(clazz, preInstanticateBeans);
                if (beans.get(concreteClazz) != null) {
                    continue;
                }
                Object instance = instantiateClass(concreteClazz);
                beans.put(concreteClazz, instance);
            }
        } catch (Exception e) {
            throw new RuntimeException("Bean initialize failed");
        }
    }
    public Map<Class<?>, Object> getControllers() {
        Map<Class<?>, Object> controllers = Maps.newHashMap();
        for(Class<?> clazz : preInstanticateBeans) {
            if(clazz.isAnnotationPresent(Controller.class)) {
                controllers.put(clazz, getBean(clazz));
            }
        }
        return controllers;
    }
    private Object instantiateClass(Class<?> clazz) throws InstantiationException, IllegalAccessException, InvocationTargetException {
        Set<Constructor> constructors = ReflectionUtils.getAllConstructors(clazz, ReflectionUtils.withAnnotation(Inject.class));

        Class<?> concreteClazz = BeanFactoryUtils.findConcreteClass(clazz, preInstanticateBeans);

        if (constructors.isEmpty()) {
            return concreteClazz.newInstance();
        }
        Constructor constructor = constructors.iterator().next();
        return instantiateConstructor(constructor);
    }
    private Object instantiateConstructor(Constructor<?> constructor) throws InstantiationException, IllegalAccessException, InvocationTargetException {
        Class<?>[] parameters = constructor.getParameterTypes();
        List<Object> paramInstances = Lists.newArrayList();

        for(Class<?> param: parameters) {
            Object paramInstance = instantiateClass(param);
            paramInstances.add(paramInstance);
        }
        return constructor.newInstance(paramInstances.toArray());
    }
}
