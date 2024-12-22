package core.di.factory;

import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Field;
import java.util.Map;

import static org.junit.Assert.*;

public class ConstructorInjectorTest {

    private BeanScanner beanScanner;
    private BeanFactory beanFactory;

    @Before
    public void setUp() {
        beanScanner = new BeanScanner("core.di.factory.example");
        beanFactory = new BeanFactory(beanScanner.scan());
    }


    @Test
    public void inject() throws NoSuchFieldException, IllegalAccessException {
        beanFactory.initialize();
        Class<?> clazz = BeanFactory.class;
        Field field = clazz.getDeclaredField("beans");
        field.setAccessible(true);
        Map<Class<?>, Object> beans = (Map<Class<?>, Object>) field.get(beanFactory);
        assertEquals(4, beans.size());
    }
}