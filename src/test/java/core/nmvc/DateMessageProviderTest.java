package core.nmvc;

import org.junit.Test;

import java.util.Calendar;

import static org.junit.Assert.*;

public class DateMessageProviderTest {

    @Test
    public void getDateMessage() {
        Calendar now = Calendar.getInstance();
        now.set(Calendar.HOUR_OF_DAY, 13);

        DateMessageProvider dp = new DateMessageProvider();
        assertEquals("오후", dp.getDateMessage(now));
    }

    @Test
    public void getDateMessage오전() {
        Calendar now = Calendar.getInstance();
        now.set(Calendar.HOUR_OF_DAY, 11);
        DateMessageProvider dp = new DateMessageProvider();
        assertEquals("오전", dp.getDateMessage(now));
    }
}