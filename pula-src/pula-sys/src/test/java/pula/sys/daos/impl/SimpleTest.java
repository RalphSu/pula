package pula.sys.daos.impl;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import org.junit.Test;

public class SimpleTest {

    @Test
    public void test() {
        for (int i = 0; i < 60; i++) {
            Calendar now = Calendar.getInstance();
            now.add(Calendar.DATE, i);
            Date toDate = now.getTime();

            String key = String.format(Locale.US, "%4d-%02d-%02d", toDate.getYear() + 1900, toDate.getMonth() + 1,
                    toDate.getDay());
            System.out.println(key);
        }
    }

}
