package core.di.factory;

import com.google.common.collect.Lists;
import core.annotation.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.List;

public class SetterInjector extends AbstractInjector {
    private static final Logger log = LoggerFactory.getLogger(SetterInjector.class);

    public SetterInjector(BeanFactory beanFactory) {
        super(beanFactory);
    }

    @Override
    public void inject(Class<?> clazz) {
        instantiateClass(clazz);
        Method[] methods = clazz.getDeclaredMethods();
        for (Method method : methods) {
            if (!method.isAnnotationPresent(Inject.class)) {
                continue;
            }
            Class<?>[] params = method.getParameterTypes();
            if (params.length != 1) {
                throw new IllegalStateException("require one parameter.");
            }
            Class<?> concreteClazz = BeanFactoryUtils.findConcreteClass(params[0], beanFactory.getPreInstanticateBeans());
            Object bean = beanFactory.getBean(concreteClazz);
            if (bean == null) {
                bean = instantiateClass(concreteClazz);
            }
            try {
                method.invoke(beanFactory.getBean(
                        method.getDeclaringClass()
                ), bean);
            } catch (Exception e) {
                log.error(e.getMessage());
            }
        }
    }
}
