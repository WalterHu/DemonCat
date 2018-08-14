package com.demoncat.dcapp.mockito;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.MockitoRule;
import org.mockito.stubbing.Answer;

import static org.mockito.Mockito.after;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.description;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

/**
 * @Class: MockitoTest2
 * @Description: java类作用描述
 * @Author: hubohua
 * @CreateDate: 2018/8/14
 */
@RunWith(MockitoJUnitRunner.class)
public class MockitoTest2 {
    @Mock
    public Person mPerson;

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Test
    public void testForsVerification() {
        when(mPerson.getAge()).thenReturn(18);
        // 延迟1s验证
        System.out.println(mPerson.getAge());
        System.out.println(System.currentTimeMillis());
        verify(mPerson, after(1000)).getAge();
        System.out.println(mPerson.getAge());
        System.out.println(System.currentTimeMillis());
        // 验证getAge()方法被至少调用过2次
        verify(mPerson, atLeast(2)).getAge();
    }

    @Test
    public void testForCall() {
        mPerson.getAge();
        verify(mPerson).getAge();
        System.out.println("testForCall1");
        verify(mPerson, times(0)).getName();
        System.out.println("testForCall2");
    }

    @Test
    public void testForCallAtLeast() {
        mPerson.getAge();
        mPerson.getAge();
        // 验证至少调用了3次，失败，因为此处只调用了2次
//        verify(mPerson, atLeast(3)).getAge();
    }

    @Test
    public void testForNoMoreCalling() {
        mPerson.getName();
        verify(mPerson).getName();
        mPerson.getAge(); // 此时在距离上次验证后又再次被调用，下面验证将会失败
        verifyNoMoreInteractions(mPerson); // 验证mock对象没有被再次调用
    }

//    @Test
//    public void testForTimeout() {
//        Mockito.doAnswer(new Answer() {
//            @Override
//            public Object answer(InvocationOnMock invocation) throws Throwable {
//                Object[] args = invocation.getArguments();
//
//                return null;
//            }
//        }).when(mPerson).getAge();
//    }

}
