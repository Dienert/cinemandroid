/**
 * 
 * Set up state of servers on red5 startup. Created only on time
 */
package br.com.ufpb;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.red5.server.api.IScope;
import org.springframework.context.ApplicationContext;

import br.com.ufpb.utils.ContextRedCinema;

/**
 * Class that runs when the application is started.
 * Used to create all the channels and start them.
 * 
 */
public class MainManager {
	
    private static final Log log = LogFactory.getLog( MainManager.class );
    private static MainManager instance;
    
	private IScope webScopeApp;
	
	/**
	 * Creates the red5 channels and starts them based on xml files on the folder.
	 */
	public MainManager(){
		
		ServerConnection server = new ServerConnection();
		server.start();
		System.out.println("Aceitando conexões");
		
		ApplicationContext ctx = ContextRedCinema.getApplicationContext();
		StreamManager ch = (StreamManager)ctx.getBean("channelManager");
		
		String channelName = "sala1";
		boolean created = false;
		boolean started = false;
		
		created = ch.addChannel(channelName);
		
		if(created){
			started = ch.startChannel(channelName);
			if(!started){
				log.info("Canal: "+channelName+" não pode ser iniciado.");
			} else {
				log.info("Canal: "+channelName+" iniciado.");
			}
		} else {
			log.info("Canal: "+channelName+" não pode ser adicionado.");
		}
	}
	
	/**
	 * Singleton of the Class
	 * @return instance
	 */
	public static synchronized MainManager getInstance() {
	      if (instance == null)
	    	 instance = new MainManager();
	      return instance;
	}
	
	/**
	 * Retrieves the Red5 main scope
	 * @return webscope
	 */
	public IScope getWebScopeApp() {
		return webScopeApp;
	}
	/**
	 * Set Red5 Main Scope
	 * @param webScopeApp
	 */
	public void setWebScopeApp(IScope webScopeApp) {
		this.webScopeApp = webScopeApp;
	}
}
