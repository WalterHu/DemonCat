package com.demoncat.dcapp.mockito;

/**
 * @Class: MockitoTest7
 * @Description: java类作用描述
 * @Author: hubohua
 * @CreateDate: 2018/8/15
 */

import android.telecom.Call;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.stubbing.Answer;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.any;

/**
 * 测试桩验证
 */
public class MockitoTest7 {
    @Mock
    public Person person;
    @Mock
    public Model model; // 异步调用打桩类
    // 待测试类
    Presenter presenter;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        presenter = new Presenter(person, model);
    }

    @Test
    public void testForReturnValue() {
//        Mockito.when(person.getAge()).thenReturn(15, 16, 17);
        Mockito.when(person.getAge()).thenReturn(15).thenReturn(16).thenReturn(17);
        // 执行三次，顺序打印出15、16、17
        System.out.println(person.getAge());
        System.out.println(person.getAge());
        System.out.println(person.getAge());
    }

    @Test
    public void testForCallbackSync() {
        Assert.assertNotNull(presenter);
        Assert.assertNotNull(person);
        Assert.assertNotNull(model);
        // 打桩, 为mock对象model创建answer
        // when()后面接的方法必须都使用mock的matcher方法，比如anyString, any(Class)等
        Mockito.doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                Object[] args = invocation.getArguments();
                if (args != null && args.length > 0) {
                    if (args[0] != null && args[0] instanceof Callback) {
//                        Person person = new Person();
//                        person.setName("尼玛");
                        ((Callback) args[0]).onSuccess(person);
                    }
                }
                return null;
            }
        }).when(model).executeAsyncMethod(any(Callback.class)); // 匹配任何传入的Callback对象
        // 调用presenter的changeName方法, 其内会调用model的方法
        presenter.changeName("花花");
        // 验证状态与结果
        Mockito.verify(model,
                Mockito.times(1)).executeAsyncMethod(any(Callback.class));
        assertThat(presenter.getPersonName(), equalTo(person.getName()));
    }

    @Test
    public void testForCallbackAsync() {
        // 调用要触发的测试方法
        presenter.changeName("花花");
        // 构造桩数据
        final Person person = new Person();
        person.setName("王尼玛'");
        // 创建方法参数捕获器
        ArgumentCaptor<Callback> callbackArgumentCaptor =
                ArgumentCaptor.forClass(Callback.class);
        // 调用验证方法获取参数Callback
        Mockito.verify(model).executeAsyncMethod(callbackArgumentCaptor.capture());
        assertThat(presenter.getPersonName(), equalTo(null)); // 调用回调前的验证
        // 调用回调
        callbackArgumentCaptor.getValue().onSuccess(person);
        assertThat(presenter.getPersonName(), equalTo(person.getName())); // 调用回调前的验证
    }
}
