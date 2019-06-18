### editor-service

项目需求: 富文本编辑器要支持导入word模板, 另外样式不能改变

富文本编辑器名称: tinymce 

结合前端 vue.js (<a href="https://github.com/haoxiaoyong1014/editor-ui">editor-ui</a>)

**最终展示效果:** 

![image](https://github.com/haoxiaoyong1014/editor-service/raw/master/src/main/java/com/liumapp/demo/docker/editor/image/editor.gif)

**整体思路:**

1,在编辑器原来的基础上增加上传模板按钮

2, 前端上传 word 模板

3, 服务端接收将 word 转换为html 返回前端

4, 前端拿到服务端返回的值,将其放到富文本编辑器中

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

**更详细内容见博客:** https://blog.csdn.net/haoxiaoyong1014/article/details/82683428


2019 06 04 更新:

新增: 利用itext7将html转pdf,

添加依赖:

```xml
    <dependency>
      <groupId>com.itextpdf</groupId>
      <artifactId>itext7-core</artifactId>
      <version>7.1.0</version>
      <type>pom</type>
    </dependency>
    
    <dependency>
      <groupId>com.itextpdf</groupId>
      <artifactId>html2pdf</artifactId>
      <version>2.0.0</version>
    </dependency>
    
    <dependency>
      <groupId>org.apache.commons</groupId>
      <artifactId>commons-io</artifactId>
      <version>1.3.2</version>
    </dependency> 
```

**使用方式:**

1,添加字体

方式一:

将resources/font中的`msyh.ttf`字体加入到存放转换后pdf文件的文件夹下;和pdf文件同级即可;

例如:我转换后的pdf文件放在了 `/tmp/pdf/`下,所以我的字体文件也要放在这个文件夹下;

方式二:

把字体文件`msyh.ttf`放到resources目录下;

在`PdfService`类中增加一个方法:

```java
 public void copyFont(String dirStr) {
        try {
            InputStream inputStream = PdfService.class.getClassLoader().getResourceAsStream("msyh.ttf");
            File dir = new File(dirStr);
            FileUtils.forceMkdir(dir);
            FileOutputStream fout = new FileOutputStream(dirStr + "msyh.ttf");
            if (inputStream != null && fout != null) {
                IOUtils.copy(inputStream, fout);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
```
在 ItextHtmlToPdfController中要这么写:
```java
 PdfService pdfService = new PdfService();
 String timeFile = System.currentTimeMillis() + "";
 String tempFile = PdfService.RESOURCE_PREFIX_INDEX + "/" + "pdf" + "/";
 createDirs(tempFile);
 //添加字体
 pdfService.copyFont(tempFile);
 FileInputStream inputStream = new FileInputStream("/tmp/5.html");
 File pdfFile = createFlawPdfFile(tempFile, timeFile);
 pdfService.createPdfFromHtml(pdfFile.getName(), inputStream, tempFile);
```
以下我是用的方式一

在`PdfService`类中有两个方法:

1, 根据html文件进行转换

```java
/**
     *
     * @param pdfFileName:文件名
     * @param htmlInputStream:html文件流,这里我没有使用文件流的形式,因为我们的业务是前端直接传来一个 html文件内容;
     *                        如果你要使用这个方法需要再ItextHtmlToPdfController中加入
     *                        FileInputStream inputStream = new FileInputStream("/tmp/5.html");
     * @param resourcePrefix: 文件存储的地方
     */
    public void createPdfFromHtml(String pdfFileName, InputStream htmlInputStream, String resourcePrefix) {
        PdfDocument pdfDoc = null;
        try {
            FileOutputStream outputStream = new FileOutputStream(resourcePrefix + pdfFileName);
            WriterProperties writerProperties = new WriterProperties();
            writerProperties.addXmpMetadata();
            PdfWriter pdfWriter = new PdfWriter(outputStream, writerProperties);
            pdfDoc = createPdfDoc(pdfWriter);
            ConverterProperties props = createConverterProperties(resourcePrefix);
            HtmlConverter.convertToPdf(htmlInputStream, pdfDoc, props);
        } catch (Exception e) {
            log.error("failed to create pdf from html exception: ", e);
            e.printStackTrace();
        } finally {
            pdfDoc.close();
        }

    }
```
在 ItextHtmlToPdfController中要这么写:

```java
        PdfService pdfService = new PdfService();
        String timeFile = System.currentTimeMillis() + "";
        String tempFile = PdfService.RESOURCE_PREFIX_INDEX + "/" + "pdf" + "/";
        createDirs(tempFile);
        FileInputStream inputStream = new FileInputStream("/tmp/5.html");
        File pdfFile = createFlawPdfFile(tempFile, timeFile);
        pdfService.createPdfFromHtml(pdfFile.getName(), inputStream, tempFile);
```

但是呢我们的项目需求不是这样的;我们项目中的需求是前端直接传 html 文件内容,所以用了下面这个方法;

```java
 /**
     *
     * @param pdfFileName
     * @param htmlString : html文件内容
     * @param resourcePrefix
     */
    public void createPdfFromHtml(String pdfFileName, String htmlString, String resourcePrefix) {
        PdfDocument pdfDoc = null;
        try {
            FileOutputStream outputStream = new FileOutputStream(resourcePrefix + pdfFileName);
            WriterProperties writerProperties = new WriterProperties();
            writerProperties.addXmpMetadata();
            PdfWriter pdfWriter = new PdfWriter(outputStream, writerProperties);
            pdfDoc = createPdfDoc(pdfWriter);
            ConverterProperties props = createConverterProperties(resourcePrefix);
            HtmlConverter.convertToPdf(htmlString, pdfDoc, props);
        } catch (Exception e) {
            log.error("failed to create pdf from html exception: ", e);
            e.printStackTrace();
        } finally {
            pdfDoc.close();
        }

```

这样的话当然你的 ItextHtmlToPdfController中要这么写:

```java
        PdfService pdfService = new PdfService();
        String timeFile = System.currentTimeMillis() + "";
        String tempFile = PdfService.RESOURCE_PREFIX_INDEX + "/" + "pdf" + "/";
        createDirs(tempFile);
        File pdfFile = createFlawPdfFile(tempFile, timeFile);
        pdfService.createPdfFromHtml(pdfFile.getName(), htmlContent, tempFile);
```

结合前端,如果需要用 itext7来转 pdf,前端的路径只需要改为 `/itext/html/pdf`即可;

```js
handleSubmit(name) {
        this.$refs[name].validate((valid) => {
          if (valid) {
            util.post('/itext/html/pdf', this.editorModel).then(res => {
              this.$Message.success('Success!');
            });
          } else {
            this.$Message.error('Fail!');
          }
        });
      }
```
之所以用itext7,是因为他对表格的处理很友好;

2019/06/05更新:

在使用poi进行docx(word2007)转换成html时,会自动生成一个div, `<div style="width: 595.0pt; margin: 72.0pt 90.0pt 72.0pt 90.0pt;">`,但是我不需要,因为有
了这个 div 之后,再转换成 pdf 会出问题;所以我要去掉,加入下面这段话即可:

```java
content = Pattern.compile("width:\\d*[.]\\d*pt;").matcher(content).replaceAll("592.0pt;");
//content = Pattern.compile("width:595.0pt;").matcher(content).replaceAll("");
content = Pattern.compile("margin-left:\\d*[.]\\d*pt;").matcher(content).replaceAll("");
content = Pattern.compile("margin-right:\\d*[.]\\d*pt;").matcher(content).replaceAll("");
```

生成这html中会是下面这个样子: `<div style="margin-bottom: 72.0pt; margin-top: 72.0pt;">`

不同的文档`width`是不一样的但是大部分都是 `592`,`595`;

试情况而定,如果都不想要的话,都可以用正则表达式去掉;

在进行转换成 pdf 文件就没有问题了,转换 pdf 时用的 itext7; 详细内容看代码;

2019-06-12更新

更新内容: 解决异常

异常信息:

```
Caused by: java.lang.NullPointerException 
at org.apache.poi.xwpf.converter.core.styles.run.RunUnderlineValueProvider.getValue(RunUnderlineValueProvider.java:40)
```
解决方法:

创建一个package名为: `org.apache.poi.xwpf.converter.core.styles.run`;

新建一个RunUnderlineValueProvider类,代码如下:

```java
public class RunUnderlineValueProvider extends AbstractRunValueProvider<UnderlinePatterns> {

    public static final RunUnderlineValueProvider INSTANCE = new RunUnderlineValueProvider();

    @Override
    public UnderlinePatterns getValue(CTRPr rPr, XWPFStylesDocument stylesDocument) {

        if (rPr == null) {
            return null;
        }
        if (rPr.isSetU()) {
            CTUnderline u = rPr.getU();
            if (u != null) {
                Enum val = u.getVal();
                if (val != null) {
                    return UnderlinePatterns.valueOf(val.intValue());
                }
            }
        }
        return null;
    }
}
```

这样系统会优先调用自己写的这个类;

2019-06-14 更新: 增加灵活设置pdf的页边距

在`PdfService`中新增方法: 

```java
    public static final float topMargin = 0f;
    public static final float bottomMargin = 0f;
    public static final float leftMargin = 30f;
    public static final float rightMargin = 30f;

    public void convertPageSpacing(String pdfFileName,String html,String resourcePrefix) throws IOException {
        //ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        FileOutputStream outputStream = new FileOutputStream(resourcePrefix + pdfFileName);
        PdfWriter writer = new PdfWriter(outputStream);
        PdfDocument pdfDocument = new PdfDocument(writer);
        try {
            ConverterProperties props = new ConverterProperties();
            FontProvider fp = new FontProvider();
            fp.addStandardPdfFonts();
            fp.addDirectory(resourcePrefix);
            /*ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
            fp.addDirectory(classLoader.getResource("msyh.ttf").getPath());*/
            props.setFontProvider(fp);
            List<IElement> iElements = HtmlConverter.convertToElements(html, props);
            Document document = new Document(pdfDocument, PageSize.A4, true); // immediateFlush设置true和false都可以，false 可以使用 relayout
            document.setMargins(topMargin, rightMargin, bottomMargin, leftMargin);
            for (IElement iElement : iElements) {
                BlockElement blockElement = (BlockElement) iElement;
                blockElement.setMargins(1, 0, 1, 0);
                document.add(blockElement);
            }
            document.close();
        } catch (Exception e) {
            throw e;
        } finally {
            outputStream.close();
        }
    }
```
上面四个值(`topMargin`,`bottomMargin`,`leftMargin`,`rightMargin`)按需定义;


2019-06-18 更新

添加`Html2PdfUtil`类,增加两个方法,分别为增加页眉和页脚;