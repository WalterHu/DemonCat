package test.singleton;

/**
 * @Class: TestOfSingleton7
 * @Description: java类作用描述
 * @Author: hubohua
 * @CreateDate: 2018/8/29
 */

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import rx.Single;

/**
 * 单例模式-序列化和反序列化
 * 单例模式虽然能保证线程安全，但在
 * 序列化和反序列化的情况下会出现生成多个对象的情况。
 * 下述代码提供了如何避免被序列化和反序列化产生的多
 * 对象情况。
 */
public class TestOfSingleton7 {

    public static void main(String[] args) {
        Singleton7 singleton7 = Singleton7.getInstance();
        System.out.println("singleton7 hash code: " + singleton7.hashCode());
        //序列化
        FileOutputStream fo = null;
        try {
            fo = new FileOutputStream("tem");
            ObjectOutputStream oo = new ObjectOutputStream(fo);
            oo.writeObject(singleton7);
            oo.close();
            fo.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //反序列化
        FileInputStream fi = null;
        try {
            fi = new FileInputStream("tem");
            ObjectInputStream oi = new ObjectInputStream(fi);
            Singleton7 serialize2 = (Singleton7) oi.readObject();
            oi.close();
            fi.close();
            System.out.println(serialize2.hashCode());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

}

//使用匿名内部类实现单例模式，在遇见序列化和反序列化的场景，得到的不是同一个实例
//解决这个问题是在序列化的时候使用readResolve方法，即去掉注释的部分
class Singleton7 implements Serializable {
    private static final long serialVersionUID = -1;

    private static class InnerClass {
        private static Singleton7 singleton7 = new Singleton7();
    }

    private Singleton7() {
        System.out.println("调用了构造方法");
    }

    public static Singleton7 getInstance() {
        return InnerClass.singleton7;
    }

//    protected Object readResolve() {
//        System.out.println("调用了readResolve方法");
//        return InnerClass.singleton7;
//    }

}
