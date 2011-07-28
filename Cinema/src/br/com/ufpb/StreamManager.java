package br.com.ufpb;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.red5.server.api.IScope;

import br.com.ufpb.utils.PlayItemAnalyzer;

/**
 * Class that manages the channels and stream.
 * 
 * @author Dienert Vieira
 */
public class StreamManager {
	
    private static final Log log = LogFactory.getLog( StreamManager.class );
    
    private static StreamManager instance;
    
	private IScope webScopeApp;
	
	private static String versao;
	
	private List<IServerPlaylist> streams = new LinkedList<IServerPlaylist>();
	private static List<PlayItemAnalyzer> playItemAnalyzers = new LinkedList<PlayItemAnalyzer>();
	
	public StreamManager(){}

	public static synchronized StreamManager getInstance() {
	      if (instance == null)
	    	 instance = new StreamManager();
	      return instance;
	}

	/**
	 * Adds a new channel to the list. 
	 * If the channel name is already in use it is not created.
	 * 
	 * @param channel name
	 * @return true if the channel was created, false if the channel already exists.
	 */
	public synchronized Boolean addChannel(String channelName){
		
		Boolean canAddChannel=true;
		ServerPlaylist channel = new ServerPlaylist();
		PlayItemAnalyzer pa = new PlayItemAnalyzer();
		
		for (IServerPlaylist streamChannel : streams) {
			if(streamChannel.getStreamServer().getName().equals(channelName)){
				canAddChannel=false;
			} else {
				canAddChannel=true;
			}
		}
		
		if(canAddChannel){
			channel.setRepeat(false);
			channel.setPath(PlaylistController.PATH);
	    	channel.setStreamName(channelName);
	    	channel.setRunOnStart(true);
	    	channel.setScope(webScopeApp);
	    	channel.init();
	    	pa.setServer(channel.getStreamServer());
	    	streams.add(channel);
	    	playItemAnalyzers.add(pa);
	    	
	    	log.info("ESTOU INICIANDO: "+channelName+" EM: "+ webScopeApp.getName());
		}
		
		return canAddChannel;

	}
	
	/**
	 * Starts to run a channel. 
	 * 
	 * @param channel name
	 * @return true if starts ok, false if not
	 */
	public synchronized Boolean startChannel(String channelName){
		
		ServerPlaylist channel = (ServerPlaylist) getChannel(channelName);
		
		if(channel != null){
			
			channel.start(false);
			return true;
			
		} else {
			
			log.info("Canal: "+channelName+" não encontrado.");
		}
		
		return false;
	}
	
	/**
	 * Remove a channel.
	 * 
	 * @param channel name
	 * @return true if the channel was removed, false if not
	 */
	public synchronized boolean delChannel(String ChannelName){
		
		Boolean canDelChannel = false;
		
		for (int cont=0;cont<streams.size();cont++){
			if(streams.get(cont).getStreamServer().getName().equals(ChannelName)){
				
				if(streams.get(cont).getStreamServer().getItemSize()>0){
					streams.get(cont).shutdown();
				}
				
				playItemAnalyzers.remove(cont);
				streams.remove(cont);
				canDelChannel=true;
			}
		}

		return canDelChannel;
	}
	
	/**
	 * Changes the channel schedule by removing all the old videos and adding all the new one. 
	 * The video that is current playing is not removed.
	 * 
	 * @param channelName
	 */
	public void changeSchedule(String channelName){
		ServerPlaylist channel = (ServerPlaylist) getChannel(channelName);
		
		if(channel != null){
			channel.changeSchedule();

			log.info("CHANGE SCHEDULE... CHANNEL NAME: "+channelName);
			
		} else {
			
			log.info("CHANNEL DOES NOT EXISTS");
			//TODO: throw new Exception;
		}
	}
	
	/**
	 * Loads the playlist of each channel. If the channel was already created the new videos
	 * are added to the playlist, if it wasn't a new playlist is created.
	 */
	public void loadPlaylist(){
		for (IServerPlaylist stream : streams) {
			if(stream.isStarted()){
				stream.setNewSchedule();
			} else {
				stream.start(false);
			}
		}
	}
	
	/**
	 * Gets a channel that was created by its name.
	 * 
	 * @param channelName channel name
	 * @return the channel
	 */
	private synchronized IServerPlaylist getChannel(String channelName){
		
		for (IServerPlaylist streamChannel : streams) {
			if(streamChannel.getStreamServer().getName().equals(channelName)){
				return streamChannel;
			}
		}
		
		return null;
	}
	
	/**
	 * Gets a channel analyzer.
	 * 
	 * @param channelName
	 * @return the analyzer if it was found, null if it wasn't. 
	 */
	public static synchronized PlayItemAnalyzer getAnalyzer(String channelName){
		for (PlayItemAnalyzer analyzer : playItemAnalyzers) {
			if(analyzer.getServer().getName().equals(channelName)){
				System.out.println("Encontrei analyzer do canal: "+channelName);
				return analyzer;
			}
		}
		System.out.println("Não Encontrei analyzer do canal: "+channelName);
		System.out.println("Tamanho da lista de analyzers: "+playItemAnalyzers.size());
		return null;
	}
	
	public IScope getWebScopeApp() {
		return webScopeApp;
	}
	
	public List<IServerPlaylist> getStreamServers(){
		return streams;
	}

	public void setWebScopeApp(IScope webScopeApp) {
		this.webScopeApp = webScopeApp;
	}

	public String getVersao() {
		return versao;
	}

	public void setVersao(String versao) {
		StreamManager.versao = versao;
	}
	
}
