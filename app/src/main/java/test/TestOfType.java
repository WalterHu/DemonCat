package test;

import android.app.Application;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * @Class: TestOfType
 * @Description: java类作用描述
 * @Author: hubohua
 * @CreateDate: 2018/8/1
 */
public class TestOfType {
    static class StrList {
        private List mList = new ArrayList();

        public void add(String object) {
            mList.add(object);
        }

        public String get(int index) {
            return (String) mList.get(index);
        }

        public int size() {
            return mList.size();
        }
    }

    static class Apple<T> {
        private T mInfo;
        public static int sCount = 1;

        public Apple(T info) {
            mInfo = info;
        }

        public T getInfo() {
            return mInfo;
        }

        public void setInfo(T info) {
            mInfo = info;
        }

        public void updateCount() {
            sCount ++;
        }

        public int getCount() {
            return sCount;
        }
    }

    // 使用Apple<T>来派生子类时，可以不传入实际类型参数
    static class A extends Apple {
        public A(Object info) {
            super(info);
        }

        @Override
        public Object getInfo() {
            return super.getInfo();
        }

        @Override
        public void setInfo(Object info) {
            super.setInfo(info);
        }
    }

    // 使用Apple<T>来派生子类时，传入实际类型参数String
//    class B extends Apple<String> {}
//
//    public static void main(String args[]) {
//        // 因为传递给T型参的类型是String实际类型，所以构造器的参数只能是String
//        Apple<String> stringApple = new Apple<>("苹果");
//        // 因为传递给T型参的类型是Integer实际类型，所以构造器的参数只能是Integer
//        Apple<Integer> integerApple = new Apple<>(15);
//    }

//    static class AppleB extends Apple<String> {
//
//    }
//
//    static class AppleC extends Apple<Integer> {
//
//    }

    public void updateList(List<Object> object) {
        for (int i = 0; i < object.size(); i ++) {
            System.out.println(object.get(i));
        }
    }

    public void updateList2(List<?> list) {
        for (int i = 0; i < list.size(); i ++) {
            System.out.println(list.get(i));
        }
    }

    public void updateList3(List<? extends Apple> list) {
        for (int i = 0; i < list.size(); i ++) {
            System.out.println("updateList3 " +  "i: " + list.get(i));
        }
    }

    public static void main(String args[]) {
        List<String> strList = new ArrayList<>();
        List<Integer> intList = new ArrayList<>();
        System.out.println(strList.getClass() == intList.getClass());

        Collection<?> strings = new ArrayList<String>();
        if (strings instanceof ArrayList) { // 代码将发生编译错误
            // TODO
        }
    }

//        Apple<String> appleA = new Apple<>();
//        Apple<Integer> appleB = new Apple<>();
//        System.out.println("apple: " + Apple.sCount);
//        appleA.updateCount();
//        System.out.println("appleA: " + appleA.getCount());
//        appleB.updateCount();
//        System.out.println("appleB: " + appleB.getCount());

//        List<String> strList = new ArrayList<>();
//        new TestOfType().updateList(strList); // 泛型会执行编译检查，这里List<Object>不能够等同于List<String>

        // 泛型类Apple在使用时声明String类型的类型参数
        // AppleB继承自Apple类，并实现泛型的类型参数为String
        // 下述代码，即遵循一般的Java的继承特性
        // AppleB是Apple的子类，可以赋值
//        Apple<String> apple = new AppleB();

        // 泛型类Apple在使用时声明String类型的类型参数
        // AppleC继承自Apple类，并实现泛型的类型参数为Integer
        // 下述代码，编译检查的时候无法通过，因为泛型类实现类型参数后
        // 类型参数之间不具备继承关系，必须严格遵循类型参数的实现匹配
//        Apple<String> apple1 = new AppleC();

        // 泛型类Apple在使用时声明Number类型的类型参数
        // AppleC继承自Apple类，并实现泛型的类型参数为Integer
        // 下述代码，编译检查的时候无法通过，因为泛型类实现类型参数后
        // 类型参数之间不具备继承关系，必须严格遵循类型参数的实现匹配
        // 即使Apple类型实现类型参数Number是AppleC类型参数Integer的父类
//        Apple<Number> apple2 = new AppleC();
//        Apple<Number> apple3 = new Apple<Integer>();

        // 数组类型之间存在继承关系，即Integer继承自Number，所以Integer[]可以赋值给Number[]
        // 但是数组类型声明的元素类型在存放于数组中时，必须匹配其数组类型
        // Number[]数组的运行时类型实际是Integer，无法接受Double类型元素；
        // 但是编译时由于识别是Number类型，所以可以接受Double元素
//        Integer[] integers = new Integer[5];
//        Number[] numbers = integers;
//        numbers[0] = 0.5; // 此处将在运行时报错，"java.lang.ArrayStoreException: java.lang.Double"

        // 数组类型的元素遵循继承关系，即声明的Number类型数组
        // 运行时也是Number类型，所以可以存放Double类型元素
//        Number[] numbers1 = new Number[5];
//        numbers1[0] = 0.5;
//        System.out.println("在Number[]数组中存放Double类型数据成功。");

        // ？表示通配符，表示它是所有泛型List的父类
        // 但是不能够直接添加元素，因为不清楚其实际的元素类型
        // 唯一的例外是null，它是所有引用类型的实例
//        List<?> list = new ArrayList<String>();
//        list.add("实例"); // 会引起编译错误
//        list.add(null); // 不会错误

        // updateList2方法的参数类型，是List<?>泛型类
        // 其接受所有类型参数的List泛型类或其子类
        // 参数类型会被认为是Object类型，不同于添加，访问是被允许的
        // 在使用的时候，这样做比较安全，运行不会报错
//        new TestOfType().updateList2(new ArrayList<String>()); // 不会报错

        // 会引起编译错误，因为apples列表使用的含有类型上限的APP内容，
        // 但是在使用apples时不确定具体类型，无法添加，同?类型
//        List<? extends Apple> apples = new ArrayList<>();
//        apples = new ArrayList<AppleB>();
//        apples.add(new AppleB());

//        new TestOfType().updateList3(new ArrayList<AppleB>()); // 编译不会报错，运行也成功
//    }
}