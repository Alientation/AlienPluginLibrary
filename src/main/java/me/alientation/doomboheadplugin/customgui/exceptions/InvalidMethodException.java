package me.alientation.doomboheadplugin.customgui.exceptions;

public class InvalidMethodException extends RuntimeException {
	private static final long serialVersionUID = -8659086011099650988L;

	public InvalidMethodException() {
		
	}
	
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
