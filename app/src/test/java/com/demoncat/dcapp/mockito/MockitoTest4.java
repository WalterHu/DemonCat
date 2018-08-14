package com.demoncat.dcapp.mockito;

/**
 * @Class: MockitoTest4
 * @Description: java类作用描述
 * @Author: hubohua
 * @CreateDate: 2018/8/14
 */

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.anyString;

/**
 * 验证void返回值的方法
 */
public class MockitoTest4 {

    @Mock
    public Case aCase;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testInitialize() {
        assertNotNull(aCase);
    }

    @Test(expected = RuntimeException.class)
    public void testForVoid() {
        // Case的setDesc方法为void返回值方法，doReturn依旧无法实现并报错
        // org.mockito.exceptions.misusing.CannotStubVoidMethodWithReturnValue:
        // 'setDesc' is a *void method* and it *cannot* be stubbed with a *return value*!
        //        Voids are usually stubbed with Throwables:
        // doThrow(exception).when(mock).someVoidMethod();
        // If you need to set the void method to do nothing you can use:
        // doNothing().when(mock).someVoidMethod();
//        Mockito.doReturn("我是一个case.").when(aCase).setDesc("我是一个Case.");
        aCase.setDesc("我是一个Case.");
        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        Mockito.verify(aCase).setDesc(captor.capture());
        assertThat(captor.getValue(), equalTo("我是一个Case."));
        Mockito.doThrow(new RuntimeException("Void方法验证")).when(aCase).setDesc(anyString());
        aCase.setDesc("我是一个Case.");
    }
}
