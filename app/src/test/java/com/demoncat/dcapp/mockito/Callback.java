package com.demoncat.dcapp.mockito;

/**
 * @Class: Callback
 * @Description: java类作用描述
 * @Author: hubohua
 * @CreateDate: 2018/8/15
 */
public class Callback {
    public void onSuccess(Person person) {
        System.out.println("person: " + person.getName());
    }
    public void onFailure() {}
}
