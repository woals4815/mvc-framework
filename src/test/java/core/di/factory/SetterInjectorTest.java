package core.di.factory;

import core.di.factory.example.JdbcUserRepository;
import core.di.factory.example.MyQnaService;
import core.di.factory.example.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.Assert.*;

public class SetterInjectorTest {
    private static final Logger log = LoggerFactory.getLogger(SetterInjectorTest.class);
    private BeanScanner beanScanner;
    private BeanFactory beanFactory;
    private SetterInjector setterInjector;

    @Before
    public void setUp() {
        beanScanner = new BeanScanner("core.di.factory.example");
        beanFactory = new BeanFactory(beanScanner.scan());
        setterInjector = new SetterInjector(beanFactory);
    }

    @Test
    public void inject() {
        setterInjector.inject(MyQnaService.class);
        assertTrue(beanFactory.getBean(JdbcUserRepository.class) != null);
    }
}