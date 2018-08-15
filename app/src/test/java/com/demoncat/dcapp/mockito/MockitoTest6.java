package com.demoncat.dcapp.mockito;

/**
 * @Class: MockitoTest6
 * @Description: java类作用描述
 * @Author: hubohua
 * @CreateDate: 2018/8/15
 */

import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

/**
 * 验证冗余调用
 */
public class MockitoTest6 {
    @Mock
    public Person person;

    @Rule
    public MockitoRule rule = MockitoJUnit.rule();

    @Test
    public void testForReduplicate() {
        person.getAge();
        Mockito.verify(person).getAge();
//        Mockito.verify(person, Mockito.times(0)).getAge();
        person.getName(); // 调用了一次getName方法，导致person在上次被验证后又被调用过，下面验证会失败
        Mockito.verifyNoMoreInteractions(person);
    }
}
