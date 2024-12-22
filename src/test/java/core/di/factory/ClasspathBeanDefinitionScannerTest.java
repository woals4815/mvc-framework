package core.di.factory;

import java.util.Set;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ClasspathBeanDefinitionScannerTest {
    private Logger log = LoggerFactory.getLogger(ClasspathBeanDefinitionScannerTest.class);

    @Test
    public void scan() throws Exception {
        ClasspathBeanDefinitionScanner scanner = new ClasspathBeanDefinitionScanner("core.di.factory.example", "next.controller");
        Set<Class<?>> beans = scanner.scan();
        for (Class<?> clazz : beans) {
            log.debug("Bean : {}", clazz);
        }
    }
}
