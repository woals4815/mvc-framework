package core.nmvc;

import org.junit.Test;

import static org.junit.Assert.*;

public class ControllerScannerTest {

    @Test
    public void getControllers() {
        ControllerScanner scanner = new ControllerScanner("core.nmvc");
        assertEquals(1, scanner.getControllers().size());
    }
}