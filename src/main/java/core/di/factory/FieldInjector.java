package core.di.factory;

import com.google.common.collect.Lists;
import core.annotation.Inject;
import org.reflections.ReflectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Set;

public class FieldInjector implements Injector {
    private static final Logger log = LoggerFactory.getLogger(FieldInjector.class);
    private BeanFactory beanFactory;

    public FieldInjector(BeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }
    @Override
    public void inject(Class<?> clazz) {
        instantiateClass(clazz);

        Set<Field> fields = ReflectionUtils.getAllFields(clazz, ReflectionUtils.withAnnotation(Inject.class));
        for (Field field : fields) {
            this.instantiateClass(field.getType());
        }
    }

    private Object instantiateClass(Class<?> clazz) {
        Object bean = beanFactory.getBean(clazz);
        if (bean != null) {
            return bean;
        }

        Constructor<?> injectedConstructor = BeanFactoryUtils.getInjectedConstructor(clazz);
        if (injectedConstructor == null) {
            bean = BeanUtils.instantiate(clazz);
            beanFactory.addBean(clazz, bean);
            return bean;
        }

        bean = instantiateConstructor(injectedConstructor);
        beanFactory.addBean(clazz, bean);
        return bean;
    }
    private Object instantiateConstructor(Constructor<?> constructor) {
        Class<?>[] pTypes = constructor.getParameterTypes();
        List<Object> args = Lists.newArrayList();
        for (Class<?> clazz : pTypes) {
            Class<?> concreteClazz = BeanFactoryUtils.findConcreteClass(clazz, beanFactory.getPreInstanticateBeans());
            if (!beanFactory.getPreInstanticateBeans().contains(concreteClazz)) {
                throw new IllegalStateException(clazz + "는 Bean이 아니다.");
            }

            Object bean = beanFactory.getBean(concreteClazz);
            if (bean == null) {
                bean = instantiateClass(concreteClazz);
            }
            args.add(bean);
        }
        return BeanUtils.instantiateClass(constructor, args.toArray());
    }
}
