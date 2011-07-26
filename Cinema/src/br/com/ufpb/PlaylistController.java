package br.com.ufpb;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.TimeZone;

import org.apache.commons.lang.time.DateUtils;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

import br.com.ufpb.utils.DateUtil;

public class PlaylistController {
	
	public static final String XML_CHANNEL_TAG = "channel";
	public static final String XML_SCHEDULE_TAG = "schedule";
	public static final String XML_DATE_TAG = "date";
	public static final String XML_TIME_TAG = "time";
	public static final String XML_FILE_TAG = "file";
	
	public static final String XML_DAY_ATT = "day";
	public static final String XML_VALUE_ATT = "value";
	public static final String XML_START_ATT = "start";
	public static final String XML_END_ATT = "end";
	
	public static final String PATH = "/home/dienert/Desktop/Red5/";
	
	public static final String TODAY = "today";
	public static final String TOMORROW = "tomorrow";
	
	/**
	 * 
	 * 
	 * @param folderPath 
	 * 		Absolute path to the folder where the disred files are located
	 * @param filesExtension 
	 * 		Filter which specifies the desired files' extension
	 * @return
	 * 		A List() containing the files located into the specified folder
	 */
	public static List<File> listFiles(String folderPath, String filesExtension){
		File folder = new File(folderPath);
		
		List<File> files = new ArrayList<File>();
		
		if(filesExtension != null && !filesExtension.isEmpty()){
			for (File file : folder.listFiles()) {
				if(checkFileExtension(file.getName(), filesExtension)){
					files.add(file);
				}
			}
			
			return files;
		} else {
			return Arrays.asList(folder.listFiles());
		}
		
	}
	
	/**
	 * @param fileName
	 * 		The name of the file which are being analyzed
	 * @param extension
	 * 		The file extension to compare with the file name
	 * @return
	 * 		True if the file name ends with the specified extension; false if not.
	 */
	public static boolean checkFileExtension(String fileName, String extension){
		return fileName.toLowerCase().endsWith(extension.toLowerCase());
	}
	
	@SuppressWarnings("unchecked")
	public static List<PlaylistItem> getPlaylist(File xmlFile){
		
		List<PlaylistItem> items = new ArrayList<PlaylistItem>();
		
		try {
			
			SAXBuilder sb = new SAXBuilder();
			
			Document document = sb.build(xmlFile);
			Element channelSchedule = document.getRootElement();
			List<Element> scheduleList = channelSchedule.getChildren();
			
			for (Element schedule: scheduleList) { 
				
		       String day = schedule.getAttributeValue(XML_DAY_ATT);
		       
		       if(day.equals(TODAY)){
		    	   
		    	   List<Element> datesList = schedule.getChildren();
		    	   
		    	   for(Element date: datesList){
		    		   
		    		   String value = date.getAttributeValue(XML_VALUE_ATT);
		    		   
		    		   if(value != null){
		    			   
		    			   Date dt = DateUtil.convertDateBR(value);
		    			   
		    			   GregorianCalendar now = new GregorianCalendar();
		    			   now.setTimeZone(TimeZone.getDefault());
		    			   
		    			   if(DateUtil.isSameDay(dt, now.getTime())){
		    				   
		    				   List<Element> timesList = date.getChildren();
		    				   
		    				   for (Element time: timesList){
		    					   
		    					   String fileName = time.getChildText(XML_FILE_TAG);
		    					   
		    					   if(fileName != null && !fileName.isEmpty()){
		    						   
		    						   File file = new File(fileName);
		    						   
		    						   String starts = time.getAttributeValue(XML_START_ATT);
		    						   Date start = DateUtil.convertDateTime(value+" "+starts);
		    						   String ends = time.getAttributeValue(XML_END_ATT);
		    						   Date end = DateUtil.convertDateTime(value+" "+ends);
		    						   
		    						   if(end.before(start)){
		    							   end = DateUtils.addDays(end, 1);
		    						   }
		    						   
		    						   PlaylistItem item = new PlaylistItem();
		    						   item.setFile(file);
		    						   item.setTimeBegin(start);
		    						   item.setTimeEnd(end);
		    						   
		    						   items.add(item);
		    					   }
		    				   }
		    				   
		    			   }
		    			   
		    		   	}
		    			   
		    	   }
			        
		       }
		    }
			
		} catch (JDOMException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return items;
	}
	
	/**
	 * Remove um vídeo da pasta do Red5 e o arquivo .meta ligado a ele.
	 * 
	 * @param channelName -  nome do canal
	 * @param fileName - Nome do arquivo
	 */
	public static void removeVideo(String channelName, String fileName){
		
		System.out.println("Já posso remover o arquivo: "+fileName+" do canal: "+channelName);
		try {
			
			if(!verifyFileUse(fileName)){
				
				File file = new File(PATH+fileName);
				if(file.exists()){  
					file.delete();
				} 
				
				File fileMeta = new File(PATH+fileName+".meta");
				if(fileMeta.exists()){  
					fileMeta.delete();
				} 
			} 
			
		} catch (JDOMException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@SuppressWarnings("unchecked")
	public static boolean verifyFileUse(String fileName) throws JDOMException, IOException {
		
		List<File> files = listFiles(PATH, "xml");
		
		for(File f: files){
				
			SAXBuilder sb = new SAXBuilder();
			Document document = sb.build(f);
			Element channelSchedule = document.getRootElement();
			
			List<Element> scheduleList = channelSchedule.getChildren();
			
			for(Element schedule: scheduleList){
				
				List<Element> dates = schedule.getChildren();
				String day = schedule.getAttributeValue(XML_DAY_ATT);
				
				for(Element dateElement: dates){
					
					String date = dateElement.getAttributeValue(XML_VALUE_ATT);
					List<Element> times = dateElement.getChildren();
					
					for(Element time: times){
						
						String file = time.getChild(XML_FILE_TAG).getValue();
						
						if(file != null && !file.isEmpty()){
							
							String name = PATH+fileName;
							
							if(name.equals(file)){
								if(day.equals(TOMORROW)){
									return true;
								} else {

									GregorianCalendar now = new GregorianCalendar();
									now.setTimeZone(TimeZone.getDefault());
									
									String starts = time.getAttributeValue(XML_START_ATT);
									Date start = DateUtil.convertDateTime(date+" "+starts);
									
									String ends = time.getAttributeValue(XML_END_ATT);
									Date end = DateUtil.convertDateTime(date+" "+ends);
									
									
									if(end.before(start)){
										end = DateUtils.addDays(end, 1);
									}
									
									if(DateUtil.isThisDayBetween(start, end, now.getTime()) || start.after(now.getTime())){
										return true;
									}
										
								}
							}
							
						}
					}
					
				}
			}
		}
		
		return false;
	}
	
}
