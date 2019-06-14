package com.liumapp.demo.docker.editor.utils;

import com.itextpdf.io.font.PdfEncodings;
import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.io.source.ByteArrayOutputStream;
import com.itextpdf.kernel.events.Event;
import com.itextpdf.kernel.events.IEventHandler;
import com.itextpdf.kernel.events.PdfDocumentEvent;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.kernel.pdf.xobject.PdfFormXObject;
import com.itextpdf.layout.Canvas;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.property.TextAlignment;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Haoxy on 2019-06-14.
 * E-mail:hxyHelloWorld@163.com
 * github:https://github.com/haoxiaoyong1014
 */
public class Html2PdfUtil {


    // 页眉
    protected static class Header implements IEventHandler {
        protected float width = 102f;
        protected float height = 32f;
        protected float x = 42f;
        protected float y = 740f;

        @Override
        public void handleEvent(Event event) {
            PdfDocumentEvent docEvent = (PdfDocumentEvent) event;
            PdfDocument pdf = docEvent.getDocument();
            PdfPage page = docEvent.getPage();
            Rectangle pageSize = page.getPageSize();
            PdfCanvas pdfCanvas = new PdfCanvas(
                    page.getLastContentStream(), page.getResources(), pdf);
            Canvas canvas = new Canvas(pdfCanvas, pdf, pageSize);
            ImageData image = null;
            ClassLoader loader = Thread.currentThread().getContextClassLoader();
            InputStream logo = loader.getResourceAsStream("imgaes/logo.jpg");
            try {
                image = ImageDataFactory.create(toByteArray(logo));
            } catch (IOException e) {
                e.printStackTrace();
            }
            Image img = new Image(image);
            img.scaleAbsolute(width, height); // 图片宽高
            img.setFixedPosition(x, y); // 图片坐标 左下角(0,0)
            canvas.add(img);
        }
    }

    // 页脚
    protected class Footer implements IEventHandler {
        protected PdfFormXObject placeholder; // 相对坐标系
        protected float x = 82f;
        protected float y = 50f;
        protected float imageWidth = 6f;
        protected float imageHeight = 78f;
        protected float space = 10f;

        public Footer() {
            placeholder = new PdfFormXObject(new Rectangle(0, 0, 500, 78));

        }

        @Override
        public void handleEvent(Event event) {
            PdfDocumentEvent docEvent = (PdfDocumentEvent) event;
            PdfDocument pdf = docEvent.getDocument();
            PdfPage page = docEvent.getPage();
            Rectangle pageSize = page.getPageSize();
            PdfCanvas pdfCanvas = new PdfCanvas(
                    page.getLastContentStream(), page.getResources(), pdf);
            pdfCanvas.addXObject(placeholder, x + space, y);
            Canvas canvas = new Canvas(pdfCanvas, pdf, pageSize);
            ImageData image = null;
            ClassLoader loader = Thread.currentThread().getContextClassLoader();
            InputStream buleRed = loader.getResourceAsStream("imgaes/bule_red.JPG");
            try {
                image = ImageDataFactory.create(toByteArray(buleRed));
            } catch (IOException e) {
                e.printStackTrace();
            }
            Image img = new Image(image);
            img.scaleAbsolute(imageWidth, imageHeight);
            img.setFixedPosition(x, y);
            canvas.add(img);
            writeInfo(pdf);
            pdfCanvas.release();
        }

        public void writeInfo(PdfDocument pdf) {
            Canvas canvas = new Canvas(placeholder, pdf);
            canvas.setFontSize(7.5f);
            PdfFont pdfFont = null;
            try {
                // 微软雅黑
                ClassLoader loader = Thread.currentThread().getContextClassLoader();
                InputStream msyh = loader.getResourceAsStream("fonts/msyh.ttf");
                pdfFont = PdfFontFactory.createFont(toByteArray(msyh), PdfEncodings.IDENTITY_H, false);
            } catch (IOException e) {
                e.printStackTrace();
            }
            canvas.setFont(pdfFont); // 需要单独设置一下字体才能使用中文
            canvas.showTextAligned("http://www.xxxx.com",
                    0, 65, TextAlignment.LEFT);
            canvas.showTextAligned("深圳市南山区学府路东xxxxx  xxxxxx",
                    0, 50, TextAlignment.LEFT);
            canvas.showTextAligned("xxxxx Ixxxxxx,Xuefu Road Ease,Nan Shan District, Shenzhen xxxxxx",
                    0, 35, TextAlignment.LEFT);
            canvas.showTextAligned("Tel:0755-xxxxx Fax:212-xxxxxx",
                    0, 20, TextAlignment.LEFT);
        }

    }

    public static byte[] toByteArray(InputStream input) throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        byte[] buffer = new byte[4096];
        int n = 0;
        while (-1 != (n = input.read(buffer))) {
            output.write(buffer, 0, n);
        }
        return output.toByteArray();
    }
}
