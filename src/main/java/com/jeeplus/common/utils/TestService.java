package com.jeeplus.common.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFPictureData;
import org.apache.poi.xwpf.usermodel.XWPFRun;

public class TestService {

	public static void main(String[] args){
		docx();
	}
    
    public static void docx() {
    	String importPath = "E:\\选择题.docx";
		String absolutePath = "E:\\";
		try {
			FileInputStream inputStream = new FileInputStream(importPath);
			XWPFDocument xDocument = new XWPFDocument(inputStream);
			
			List<XWPFParagraph> paragraphs = xDocument.getParagraphs();
			//读取图片信息
			List<XWPFPictureData> pictures = xDocument.getAllPictures();
			Map<String, String> map = new HashMap<String, String>();
			for(XWPFPictureData picture : pictures){
				String id = picture.getParent().getRelationId(picture);
				File folder = new File(absolutePath);
				if (!folder.exists()) {
					folder.mkdirs();
				}
				String rawName = picture.getFileName();
				String fileExt = rawName.substring(rawName.lastIndexOf("."));
				String newName = System.currentTimeMillis() + UUID.randomUUID().toString() + fileExt;
				File saveFile = new File(absolutePath + File.separator + newName);
				@SuppressWarnings("resource")
				FileOutputStream fos = new FileOutputStream(saveFile); 
				fos.write(picture.getData());
				System.out.println("id===777==="+id);
				System.out.println(saveFile.getAbsolutePath());
				//将图片地址信息，跟id信息绑定
                map.put(id, saveFile.getAbsolutePath());
			}
			String text = "";
			int i = 0;
			Map<String,Object> selectMap = new HashMap<String,Object>();
			for(XWPFParagraph paragraph : paragraphs){
				//System.out.println(paragraph.getParagraphText());
				List<XWPFRun> runs = paragraph.getRuns();
				String line = "";
				for(XWPFRun run : runs){
					/*System.out.println(run.getCTR().xmlText());*/
					String imgUrl = "";
					//判断是否是图片信息
					if(run.getCTR().xmlText().indexOf("<w:drawing>")!=-1){
						String runXmlText = run.getCTR().xmlText();
						int rIdIndex = runXmlText.indexOf("r:embed");
						int rIdEndIndex = runXmlText.indexOf("/>", rIdIndex);
						
						String rIdText = runXmlText.substring(rIdIndex, rIdEndIndex);
						System.out.println(rIdText.split("\"")[1].substring("rId".length()));
					
						String id = rIdText.split("\"")[1];
						System.out.println(map.get(id));
						
						imgUrl = map.get(id);
						text = text +"<img src = '"+map.get(id)+"'/>";
					}else{
						text = text + run;
					}
					line += (run + imgUrl);
				}
				System.out.println(i+"=========="+line);
				if(line.contains("【题文】") ){ //题文
					selectMap.put("id", UUID.randomUUID());
					selectMap.put("question", line.replace("【题文】", ""));
					i++;
				}
				if(isSelecteTitele(line) ){ //选项
					Pattern p = Pattern.compile("\\s+");
					Matcher m = p.matcher(line);  
					line= m.replaceAll(" ");
					String answer[] = line.split("\\s+");
					
					for(int j = 0;j<answer.length;j++){
						selectMap.put("option"+j, answer[j].trim());
					}
//					selectMap.put("options", line);
					i++;
				}
				if(line.contains("【答案】") ){ //答案
					selectMap.put("answer", line.replace("【答案】", ""));
					i++;
				}
				if(line.contains("【解析】") ){ //解析
					selectMap.put("explain", line.replace("【解析】", ""));
					i++;
				}
				if(line.contains("【结束】") ){
					System.out.println(selectMap);
					selectMap = new HashMap<String,Object>();
					i = 0;
				}
//				System.out.println(line);
			}
//			System.out.println(text);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    /**
     * 判断Str是否是选择题选择项
     * @param str 内容
     * @return
     */
    public static boolean isSelecteTitele(String str) {
        Pattern pattern = Pattern.compile("^([a-zA-Z]+[\\. | \\、| \\．| \\､ ].*)");

        if(StringUtils.isNotBlank(str)){
            //str=str.replaceAll("\\s*|\t|\n|\r", "");
            for (int i = 0; i < str.length(); i++) {
                //空格 160 12288全角Unicode空格  8197 4分之一全角空格  en空格8194（两个普通空格） em空格 8195
                if (str.charAt(i) != 160 && str.charAt(i) != 12288 && str.charAt(i) != 8197 && str.charAt(i) != 32 && str.charAt(i) != 8195 && str.charAt(i) != 8194) {
                    return pattern.matcher(str.substring(i)).matches();
                }
            }
        }

        return false;
    }
}
