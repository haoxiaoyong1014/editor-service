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

#### 放到项目中遇到的问题修复

* 问题描述1: 

当上传模板之后点击浏览器刷新编辑框中的内容会变为之前上传的内容

* 解决方法:
```html

 if (localStorage.editorContent) {
                tinymce.get('tinymceEditer').setContent(localStorage.editorContent);
              }
              
```
将这段代码注释掉即可,因为编辑器会自动的将内容保存到本地,当你去点击浏览器刷新的时候他会去本地取出并赋值到编辑框中

* 问题描述2:

当你在编辑框中进行编辑的时候tinymce编辑器监听了键盘按下的事件,但是键盘按下的前一个字符没有保存,例如:

你在编辑框中输入4个字符 `aaaa` 你再点击submit生成pdf文件,但是 pdf文件中就只有3个字符`aaa`

* 解决方法:

因为编辑器只监听了`keydown`事件,并没有去监听`keyup`事件
所以加上如下代码即可

```html
editor.on('keyup', function (e) {
              localStorage.editorContent = tinymce.get('tinymceEditer').getContent();
              vm.editorModel.content = tinymce.get('tinymceEditer').getContent();
            });

``` 

* 问题描述3:

当点击submit 生成pdf文件时,生成的 pdf 文件样式改变了

* 解决方法:

这是因为将 word 文档转换成 html 的时候自动的加上了这段样式

`<div style="width: 595.0pt; margin: 72.0pt 90.0pt 72.0pt 90.0pt;"></div>`

解决方法可以在前端解决也可以在后端去解决,这里我选择了在后端解决

后端在返回给前端html 的时候,在返回的内容上加上

`respInfo.setContent("<div style=\"width: 595.0pt; margin: -72.0pt -90.0pt -72.0pt -90.0pt !important;\">"+content+"</div>")`
