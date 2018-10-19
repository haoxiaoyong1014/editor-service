package com.liumapp.demo.docker.editor.controller;

import com.alibaba.fastjson.JSON;
import com.liumapp.demo.docker.editor.entity.Editor;
import com.liumapp.demo.docker.editor.utils.Html2PDF;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by haoxy on 2018/9/11.
 * E-mail:hxyHelloWorld@163.com
 * github:https://github.com/haoxiaoyong1014
 */
@RestController
@RequestMapping(value = "html")
public class IndexController {

    @RequestMapping(value = "pdf")
    public String getContent (@RequestBody Editor editor) {
        Html2PDF html2PDF = new Html2PDF();
        html2PDF.html2pdf("/tmp/docker/doc/test2.pdf", editor.getContent());
        return JSON.toJSONString("success");
    }
}
