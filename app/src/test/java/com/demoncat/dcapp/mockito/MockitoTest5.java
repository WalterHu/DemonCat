package com.demoncat.dcapp.mockito;

/**
 * @Class: MockitoTest5
 * @Description: java类作用描述
 * @Author: hubohua
 * @CreateDate: 2018/8/15
 */

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

/**
 * Test for executing order
 */
public class MockitoTest5 {
    @Mock
    public Person person;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testForOrder() {
        Assert.assertNotNull(person);
        // 为该mock对象创建Order对象
        InOrder inOrder = Mockito.inOrder(person);
        // 调用对应方法
        person.getAge();
        person.getName();
        // 验证调用顺序
        inOrder.verify(person).getAge();
        inOrder.verify(person).getName();
        // 验证多个mock对象的调用顺序
        Person p1 = Mockito.mock(Person.class);
        Person p2 = Mockito.mock(Person.class);
        // 调用对应方法
        p1.getAge();
        p2.getName();
        // 为这两个Mock对象创建inOrder对象
        InOrder inOrder2 = Mockito.inOrder(p1, p2);
        // 验证调用顺序
        inOrder2.verify(p1).getAge();
        inOrder2.verify(p2).getName();
    }
}
