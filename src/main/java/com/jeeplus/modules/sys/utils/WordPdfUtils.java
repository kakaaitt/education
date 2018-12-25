package com.jeeplus.modules.sys.utils;

import java.io.File;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * @author Rocca
 *
 */
public class WordPdfUtils {

    protected static final Logger logger = LoggerFactory.getLogger(WordPdfUtils.class);

    public static boolean wordConverterToPdf(String docxPath) throws IOException {
        File file = new File(docxPath);
        String path = file.getParent();
        try {
            String osName = System.getProperty("os.name");
            String command = "";
            if (osName.contains("Windows")) {
                //soffice --convert-to pdf  -outdir E:/test.docx
                command = "soffice -convert-to pdf -outdir " + path + " " + docxPath;
            } else {
//                command = "doc2pdf -output=" + path + File.separator + file.getName().replaceAll(".(?i)docx", ".pdf") + " " + docxPath;
                command = "/usr/bin/libreoffice6.1 --invisible --convert-to pdf -outdir " + path + " " + docxPath;
            }
            String result = CommandExecute.executeCommand(command);
//          LOGGER.info("result==" + result);
            System.out.println("生成pdf的result==" + result);
            if (result.equals("") || result.contains("writer_pdf_Export")) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
        return false;
    }

    //测试用
    public static void main(String[] args) {
        try {
            wordConverterToPdf("C:\\Users\\Administrator\\Desktop\\测试导入word2.docx");
        } catch (IOException e) {
            System.out.println("word转换成pdf时出错");
            e.printStackTrace();
        }
    }
}
