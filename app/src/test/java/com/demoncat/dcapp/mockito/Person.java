package com.demoncat.dcapp.mockito;

/**
 * @Class: Person
 * @Description: java类作用描述
 * @Author: hubohua
 * @CreateDate: 2018/8/14
 */
public class Person {
    private String name;
    private int age;
    private int sex;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getSex() {
        return sex;
    }

    public int setSex(int sex) {
        this.sex = sex;
        return this.sex;
    }
}
