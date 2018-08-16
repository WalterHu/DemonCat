package com.demoncat.dcapp.mockito;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import static org.hamcrest.CoreMatchers.is;

/**
 * @Class: MockitoTest3
 * @Description: java类作用描述
 * @Author: hubohua
 * @CreateDate: 2018/8/14
 */

/**
 * Mockito 对于final类、匿名类和Java的基本类型是无法进行mock的。
 * 此处我们想要验证Presenter在调用Person对象的方法是是否正常。
 * Mock对象为Person类对象。
 */
public class MockitoTest3 {
    @Mock
    public Person mPerson; // mock对象为Person类对象
    private Presenter mPresenter;

    // 注解方式实现
    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        mPresenter = new Presenter(mPerson); // 初始化Presenter对象
    }

    @Test
    public void testForGrowup() {
        int age = mPerson.getAge();
        System.out.println(age);
//        Mockito.when(mPerson.getAge()).thenReturn(18);
        mPresenter.growup();
        ArgumentCaptor<Integer> captor = ArgumentCaptor.forClass(Integer.class); // Person的setAge参数类型是int
        Mockito.verify(mPerson).setAge(captor.capture()); // 验证Person的setAge()方法调用次数
        Assert.assertThat(captor.getValue(), is(1)); // 验证传入Person方法setAge的参数是1
    }

    @Test(expected = RuntimeException.class)
    public void testForThrow() {
        Mockito.doThrow(new RuntimeException("异常出现了1")).when(mPerson).getAge();
        mPerson.getAge();
        Mockito.when(mPerson.getAge()).thenThrow(new RuntimeException("异常出现了2"));
        mPerson.getAge();
    }
}
