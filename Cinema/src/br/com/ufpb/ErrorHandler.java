package br.com.ufpb;

import java.io.Serializable;

public interface ErrorHandler extends Serializable {

	/**
	 * Informs an error code for a job
	 * @param code
	 */
	public void setErrorCode(int code);
	
	/**
	 * Retrieves a previous set error code
	 * @return the error code for a given job
	 */
	public int getErrorCode();
	
	/**
	 * Error output for a given job
	 * @param message String with the error message
	 */
	public void error(String message);
	
	/**
	 * Error output for a given job that returned with a fatal error
	 * @param message String with the error message
	 */
	public void fatalError(String message);
	
	/**
	 * The default output for a given job
	 * @param message String with the warning messages
	 */
	public void info(String message);
	
	/**
	 * This methods notifies about a successfully finished job
	 */
	public void success();
}