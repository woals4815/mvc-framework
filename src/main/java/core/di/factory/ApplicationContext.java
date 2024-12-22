package core.di.factory;

import java.util.Set;

public class ApplicationContext {
    private BeanFactory beanFactory;
    public ApplicationContext(Object... pbasePackages) {
        beanFactory = new BeanFactory();
        ClasspathBeanDefinitionScanner cs = new ClasspathBeanDefinitionScanner(beanFactory);
        cs.doScan(pbasePackages);
        beanFactory.initialize();
    }

    public <T> T getBean(Class<T> clazz) {
        return beanFactory.getBean(clazz);
    }

    public Set<Class<?>> getBeanClasses() {
        return beanFactory.getBeanClasses();
    }
}
