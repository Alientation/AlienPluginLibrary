package me.alientation.doomboheadplugin.customcommand.exceptions;

/**
 * Method was not found
 */
public class InvalidMethodException extends RuntimeException{
	private static final long serialVersionUID = 1671192770414143872L;
	
	public InvalidMethodException(String errorMessage) {
		super(errorMessage);
	}
	
	public InvalidMethodException(Throwable err) {
		super(err);
	}
	
	public InvalidMethodException(String errorMessage, Throwable err) {
		super(errorMessage, err);
	}

}
