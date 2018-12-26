package com.bottle.log;

import org.junit.Test;

import java.text.SimpleDateFormat;
import java.util.Date;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {

    @Test
    public void testDateFormat() {
        Date now = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
        System.out.println(simpleDateFormat.format(now));
        assertEquals(4, 2 + 2);
    }

    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }

}