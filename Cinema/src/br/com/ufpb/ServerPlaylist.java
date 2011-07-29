/**
 * Used to set up Server Stream. We application can work with many servers
 */
package br.com.ufpb;

import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.TreeMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.red5.server.api.IScope;
import org.red5.server.api.stream.IServerStream;
import org.red5.server.api.stream.support.SimplePlayItem;
import org.red5.server.api.stream.support.StreamUtils;

import br.com.ufpb.utils.DateUtil;
import br.com.ufpb.utils.PlayItemAnalyzer;

@SuppressWarnings("unchecked")
public class ServerPlaylist implements IServerPlaylist{
	
	//logger
    private static final Log log = LogFactory.getLog( Application.class );
    
	//path(do arquivo)	
	private String path = "";
	
	//FLV
	private String pattern = "";
	
	//Nome do Stream
	private String name = "";
	
	//Ativar repeticao
	private Boolean repeat = false;
	
	//Rodar
	private Boolean runOnStartUp = false;
	
	//Escopo do usuario
	private IScope appScope;
	
	//Interface que contem IBroadcast e Iplaylist
	private IServerStream serverStream;
	
	private boolean started = false;
	
	private Timer timer = new Timer();
	
	private HashMap<String, String[]> hashmap;
	
	private String answer = "intro";
	
	private static ServerPlaylist instance;
	
	public ServerPlaylist(){}
	
	public static ServerPlaylist getInstance(){
		return instance;
	}
	
	public IServerStream getStreamServer(){
		return serverStream;
	}
	
	/** Setar o escopo do usuarios */
	public void setScope(IScope scope) {
		appScope = scope;
	}
	
	/** Ativar repeticao ou nao */
	public void setRepeat(Boolean repeat) {
		this.repeat = repeat;
	}
	
	/** Configurar Path dos FLVs */
	public void setPath(String path) {
		this.path = path;
	}
	
	/** Padrao	*/
	public void setPattern(String pattern) {
		this.pattern = pattern;
	}
	
	public String getPattern(){
		return pattern;
	}
	
	/** Nome do Servico de Stream */
	public void setStreamName(String name) {
		this.name = name;
	}
	
	/** Iniciar quando servidor estartar? */
	public void setRunOnStart(Boolean value) {
		runOnStartUp = value;
	}
	
	/** qual o modo de inicializacao */
	public Boolean getRunOnStart() {
		return runOnStartUp;
	}
	
	public void init(IScope scope) {
		appScope = scope;
		if (runOnStartUp) {
			start(false);
		}
	}
	/**
	 * Creates a server instance
	 */
	public void init() {
		hashmap = new HashMap<String, String[]>();
		hashmap.put("intro", new String[]{"let him in", "leave him behind", "1"});
		hashmap.put("let him in", new String[]{"intro", "leave him behind", "1.2"});
		hashmap.put("leave him behind", new String[]{"let him in", "intro", "1.1"});
		serverStream = StreamUtils.createServerStream(appScope, this.name);
		instance = this;
//		if (runOnStartUp) {
//			start();
//		} else start();
	}
	
	/**
	 * Shutdowns streaming
	 */
	public void shutdown() {
		stop();
	}
	/**
	 * Stops server stream
	 */
	public void stop() {
		serverStream.pause();
		started = false;
	}
	
	/** 
	 * Stars Streaming 
	 * @param  alreadyPlaying[needed knowledge about status of streaming ]
	 */
	public void start(boolean alreadyPlaying)	{
		
		SimplePlayItem item = new SimplePlayItem();
		
		String[] strings = hashmap.get(answer);
		
		String versao = StreamManager.getInstance().getVersao();
		
		if (versao != null && !versao.equals("")) {
			item.setName(strings[2]+versao+".flv");
		} else {
			item.setName(strings[2]+".flv");
		}
		
		
		System.out.println("Iniciando o canal: "+this.name);
		
		String path = CustomFilenameGenerator.playbackPath;
		
		int length = MplayerInfo.getLength(new File(path+item.getName()));
		
		item.setLength(length);
		
		System.out.println("adicionando >> "+item.getName());
			
		serverStream.removeAllItems();
		serverStream.addItem(item);
	
		serverStream.setRewind(this.repeat);
		if (!alreadyPlaying) {
			serverStream.start();
		} else {
			serverStream.setItem(0);
		}
		started = true;
		PlayItemAnalyzer analyzer = StreamManager.getAnalyzer(this.name);
		if (analyzer != null) {
			analyzer.run();
		}
		log.info("FINISH");
	}
	/**
	 * Schedule the start of service.
	 * @param Date 	
	 **/
	public void goTimer(Date dataDisparo){	
			timer.schedule(new TimerTask() {
		        public void run() {
		        	System.out.println("Disparado o Start em ServerPlayList");
		        	serverStream.start();
		        	started = true;
		        }
		    },dataDisparo);
		}
	
