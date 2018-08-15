package test;

/**
 * @Class: TestOfInterfaceMethod
 * @Description: java类作用描述
 * @Author: hubohua
 * @CreateDate: 2018/8/15
 */

/**
 * 实际开发过程中，会出现如果修改一个接口增加方法，
 * 那么它的子类也会需要重新实现这个新增方法，这样很麻烦。
 * 但是在Java8中，允许在接口中定一个默认方法，
 * 让实现类可以不用实现该方法，类似于父类(抽象类)中的方法。
 */
public class TestOfInterfaceMethod implements InterfaceA {

    @Override
    public void a() {
        System.out.println();
    }

}
