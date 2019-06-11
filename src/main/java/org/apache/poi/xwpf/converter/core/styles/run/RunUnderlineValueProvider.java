package org.apache.poi.xwpf.converter.core.styles.run;

import org.apache.poi.xwpf.converter.core.styles.XWPFStylesDocument;
import org.apache.poi.xwpf.usermodel.UnderlinePatterns;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRPr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTUnderline;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STUnderline.Enum;


/**
 * Created by Haoxy on 2019-06-10.
 * E-mail:hxyHelloWorld@163.com
 * github:https://github.com/haoxiaoyong1014
 */

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

        // old code
//      return (rPr != null && rPr.isSetU())
//              ? UnderlinePatterns.valueOf(rPr.getU().getVal().intValue()) : null;

    }
}
