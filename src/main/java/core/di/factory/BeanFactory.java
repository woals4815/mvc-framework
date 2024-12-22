package core.di.factory;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Maps;

import core.annotation.Controller;
import org.springframework.beans.BeanUtils;

public class BeanFactory implements BeanDefinitionRegistry {
    private static final Logger logger = LoggerFactory.getLogger(BeanFactory.class);

    private Set<Class<?>> preInstanticateBeans;

    private Map<Class<?>, Object> beans = Maps.newHashMap();

    private Map<Class<?>, BeanDefinition> beanDefinitions = Maps.newHashMap();

    private List<Injector> injectors;

    public BeanFactory() {}

    public BeanFactory(Set<Class<?>> preInstanticateBeans) {
        this.preInstanticateBeans = preInstanticateBeans;

        injectors = Arrays.asList(new FieldInjector(this), new SetterInjector(this), new ConstructorInjector(this));
    }

    public Set<Class<?>> getPreInstanticateBeans() {
        return preInstanticateBeans;
    }

    @Override
    public void registerBeanDefinition(Class<?> clazz, BeanDefinition beanDefinition) {
        beanDefinitions.put(clazz, beanDefinition);
    }

    @SuppressWarnings("unchecked")
    public <T> T getBean(Class<T> requiredType) {
        Object bean = beans.get(requiredType);
        if (bean != null) {
            return (T) bean;
        }
        Class<?> concreteClass = findConcreteClass(requiredType);
        BeanDefinition beanDefinition = beanDefinitions.get(concreteClass);
        bean = inject(beanDefinition);
        beans.put(requiredType, bean);
        return (T) bean;
    }

    public Set<Class<?>> getBeanClasses() {
        return beanDefinitions.keySet();
    }

    public void initialize() {
        for (Class<?> clazz : getBeanClasses()) {
            if (beans.get(clazz) == null) {
                getBean(clazz);
            }
        }
    }
    private Class<?> findConcreteClass(Class<?> clazz) {
        Set<Class<?>> beanClasses = getBeanClasses();
        Class<?> concreteClass = BeanFactoryUtils.findConcreteClass(clazz, beanClasses);
        if (!beanClasses.contains(concreteClass)) {
            throw new IllegalStateException(clazz + "빈이 아니다");
        }
        return concreteClass;
    }
    private Object inject(BeanDefinition beanDefinition) {
        if (beanDefinition.getResolvedInjectMode() == InjectType.INJECT_NO) {
            return BeanUtils.instantiate(beanDefinition.getBeanClass());
        } else if (beanDefinition.getResolvedInjectMode() == InjectType.INJECT_FIELD) {
            return injectFields(beanDefinition);
        } else {
            return injectConstructor(beanDefinition);
        }
    }
    private Object injectFields(BeanDefinition beanDefinition) {
        Object bean = BeanUtils.instantiate(beanDefinition.getBeanClass());
        Set<Field> injectFields = beanDefinition.getInjectedFields();
        for(Field field : injectFields) {
            injectField(bean, field);
        }
        return bean;
    }
    private void injectField(Object bean, Field field) {
        try {
            field.setAccessible(true);
            field.set(bean, BeanUtils.instantiate(field.getType()));
        }catch(IllegalAccessException | IllegalArgumentException e) {
            logger.error(e.getMessage());
        }
    }
    private Object injectConstructor(BeanDefinition beanDefinition) {
        Constructor<?> constructor = beanDefinition.getInjectedConstructor();
        List<Object> args = Lists.newArrayList();
        for (Class<?> clazz : constructor.getParameterTypes()) {
            args.add(clazz);
        }
        return BeanUtils.instantiateClass(constructor, args.toArray());
    }

    private void inject(Class<?> clazz) {
        for (Injector injector : injectors) {
            injector.inject(clazz);
        }
    }

    void registerBean(Class<?> clazz, Object bean) {
        beans.put(clazz, bean);
    }

    public Map<Class<?>, Object> getControllers() {
        Map<Class<?>, Object> controllers = Maps.newHashMap();
        for (Class<?> clazz : preInstanticateBeans) {
            Annotation annotation = clazz.getAnnotation(Controller.class);
            if (annotation != null) {
                controllers.put(clazz, beans.get(clazz));
            }
        }
        return controllers;
    }

    void clear() {
        preInstanticateBeans.clear();
        beans.clear();
    }


}