	/**
	 * Retrieves a video list from a XML with the channel name
	 * 
	 * @param path 
	 * @param fileName[channel.xml]
	 * @return TreeMap
	 */
	public TreeMap getDirectoryFileList(String path, String fileName) {
		
		TreeMap<String, Map> filesMap = new TreeMap<String, Map>();
		Map<String, Object> fileInfo;
		File xmlFile = new File(path+fileName);
		
		if(xmlFile.exists()){
			
			List<PlaylistItem> items = PlaylistController.getPlaylist(xmlFile);
			
			if (items != null) {
			 
				for (PlaylistItem item : items) {
					
					File file = item.getFile();
					
					Date lastModifiedDate = new Date(file.lastModified());
					String lastModified = DateUtil.outputDate(lastModifiedDate);
					String flvName = file.getName();
					String flvBytes = Long.toString(file.length());
					
					fileInfo = new HashMap<String, Object>();
					fileInfo.put("name", flvName);
					fileInfo.put("lastModified", lastModified);
					fileInfo.put("size", flvBytes);
					fileInfo.put("startPlay", item.getTimeBegin().getTime());
					fileInfo.put("endPlay", item.getTimeEnd().getTime());
					
					String key = item.getTimeBegin().getTime()+"."+item.getTimeEnd().getTime()+"."+flvName;
					
					filesMap.put(key, fileInfo);
				}
			}
		}
		return filesMap;
	}
	/**
	 * Resets videos's list to play
	 */
	public void setNewSchedule(){
		
		Map map = getDirectoryFileList(this.path, serverStream.getPublishedName()+".xml");
		
		if (map.size() > 0) {
			
			Iterator it = map.keySet().iterator();
			
			while (it.hasNext()) {
				
				String key = (String) it.next();
				HashMap<String, Object> value = (HashMap<String, Object>) map.get(key);
				
				Long initialTime = ((Long) value.get("startPlay"));
				Long finalTime = ((Long) value.get("endPlay"));
				
				Long fileDuration = finalTime - initialTime;
				
				SimplePlayItem item = new SimplePlayItem();
				item.setName((String)value.get("name"));
				item.setLength(fileDuration);

				serverStream.addItem(item);
				
				if(name.equals("TVBRASIL_JULIANA_46802")){
					
					if(!value.get("name").equals("black_video.flv")){
						log.info("->>>>  "+value.get("name"));
					}
				}
				
			}
		} else {
			log.info("Nothing to add to NEW playlist");
		}
	}
	/**
	 * Called by WebService to complete the workflow of the reset video's list
	 */
	public void changeSchedule(){
		
		if(!isStarted()){
			
			start(false);
			
		} else {
			stop();
			serverStream.removeAllItems();
			start(true);
		}
	}
	/**
	 * Return status of Streaming server
	 * @return boolean[true==started | false==stoped ]
	 */
//	
//	public void changeSchedule(){
//		
//		for(int i = 1; i < serverStream.getItemSize(); i++){
//			serverStream.removeItem(i);
//		}
//		
//		Map map = getDirectoryFileList(this.path, serverStream.getPublishedName()+".xml");
//		
//		if (map.size() > 0) {
//			
//			Iterator it = map.keySet().iterator();
//			
//			while (it.hasNext()) {
//				
//				String key = (String) it.next();
//				HashMap<String, Object> value = (HashMap<String, Object>) map.get(key);
//				
//				Long initialTime = ((Long) value.get("startPlay"));
//				Long finalTime = ((Long) value.get("endPlay"));
//				
//				Long fileDuration = finalTime - initialTime;
//				
//				Date initialDate = new Date(initialTime);
//				
//				SimplePlayItem item = new SimplePlayItem();
//				item.setName((String)value.get("name"));
//				item.setLength(fileDuration);
//
//				if(initialDate.after(new Date())){
//					serverStream.addItem(item);
//				}
//				
//			}
//		} else {
//			log.info("Nothing to add to NEW playlist");
//		}
//	}
//	
	public boolean isStarted(){
		return started;
	}
	
	public HashMap<String, String[]> getHashMap() {
		return hashmap;
	}
	
	public String getAnswer() {
		return answer;
	}
	
	public void setAnswer(String answer) {
		this.answer = answer;
	}
}
