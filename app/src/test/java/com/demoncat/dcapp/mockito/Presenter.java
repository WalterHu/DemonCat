package com.demoncat.dcapp.mockito;

/**
 * @Class: Presenter
 * @Description: java类作用描述
 * @Author: hubohua
 * @CreateDate: 2018/8/14
 */

/**
 * 作为业务流程测试
 */
public class Presenter {
    private Person mPerson;

    public Presenter(Person mPerson) {
        this.mPerson = mPerson;
    }

    public void initialize() {
        if (mPerson == null) {
            mPerson = new Person();
            mPerson.setAge(15);
            mPerson.setName("小花");
            mPerson.setSex(1);
        }
    }

    public void growup() {
        if (mPerson != null) {
            int age = mPerson.getAge();
            mPerson.setAge(age + 1);
        }
    }

    public void reduce() {
        if (mPerson != null) {
            int age = mPerson.getAge();
            mPerson.setAge(age - 1);
        }
    }
}
