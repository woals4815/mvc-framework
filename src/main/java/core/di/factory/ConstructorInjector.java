package core.di.factory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class ConstructorInjector extends AbstractInjector {
    private static final Logger log = LoggerFactory.getLogger(ConstructorInjector.class);

    public ConstructorInjector(BeanFactory beanFactory) {
        super(beanFactory);
    }

    @Override
    public void inject(Class<?> clazz) {
        if (this.beanFactory.getBean(clazz) == null) {
            instantiateClass(clazz);
        }
    }
}
