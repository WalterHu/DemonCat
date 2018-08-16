package test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * @Class: TestOfStream
 * @Description: java类作用描述
 * @Author: hubohua
 * @CreateDate: 2018/8/16
 */
public class TestOfStream {
    public static void main(String[] args) {
        File file = new File("/Users/hubohua/Project/Code/DemonCat/app/src/main/java/test/TestOfStream");
        // 从文件中读取，使用字节流，单个字节读取
        FileInputStream fileInputStream = null;
        try {
            fileInputStream = new FileInputStream(file);
            int remain = fileInputStream.available();
            byte[] buffer = new byte[remain];
            StringBuilder stringBuilder = new StringBuilder(); // not thread-safe
            while (fileInputStream.available() > -1 && -1 != fileInputStream.read(buffer)) {
                stringBuilder.append(new String(buffer));
            }
            System.out.println("File byte read out: " + stringBuilder.toString() + stringBuilder.length());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fileInputStream != null) {
                try {
                    fileInputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        // 从文件中读取，使用字符流（适合文本），单个字符读取
        FileReader fileReader = null;
        try {
            fileReader = new FileReader(file);
            char[] chars = new char[1024];
            StringBuilder stringBuilder = new StringBuilder();
            while (-1 < fileReader.read(chars)) {
                stringBuilder.append(chars);
            }
            System.out.println("File char read out: " + stringBuilder.toString() + stringBuilder.length());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fileReader != null) {
                try {
                    fileReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        // 使用InputStreamReader来读取字符流
        InputStreamReader inputStreamReader = null;
        try {
            inputStreamReader = new InputStreamReader(new FileInputStream(file));
            char[] chars = new char[1024];
            StringBuilder stringBuilder = new StringBuilder();
            while (-1 < inputStreamReader.read(chars)) {
                stringBuilder.append(chars);
            }
            System.out.println("File char read out: " + stringBuilder.toString() + stringBuilder.length());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (inputStreamReader != null) {
                try {
                    inputStreamReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
