/**
 * Analyzes Playlist of channels. This class is linked directly with PlaylistController because the videos released for removal
 */
package br.com.ufpb.utils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.red5.server.api.stream.IPlayItem;
import org.red5.server.api.stream.IServerStream;

import br.com.ufpb.SendOptions;


public class PlayItemAnalyzer {
	//logger
    private static final Log log = LogFactory.getLog( PlayItemAnalyzer.class );
	private IServerStream server;
	
	private int CurrentItemIndex=0;
	private int freeItemIndex=0;
	private IPlayItem currentItem;
	private IPlayItem freeItem;
	 
	//private Calendar calendar;
	private int delay = 5000;   // delay for 5 sec.
	private int period = 20000;  // repeat every sec.
	private Timer timer = new Timer();
	
	public static ArrayList<String> ips = new ArrayList<String>();

//	private PlaylistController pcontroller =  new PlaylistController();
	
	public PlayItemAnalyzer(){}
	
	public void setServer(IServerStream iServerStream){
		this.server = iServerStream;
	}
	
	public IServerStream getServer(){
		return this.server;
	}
	/**
	 * Checks if current iten is playing
	 * 
	 * @return boolean		true==playing| false==not playing
	 */
	public boolean CurrentPlaying(){
		if (CurrentItemIndex == server.getCurrentItemIndex()){
			return true;
		}
		return false;
		
	}
	/**
	 * Run PlayItemAnalyzer
	 */
	public void run(){
		currentItem = server.getCurrentItem();
		if(currentItem!=null)	goTimer();
		else {log.debug("NÃO HA NADA NA LISTA DO CANAL: "+server.getName());};
		
		
	}
	
	/**
	 * Next time to dispatch analyzer
	 * a verificação
	 * @return Date 	next date 
	 */
	public Date nextTime(){
		Calendar cal = new GregorianCalendar();
		cal.setTimeZone(TimeZone.getDefault());
		//Proximo minuto de verificacao
		cal.setTimeInMillis(cal.getTimeInMillis());
		System.out.println("Iniciado às "+cal.getTime());
		cal.setTimeInMillis(cal.getTimeInMillis()+currentItem.getLength()-20*1000);
		System.out.println("###Requisitar escolha às: "+cal.getTime());
		
		
		return cal.getTime();
	}
	
	/**
	 * Disptacher. responsible method 
	 * for analyzing the current item and
	 * removing videos already viewed
	 */
	public void goTimer(){
		
		timer.schedule(new TimerTask() {
	        public void run() {
	        	for (String ip : ips) {
	        		System.out.println("Hora de requisitar a escolha do usuário no ip: "+ip);
	        		SendOptions send = new SendOptions(ip, "Let him in", "Leave him behind");
	        		send.start();
	        	}
	        	//Se nao tiverem tocando o antigo CurrentItem...
	        	if(!CurrentPlaying()){
	        		//Assinale-o como livre...
	        		freeItemIndex = CurrentItemIndex;
	        		freeItem = currentItem;
	        		//Obtenha o Item que esta tocando atualmente no canal...
	        		CurrentItemIndex = server.getCurrentItemIndex();
	        		currentItem = server.getCurrentItem();
	        		//E informar que o arquivo esta livre apenas se o programa tiver mais de 1 item
	        		if(server.getItemSize()>0){
//	        			PlaylistController.removeVideo(server.getName(), freeItem.getName());
	        			//remover apenas da memoria
	        			server.removeItem(freeItemIndex);
	        		}
	        		//Me agende novamente
	        		goTimer();
	        	}
//	        	else {
//	        		//Se nao achou busque o next item em menos tempo
//	        		goTimerGranulate();
//	        	}
	        }
	    },nextTime());
	}
	/**
	 * Seek new currentItem in a shorter interval of time. 
	 * The reason for its existence is the synchronicity 
	 * of time, because the function nextTime was shot and 
	 * could not reach on time the next list item.
	 * 
	 */
	public void goTimerGranulate(){
		timer.schedule(new TimerTask() {
	        public void run() {
	        	System.out.println("GOTIMERGRANULATE SENDO CHAMADO");
	        	//Se nao tiverem tocando o antigo CurrentItem...
	        	if(!CurrentPlaying()){
	        		//Assinale-o como livre...
	        		//freeItemIndex = CurrentItemIndex;
	        		//freeItem = currentItem;
	        		//Obtenha o Item que esta tocando atualmente no canal...
	        		//CurrentItemIndex = server.getCurrentItemIndex();
	        		//currentItem = server.getCurrentItem();
	        		goTimer();
	        		this.cancel();
	        	}

	        }
	    },delay,period);
	}

	/**
	 * returns item played and free to remove
	 * 
	 * @return int
	 */
	public int getFreeItemIndex() {
		return freeItemIndex;
	}
	
	/**
	 * Set index of played item
	 * 
	 * @param freeItemIndex
	 */
	public void setFreeItemIndex(int freeItemIndex) {
		this.freeItemIndex = freeItemIndex;
	}
}
