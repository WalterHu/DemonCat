package test;

/**
 * @Class: TestOfClassLoad
 * @Description: java类作用描述
 * @Author: hubohua
 * @CreateDate: 2018/8/21
 */

import rx.Single;

/**
 * 测试JVM加载类的特性
 * Java程序运行需要使用某个类时，如果该类还没有加载到内存中，
 * 系统会通过加载、连接、初始化三个步骤来对该类进行初始化。
 * 1.加载
 * 当我们运行java.exe命令执行某个java程序时，由于java程序本身以.class字节码的形式存在，
 * 它不是一个可执行文件，所以需要JVM将类文件加载到内存中。
 *
 * 2.连接
 * 当类被加载后，系统就会位置创建一个对应的Class对象，接着进入连接阶段:
 * a.验证:检验被加载的类是否有正确的内部结构，并和其他类协调一致。
 * b.准备：负责为类的静态属性分配内存，并设置默认初始值。注意：必须是静态属性！因为此时并不存在实例对象，设置值也是默认值初始值，而不是人为给定的值。
 * c.解析：将类的二进制数据中的符号引用替换成直接引用。
 *
 * 3.初始化
 * JVM负责对类进行初始化，也就是对静态属性进行初始化。
 * java中对静态属性指定初始值的方式有两种：①声明静态属性时指定初始值；②使用静态初始化快为静态属性指定初始值。
 */
public class TestOfClassLoad {
    public static void main(String[] args) {
        Singleton singleton = Singleton.getSingleton();
        System.out.println(singleton.a + ", " + singleton.b);
    }
}

/**
 * 在连接过程中，会创建静态变量或属性，静态变量按照字节码顺序往下执行，
 * 所以如下代码会出现不同的效果，而静态变量赋值的内容是在初始化过程，
 * 如下代码会将b打印成0，因为singleton静态变量优先赋值，而b = 0是
 * 后一步赋值，则直接覆盖，若b在声明时不赋值，则不会影响。
 */
class Singleton {
    private static Singleton singleton = new Singleton();
    public static int a;
    public static int b = 0;

    private Singleton() {
        a = 1;
        b = 1;
    }

    public static Singleton getSingleton() {
        if (singleton == null) {
            singleton = new Singleton();
        }
        return singleton;
    }
}
