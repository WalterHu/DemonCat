package test;

/**
 * @Class: TestOfMap
 * @Description: java类作用描述
 * @Author: hubohua
 * @CreateDate: 2018/8/15
 */

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

/**
 * Test of map in Java
 * HashMap, TreeMap, LinkedHashMap, HashTable
 */
public class TestOfMap {
    public static void main(String[] args) {
        // HashMap无顺序，非同步，速度快
        Map<String, String> hashMap = new HashMap<>();
        hashMap.put("3", "1");
        hashMap.put("2", "1");
        hashMap.put("1", "1");
        Set<String> keys = hashMap.keySet();
        Iterator<String> keyIterator = keys.iterator();
        // 结果为 49，50，51，分别对应三个key的hashCode
        // HashMap的无序是针对put进去的value来说的，但是
        // 实际排序是按照key的hashCode作为排序规则。
        while (keyIterator.hasNext()) {
            System.out.println("hash key: " + keyIterator.next().hashCode());
        }
        // TreeMap 不允许null，默认按照key升序，非同步
        Map<String, String> treeMap = new TreeMap<>();
        treeMap.put("3", "1");
        treeMap.put("2", "1");
        treeMap.put("1", "1");
        Set<String> treeKeys = treeMap.keySet();
        Iterator<String> treeKeyIterator = treeKeys.iterator();
        // 结果为 49，50，51，分别对应三个key的hashCode
        // TreeMap的无序是针对put进去的value来说的，但是
        // 实际排序是按照key的hashCode作为排序规则。
        while (treeKeyIterator.hasNext()) {
            System.out.println("tree key: " + treeKeyIterator.next().hashCode());
        }
        // LinkedMap 按照插入顺序排序，非同步
        Map<String, String> linkedMap = new LinkedHashMap<>();
        linkedMap.put("3", "1");
        linkedMap.put("2", "1");
        linkedMap.put("1", "1");
        Set<String> linkedKeys = linkedMap.keySet();
        Iterator<String> linkedKeyIterator = linkedKeys.iterator();
        // 结果为51, 50, 49
        // 顺序为插入的顺序
        while (linkedKeyIterator.hasNext()) {
            System.out.println("linked key: " + linkedKeyIterator.next().hashCode());
        }
        // HashTable 不许存放null数据，线程安全的HashMap
        Map<String, String> hashTable = new Hashtable<>();
        hashTable.put("2", "1");
        hashTable.put("3", "1");
        hashTable.put("1", "1");
        Set<String> tableKeys = hashTable.keySet();
        Iterator<String> tableIterator = tableKeys.iterator();
        // 结果顺序为51, 50, 49
        // 顺序为插入顺序
        while (tableIterator.hasNext()) {
            System.out.println("table key: " + tableIterator.next().hashCode());
        }
    }
}
