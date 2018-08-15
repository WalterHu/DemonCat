package test;

/**
 * @Class: TestOfBreakAndContinue
 * @Description: java类作用描述
 * @Author: hubohua
 * @CreateDate: 2018/8/15
 */

/**
 * label提供了控制多层循环的方法，一般结合break和continue来使用。
 * 一般是在某个循环体前增加标号，在break或者continue后使用标号来控制循环。
 * 默认情况break只会停止其运行状态下的循环，但是如果使用label标号，则能够
 * 控制循环。
 */
public class TestOfBreakAndContinue {
    public static void main(String[] args) {
        other : for (int i = 0; i < 20; i ++) {
            for (int j = 0; j < 20; j ++) {
                System.out.println("i = " + i + ", j = " + j);
                if (i == 10) {
                    break other;
                }
            }
        }
    }
}
