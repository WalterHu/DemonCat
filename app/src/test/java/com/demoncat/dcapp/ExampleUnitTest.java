package com.demoncat.dcapp;

import com.demoncat.dcapp.utils.DateTimeUtils;

import org.junit.Test;

import java.text.ParseException;
import java.util.Date;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {

    @Test
    public void testForDateTime() throws Exception {
        String dateTime = DateTimeUtils.getDateTime(DateTimeUtils.DATE_DAY_FORMAT);
        assertEquals(dateTime, "2018-08-14");
    }

    @Test(expected = ParseException.class)
    public void testForDateTime2() throws Exception {
        // 使用的方法传入空参数，会引发ParseException，此时不通过
        // 若增加@Test后的注解参数"expected"，则会通过，表示期待ParseException异常作为结果
        Date dateTime = DateTimeUtils.string2Date("", DateTimeUtils.DATE_DAY_FORMAT);
        assertNotNull(dateTime);
    }
}