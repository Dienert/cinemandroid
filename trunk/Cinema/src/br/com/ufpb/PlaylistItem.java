/**
 * Represents the PlayItem of Red5 but with properts of DateTime. 
 */
package br.com.ufpb;

import java.io.File;
import java.util.Date;

public class PlaylistItem {

	private File file;
	private Date timeBegin;
	private Date timeEnd;
	
	/**
	 * retrieves file
	 * @return
	 */
	public File getFile() {
		return file;
	}
	
	/**
	 * Set File
	 * @param file
	 */
	public void setFile(File file) {
		this.file = file;
	}
	/**
	 * Retrieves start dateTime to play item
	 * @return DateTime
	 */
	public Date getTimeBegin() {
		return timeBegin;
	}
	/**
	 * Set start dateTime to play item
	 * @param timeBegin
	 */
	public void setTimeBegin(Date timeBegin) {
		this.timeBegin = timeBegin;
	}
	/**
	 * retrieves end datetime to stop trasmition
	 * @return DateTime
	 */
	public Date getTimeEnd() {
		return timeEnd;
	}
	/**
	 * Set end dateTime to stop trasmition
	 * @param timeEnd
	 */
	public void setTimeEnd(Date timeEnd) {
		this.timeEnd = timeEnd;
	}
	
	
	
}
