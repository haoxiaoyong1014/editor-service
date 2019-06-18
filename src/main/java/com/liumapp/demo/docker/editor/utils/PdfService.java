package com.liumapp.demo.docker.editor.utils;

import com.itextpdf.html2pdf.ConverterProperties;
import com.itextpdf.html2pdf.HtmlConverter;
import com.itextpdf.html2pdf.attach.impl.DefaultTagWorkerFactory;
import com.itextpdf.io.source.ByteArrayOutputStream;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.*;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.BlockElement;
import com.itextpdf.layout.element.IElement;
import com.itextpdf.layout.font.FontProvider;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import java.io.*;
import java.util.List;

/**
 * Created by Haoxy on 2019-06-04.
 * E-mail:hxyHelloWorld@163.com
 * github:https://github.com/haoxiaoyong1014
 */
@Slf4j
public class PdfService {

    public static final String RESOURCE_PREFIX_INDEX = "/tmp";

    public static final String FONT_NAME = "msyh.ttf";

    /**
     * 添加字体
     *
     * @param dirStr
     */
    public void copyFont(String dirStr) {
        try {
            InputStream inputStream = PdfService.class.getClassLoader().getResourceAsStream(FONT_NAME);
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

    /**
     * @param pdfFileName:文件名
     * @param htmlInputStream:html文件流,这里我没有使用文件流的形式,因为我们的业务是前端直接传来一个 html文件内容;
     *                                                               如果你要使用这个方法需要再ItextHtmlToPdfController中加入
     *                                                               FileInputStream inputStream = new FileInputStream("/tmp/5.html");
     * @param resourcePrefix:                                        文件存储的地方
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

    /**
     * @param pdfFileName
     * @param htmlString     : html文件内容
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
    }


    /**
     * 新增方法:灵活设置 pdf的页边距
     *
     * @param pdfWriter
     * @return
     */
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


    private PdfDocument createPdfDoc(PdfWriter pdfWriter) {
        PdfDocument pdfDoc;
        pdfDoc = new PdfDocument(pdfWriter);
        pdfDoc.getCatalog().setLang(new PdfString("zh-CN"));
        pdfDoc.setTagged();
        pdfDoc.getCatalog().setViewerPreferences(new PdfViewerPreferences().setDisplayDocTitle(true));
        return pdfDoc;
    }

    private ConverterProperties createConverterProperties(String resourcePrefix) {
        ConverterProperties props = new ConverterProperties();
        props.setFontProvider(createFontProvider(resourcePrefix));
        props.setBaseUri(resourcePrefix);
        props.setCharset("UTF-8");
        DefaultTagWorkerFactory tagWorkerFactory = new DefaultTagWorkerFactory();
        props.setTagWorkerFactory(tagWorkerFactory);
        return props;
    }

    private FontProvider createFontProvider(String resourcePrefix) {
        FontProvider fp = new FontProvider();
        fp.addStandardPdfFonts();
        fp.addDirectory(resourcePrefix);
        return fp;
    }
}