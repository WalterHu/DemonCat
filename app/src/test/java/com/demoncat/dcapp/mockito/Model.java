package com.demoncat.dcapp.mockito;

/**
 * @Class: Model
 * @Description: java类作用描述
 * @Author: hubohua
 * @CreateDate: 2018/8/15
 */
public class Model {
    public void executeAsyncMethod(final Callback callback) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(3000l);
                    if (callback != null) {
                        Person person = new Person();
                        person.setName("小花");
                        person.setAge(16);
                        callback.onSuccess(person);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
