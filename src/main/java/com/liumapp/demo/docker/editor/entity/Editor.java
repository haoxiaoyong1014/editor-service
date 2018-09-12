package com.liumapp.demo.docker.editor.entity;

import org.springframework.stereotype.Component;

/**
 * Created by haoxy on 2018/9/11.
 * E-mail:hxyHelloWorld@163.com
 * github:https://github.com/haoxiaoyong1014
 */
@Component
public class Editor {

    private String content;

    public Editor() {
    }

    public Editor(String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

}
