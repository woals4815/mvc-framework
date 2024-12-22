package core.di.factory;

import com.google.common.collect.Sets;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Set;

public class BeanDefinition {
    private Class<?> beanClazz;
    private Constructor<?> injectedConstructor;
    private Set<Field> injectedFields;

    public BeanDefinition(Class<?> clazz) {
        this.beanClazz = clazz;
        injectedConstructor = getInjectedConstructor(clazz);
        injectedFields = getInjectedFields(clazz, injectedConstructor);
    }

    private static Constructor<?> getInjectedConstructor(Class<?> clazz) {
        return BeanFactoryUtils.getInjectedConstructor(clazz);
    }
    private Set<Field> getInjectedFields(Class<?> clazz, Constructor<?> constructor) {
        if(constructor != null) {
            return Sets.newHashSet();
        }
        Set<Field> injectedFields = Sets.newHashSet();
        Set<Class<?>> injectedProperties = getInjectPropertiesType(clazz);
        Field[] fields = clazz.getDeclaredFields();
        for(Field field : fields) {
            if(injectedProperties.contains(field.getType())) {
                injectedFields.add(field);
            }
        }
        return injectedFields;
    }

    private static Set<Class<?>> getInjectPropertiesType(Class<?> clazz) {
        Set<Class<?>> injectedProperties = Sets.newHashSet();
        Set<Method> injectMethod = BeanFactoryUtils.getInjectedMethods(clazz);
        for(Method method : injectMethod) {
            Class<?>[] parameterTypes = method.getParameterTypes();
            if (parameterTypes.length != 1) {
                throw new IllegalStateException("인자는 하나");
            }
            injectedProperties.add(parameterTypes[0]);
        }
        Set<Field> injectField = BeanFactoryUtils.getInjectedFields(clazz);
        for(Field field : injectField) {
            injectedProperties.add(field.getType());
        }
        return injectedProperties;
    }

    public Constructor<?> getInjectedConstructor() {
        return injectedConstructor;
    }
    public Set<Field> getInjectedFields() {
        return injectedFields;
    }
    public Class<?> getBeanClass() {
        return beanClazz;
    }
    public InjectType getResolvedInjectMode() {
        if (injectedConstructor != null) {
            return InjectType.INJECT_CONSTRUCTOR;
        }
        if (!injectedFields.isEmpty()) {
            return InjectType.INJECT_FIELD;
        }
        return InjectType.INJECT_NO;
    }
}
