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
public class Presenter extends Callback {
    private Person mPerson;
    private Model mModel;

    public Presenter(Person mPerson) {
        this.mPerson = mPerson;
    }

    public Presenter(Person person, Model model) {
        this.mPerson = person;
        this.mModel = model;
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

    public void changeName(String name) {
        if (mModel != null) {
            mModel.executeAsyncMethod(this);
        }
    }

    public String getPersonName() {
        return mPerson == null ? null : mPerson.getName();
    }

    @Override
    public void onSuccess(Person person) {
        System.out.println("onSuccess person: " + person.getName());
        mPerson = person;
    }

    @Override
    public void onFailure() {
        super.onFailure();
    }
}
