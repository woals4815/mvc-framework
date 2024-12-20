package core.ref;

import org.junit.Test;
import org.reflections.ReflectionUtils;

import java.lang.reflect.Method;
import java.util.Set;

public class Junit4TestRunner {
    @Test
    public void run() throws Exception {
        Class<Junit4Test> clazz = Junit4Test.class;
        Set<Method> methods = ReflectionUtils.getAllMethods(clazz, ReflectionUtils.withAnnotation(
                MyTest.class
        ));
        for (Method method : methods) {
            method.invoke(clazz.newInstance());
        }
    }
}
