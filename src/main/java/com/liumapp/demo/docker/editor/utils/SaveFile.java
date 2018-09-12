package com.liumapp.demo.docker.editor.utils;

import java.io.*;

/**
 * Created by haoxy on 2018/9/11.
 * E-mail:hxyHelloWorld@163.com
 * github:https://github.com/haoxiaoyong1014
 */
public class SaveFile {
    private static String docPath = "/tmp/docker/doc";

    public static void savePic(InputStream inputStream, String fileName) {

        OutputStream os = null;
        try {
            byte[] bs = new byte[1024];
            int len;

            File tempFile = new File(docPath);
            if (!tempFile.exists()) {
                tempFile.mkdirs();
            }
            os = new FileOutputStream(tempFile.getPath() + File.separator + fileName);
            // 开始读取
            while ((len = inputStream.read(bs)) != -1) {
                os.write(bs, 0, len);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 完毕，关闭所有链接
            try {
                os.close();
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
