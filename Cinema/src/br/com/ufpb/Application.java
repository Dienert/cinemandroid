/**
 *  @author Dienert Vieira
 */
package br.com.ufpb;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.red5.server.adapter.MultiThreadedApplicationAdapter;
import org.red5.server.api.IClient;
import org.red5.server.api.IConnection;
import org.red5.server.api.IScope;
import org.red5.server.api.event.IEvent;
import org.red5.server.api.event.IEventListener;
import org.red5.server.api.stream.IBroadcastStream;
import org.red5.server.api.stream.IPlayItem;
import org.red5.server.api.stream.IPlaylistSubscriberStream;
import org.red5.server.api.stream.ISubscriberStream;

public class Application extends MultiThreadedApplicationAdapter implements IEventListener {
	
	//logger
    private static final Log log = LogFactory.getLog( Application.class );
    
    public static final int NUM_MAX_CONNS = 350;
    public int NUM_CURRENT_CONNS = 0;
    public int NUM_CURRENT_MAX_CONNS = 0;
    
	//Tratar as Rooms dos usuarios
	private IScope appScope;
   
	/**
	 * Called when the red5 starts Application 
	 * 
	 */
    public boolean appStart( IScope scope ) {
    	appScope = scope;
    	return true;
    }
	
    /**
     * Retrieve the number of clients connected
     * @return int
     */
    public int getCurrentContectionsNumber(){
    	return NUM_CURRENT_CONNS;
    }
    
    /**
     * Retrieve the number of clients connected
     * @return int
     */
    public int getCurrentMaxContectionsNumber(){
    	return NUM_CURRENT_MAX_CONNS;
    }
    /**
	 * Called when the red5 stops Application 
	 * 
	 */ 
    public void appStop(IScope scope) {
    	log.info("STOP");
    }
	/**
	 * Used by red5 when the User Attempts to connect to the 
	 * Application. At this moment, the number of current connections
	 * will be increased.
	 * 
	 * The user won't be granted access if there is no more room for him.
	 * 
	 * @param conn
	 * @param params
	 */ 
    public boolean appConnect( IConnection conn , Object[] params ) {
    	log.info("CONECTANDO...");
    	
    	if(NUM_CURRENT_CONNS>=NUM_MAX_CONNS){
    		this.rejectClient();
        	
//    		if (conn instanceof IServiceCapableConnection) {
//    	    IServiceCapableConnection sc = (IServiceCapableConnection) conn;
//    	    //sc.invoke("the_method", new Object[]{"One", 1});
//    	    this.rejectClient();
//    	}
    		return true;
    	}

    	//Incremente o numero de pessoas conexoes ativas
    	NUM_CURRENT_CONNS++;
    	if (NUM_CURRENT_CONNS > NUM_CURRENT_MAX_CONNS) {
    		NUM_CURRENT_MAX_CONNS = NUM_CURRENT_CONNS;
    	}
    	log.info("CONECTADO!");
    	//log.info(conn.getClient().toString());
    	return true;
	}
	/**
	 * Used by red5 when the User is disconnected from the 
	 * Application. At this moment, the number of current connections
	 * will be decreased
	 * 
	 * @param conn
	 */    
    public void appDisconnect( IConnection conn) {
    	NUM_CURRENT_CONNS--;
    	log.info("TERMINOU");
    }
    
	/**
	 * Set AppScope
	 * 
	 * @param appScope
	 */ 
	public void setAppScope(IScope appScope) {
		this.appScope = appScope;
	}

	/**
	 * Retrieves Application's Scope
	 * @return appScope
	 */
	public IScope getAppScope() {
		return appScope;
	}

	/**
	 * Called when Starts a room.
	 * @param room
	 * @return boolean[true==successful initialization | false== cannot initialize room]
	 */
	public boolean roomStart(IScope room) {
		return true;
	}

	
	/**
	 * Called when the User Joins a Room.
	 * @param client
	 * @param room
	 * @return boolean[true==successful join |false== cannot join a Room]
	 */
	public boolean join(IClient client, IScope room){
	
			
	    	try{
	    		//basic.registerServiceHandler("fishScope", new HandlerPlayerFish(basic)).serverStream));
	    		//serverStream.add( (new HandlerPlayerFish(basic)).serverStream);
	    		
	    	}catch (Exception e) {
	    		log.info(e.getMessage());
			}
	    	//basic.registerServiceHandler("fishScope", serverStream.add( (new HandlerPlayerFish(basic)).serverStream));
	    	 //serverStream.add( (new HandlerPlayerFish(basic)).serverStream);

	
		
		return true;
		
	}

