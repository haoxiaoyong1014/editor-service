### editor-service

项目需求: 富文本编辑器要支持导入word模板, 另外样式不能改变

富文本编辑器名称: tinymce 

结合前端 vue.js (<a href="https://github.com/haoxiaoyong1014/common-friends">editor-ui</a>)

**最终展示效果:** 

![image](https://github.com/haoxiaoyong1014/editor-service/raw/master/src/main/java/com/liumapp/demo/docker/editor/image/editor.gif)

**整体思路:**

1,在编辑器原来的基础上增加上传模板按钮

2, 前端上传 word 模板

3, 服务端接收将 word 转换为html 返回前端

4, 前端拿到我返回的值,将其放到富文本编辑器中

5, 前端点击submit,服务端将其转换成 pdf文件

**所需依赖:** 

```
    <dependency>
      <groupId>org.apache.poi</groupId>
      <artifactId>poi</artifactId>
      <version>3.12</version>
    </dependency>
    
    <dependency>
      <groupId>org.apache.poi</groupId>
      <artifactId>poi-scratchpad</artifactId>
      <version>3.12</version>
    </dependency>
    
    <dependency>
      <groupId>fr.opensagres.xdocreport</groupId>
      <artifactId>fr.opensagres.xdocreport.document</artifactId>
      <version>1.0.5</version>
    </dependency>
    
    <dependency>
      <groupId>fr.opensagres.xdocreport</groupId>
      <artifactId>org.apache.poi.xwpf.converter.xhtml</artifactId>
      <version>1.0.5</version>
    </dependency>
    
      <!-- https://mvnrepository.com/artifact/org.apache.commons.io/commonsIO -->
      <dependency>
        <groupId>org.apache.commons.io</groupId>
        <artifactId>commonsIO</artifactId>
        <version>2.6</version>
      </dependency>
      
    <dependency>
      <groupId>com.aspose.words</groupId>
      <artifactId>aspose-words</artifactId>
      <version>15.8.0</version>
    </dependency>
```

**其中 commonsIO 这个依赖不知道为什么下载不下来,我将 jar 放到了我的私服上,在pom.xml 中有体现,这里不做详细说明**

**前端项目使用方式**

git clone https://github.com/haoxiaoyong1014/editor-ui.git

进入项目执行:

npm install

npm run dev

前提: 需要安装 npm 