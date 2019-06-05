package com.liumapp.demo.docker.editor.controller;

import com.liumapp.demo.docker.editor.entity.Editor;
import com.liumapp.demo.docker.editor.utils.PdfService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;

/**
 * Created by Haoxy on 2019-06-05.
 * E-mail:hxyHelloWorld@163.com
 * github:https://github.com/haoxiaoyong1014
 */
@RestController
@RequestMapping(value = "itext/html")
public class ItextHtmlToPdfController {

    @RequestMapping(value = "pdf")
    public String converterTask(@RequestBody Editor editor) {
        PdfService pdfService = new PdfService();
        String timeFile = System.currentTimeMillis() + "";
        String tempFile = PdfService.RESOURCE_PREFIX_INDEX + "/" + "pdf" + "/";
        createDirs(tempFile);
        File pdfFile = createFlawPdfFile(tempFile, timeFile);
        long l1 = System.currentTimeMillis();
        pdfService.createPdfFromHtml(pdfFile.getName(), editor.getContent(), tempFile);
        long l2 = System.currentTimeMillis();
        System.out.println(l2 - l1 + "ms 转换完成;" + " 文件名:" + pdfFile.getName());
        return pdfFile.getName();
    }

    /**
     * 新建文件夹
     *
     * @param dirsPath
     */
    private static void createDirs(String dirsPath) {
        File temFile = new File(dirsPath);
        if (!temFile.exists()) {
            temFile.mkdirs();
        }
    }

    /**
     * 创建漏洞pdf版本空文件
     *
     * @param fileDir
     * @param fileName
     * @return
     */
    private static File createFlawPdfFile(String fileDir, String fileName) {
        File tempFile;
        do {
            tempFile = new File(fileDir + fileName + ".pdf");
        } while (tempFile.exists());
        return tempFile;
    }
}
