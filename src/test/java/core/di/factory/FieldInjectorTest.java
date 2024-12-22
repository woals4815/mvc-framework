package core.di.factory;

import core.di.factory.example.QnaController;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class FieldInjectorTest {
    private BeanScanner scanner;
    private BeanFactory factory;
    private FieldInjector fieldInjector;

    @Before
    public void setUp() {
        scanner = new BeanScanner("core.di.factory.example");
        factory = new BeanFactory(scanner.scan());
        fieldInjector = new FieldInjector(factory);
    }

    @Test
    public void inject() {
        fieldInjector.inject(QnaController.class);
    }
}