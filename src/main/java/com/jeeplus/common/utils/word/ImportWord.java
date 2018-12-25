package com.jeeplus.common.utils.word;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.springframework.web.multipart.MultipartFile;


public class ImportWord {

	
	/**
	 * 构造函数
	 * @param path 导入文件对象
	 * @param headerNum 标题行号，数据行号=标题行号+1
	 * @param sheetIndex 工作表编号
	 * @throws InvalidFormatException 
	 * @throws IOException 
	 */
	public List<Map<String,Object>> ImportWordByDocx(final MultipartFile multipartFile ) {
		String fileName=multipartFile.getOriginalFilename();
		InputStream is;
		List<Map<String,Object>> questions=null;
		try {
			is = multipartFile.getInputStream();
			
			if (StringUtils.isBlank(fileName)){
				throw new RuntimeException("导入文档为空!");
			}else if(fileName.toLowerCase().endsWith("docx")){    
				 questions=analyWord(is);
	        }else{  
	        	throw new RuntimeException("文档格式不正确!");
	        } 
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
		return questions;
	}
	
	/**
	 * 解析word 内容
	 * @param inputStream
	 * @return
	 */
	public List<Map<String,Object>> analyWord(InputStream inputStream ){
		XWPFDocument xDocument;
		List<Map<String,Object>> questions=new ArrayList<>();
		
		Map<String,Object> selectMap = new HashMap<String,Object>();
		try {
			xDocument = new XWPFDocument(inputStream);
			List<XWPFParagraph> paragraphs = xDocument.getParagraphs();
			
			String text = "";
			int i = 0;
		
			int flag=0;
			for(XWPFParagraph paragraph : paragraphs){
				//System.out.println(paragraph.getParagraphText());
				List<XWPFRun> runs = paragraph.getRuns();
				String line = "";
				for(XWPFRun run : runs){
					/*System.out.println(run.getCTR().xmlText());*/
					String imgUrl = "";
					//判断是否是图片信息
					if(run.getCTR().xmlText().indexOf("<w:drawing>")!=-1){
					}else{
						text = text + run;
					}
					line += (run + imgUrl);
				}
//				System.out.println(i+"=========="+line);
//				String questiontype="";
				if(line.contains("【题型】") ){ //题型
					selectMap.put("id", UUID.randomUUID());
//					questiontype=line.replace("【题型】", "");
					selectMap.put("questiontype", line.replace("【题型】", ""));
					i++;
				}
				if(line.contains("【题文】") ){ //题文只能在一行中说题文
					selectMap.put("question", line.replace("【题文】", ""));
					i++;
				}
				
					//读取选项字段信息
				if(line.contains("【选项】") ){ //选项可以是多行
						selectMap.put("options", line.replace("【选项】", ""));
						i++;
						flag=1;
				}
				//如果不包含题文和答案 就都算在选项中
				if(flag==1&&!line.contains("【选项】") ){
					String options=selectMap.get("options").toString();
					selectMap.put("options", options+line);
					i++;
				}
					
				if(line.contains("【答案】") ){ //答案
					selectMap.put("answer", line.replace("【答案】", ""));
					i++;
					flag=0;
				}
				if(line.contains("【解析】") ){ //解析
					selectMap.put("explain", line.replace("【解析】", ""));
					i++;
				}
				if(line.contains("【结束】") ){
					System.out.println(selectMap);
					questions.add(selectMap);
					selectMap = new HashMap<String,Object>();
					i = 0;
				}
//				System.out.println(line);
			}
			
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	
		
		return questions;
	}
}
