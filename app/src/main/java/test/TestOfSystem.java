package test;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * @Class: TestOfSystem
 * @Description: java类作用描述
 * @Author: hubohua
 * @CreateDate: 2018/8/15
 */
public class TestOfSystem {
    public static void main(String[] args) {
        // 通过System.getenv()方法可以获得当前运行系统的参数
        Map<String, String> maps = System.getenv();
        Set<Map.Entry<String, String>> entries = maps.entrySet();
        Iterator<Map.Entry<String, String>> iterator = entries.iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, String> entry = iterator.next();
            System.out.println(entry.getKey() + " : " + entry.getValue());
        }
    }
}
