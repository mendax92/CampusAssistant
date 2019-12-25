package com.zhiqi.campusassistant.common.utils;

import com.zhiqi.campusassistant.app.AssistantApplication;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * 文件工具类
 */
public class FileUtil {
    /**
     * @params 可以用于 本地存储json 文件  模拟网络请求
     * @time 2017/11/10
     */
    public static String getJson(String fileName) {
       /*获取到assets文件下的TExt.json文件的数据，并以输出流形式返回。*/
        InputStream is = AssistantApplication.getInstance().getClass().getClassLoader().getResourceAsStream("assets/" + fileName);
        InputStreamReader streamReader = new InputStreamReader(is);
        BufferedReader reader = new BufferedReader(streamReader);
        String line;
        StringBuilder stringBuilder = new StringBuilder();
        try {
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line);
            }
            reader.close();
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stringBuilder.toString();
    }
}