	//	@Override
	//	public void streamBroadcastStart(IBroadcastStream stream){
	//		log.info("FUI CHAMADO PRA ESTARTARTado O STREAM");
	//	
	////		log.info("NOME DO STREAM QUE ESTARTOU "+stream.getName());
	//	}
	//	
	//	@Override
	//	public void streamBroadcastClose(IBroadcastStream stream){
	//		log.info("Terminei o streaming"+stream.getPublishedName());
	////		log.info("NOME DO STREAM QUE ESTARTOU "+stream.getName());
	//	}
	/**
	 * A broadcast stream starts being published. This will be called
	 * when the first video packet has been received.
	 * 
	 * @param stream
	 */
	public void streamPublishStart(IBroadcastStream stream){
		log.info("ORDEM DAS CHAMADAS: streamPublishStart");
	}

	/**
	 * A broadcast stream starts being recorded. This will be called
	 * when the first video packet has been received.
	 * 
	 * @param stream
	 */
	public void streamRecordStart(IBroadcastStream stream){
		log.info("ORDEM DAS CHAMADAS: streamRecordStart");
	}

	/**
	 * Notified when a broadcaster starts.
	 * 
	 * @param stream
	 */
	public void streamBroadcastStart(IBroadcastStream stream){
		log.info("ORDEM DAS CHAMADAS: streamBroadcastStart");
	}

	/**
	 * Notified when a broadcaster closes.
	 * 
	 * @param stream
	 */
	public void streamBroadcastClose(IBroadcastStream stream){
		log.info("ORDEM DAS CHAMADAS: streamBroadcastClose");
	}

	/**
	 * Notified when a subscriber starts.
	 * 
	 * @param stream
	 */
	public void streamSubscriberStart(ISubscriberStream stream){
		log.info("ORDEM DAS CHAMADAS: streamSubscriberStart");
	}

	/**
	 * Notified when a subscriber closes.
	 * 
	 * @param stream
	 */
	public void streamSubscriberClose(ISubscriberStream stream){
		log.info("ORDEM DAS CHAMADAS: streamSubscriberClose");
	}

	/**
	 * Notified when a playlist item plays.
	 * 
	 * @param stream
	 * @param item
	 * @param isLive
	 *            TODO
	 */
	public void streamPlaylistItemPlay(IPlaylistSubscriberStream stream,
			IPlayItem item, boolean isLive){
		log.info("ORDEM DAS CHAMADAS: streamPlaylistItemPlay");
	}

	/**
	 * Notified when a playlist item stops.
	 * 
	 * @param stream
	 * @param item
	 */
	public void streamPlaylistItemStop(IPlaylistSubscriberStream stream,
			IPlayItem item){
		log.info("ORDEM DAS CHAMADAS: streamPlaylistItemStop");
	}

	/**
	 * Notified when a playlist vod item pauses.
	 * 
	 * @param stream
	 * @param item
     * @param position
	 */
	public void streamPlaylistVODItemPause(IPlaylistSubscriberStream stream,
			IPlayItem item, int position){
		log.info("ORDEM DAS CHAMADAS: streamPlaylistVODItemPause");
	}

	/**
	 * Notified when a playlist vod item resumes.
	 * 
	 * @param stream
	 * @param item
     * @param position
	 */
	public void streamPlaylistVODItemResume(IPlaylistSubscriberStream stream,
			IPlayItem item, int position){
		log.info("ORDEM DAS CHAMADAS: streamPlaylistVODItemResume");
	}

	/**
	 * Notified when a playlist vod item seeks.
	 * 
	 * @param stream
	 * @param item
     * @param position
	 */
	public void streamPlaylistVODItemSeek(IPlaylistSubscriberStream stream,
			IPlayItem item, int position){
		log.info("ORDEM DAS CHAMADAS: streamPlaylistVODItemSeek");
	}

	/**
	 * Called to notify a Event to clients.
	 */
	@Override
	public void notifyEvent(IEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	



}
