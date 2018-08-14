package com.demoncat.dcapp.mockito;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.MockitoRule;
import org.mockito.stubbing.Answer;

import static org.hamcrest.CoreMatchers.anything;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @Class: MockitoTest
 * @Description: java类作用描述
 * @Author: hubohua
 * @CreateDate: 2018/8/14
 */
@RunWith(MockitoJUnitRunner.class) // 运行器方法，使用Mockito运行器
public class MockitoTest {
    @Mock
    Person mPerson; // 使用@Mock注解的方式

    // 普通方法
//    @Test
//    public void testForMockito1() {
//        Person person = mock(Person.class);
//        assertNotNull(person);
//    }

    // 注解方法
//    @Before
//    public void setup() {
//        MockitoAnnotations.initMocks(this); // 注解方法初始化，结合@Before
//    }
//
//    @Test
//    public void testForMockito2() {
//        assertNotNull(mPerson);
//    }

    @Test
    public void testForTestRunner() {
        assertNotNull(mPerson);
    }

    @Test
    public void testForReturn() {
        // 这个两者的区别就是我们熟悉的while与do while。
        // 1.当执行什么方法时，然后就返回什么结果
        // when(mPerson.getName()).thenReturn("Xiaoming");
        // 2.以什么结果返回，当执行什么方法时,这类方法主要是为了应对无法使用thenReturn等方法的场景
        // （比如方法为void），可读性来说thenReturn这类更好。
        doReturn("Xiaoxiao").when(mPerson).getName();
        when(mPerson.getAge()).thenReturn(15);
        System.out.println("person: " + mPerson.getName());
        System.out.println("age: " + mPerson.getAge());
    }

    @Test
    public void testForAnswer() {
        // 我们用thenAnswer拿到了传入的参数，将返回结果重新进行处理并返回
        when(mPerson.setSex(anyInt())).thenAnswer(new Answer<Integer>() {
            @Override
            public Integer answer(InvocationOnMock invocation) throws Throwable {
                Object[] args = invocation.getArguments();
                if (args != null && args.length > 0) {
                    return (Integer) args[0];
                }
                return null;
            }
        });
        System.out.println(mPerson.setSex(12));
    }

    // MockitoRule方法
    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();
}
