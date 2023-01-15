package me.alientation.doomboheadplugin.customgui.exceptions;

public class UnknownManagerException extends RuntimeException{
	private static final long serialVersionUID = 2266754301847726459L;
	
	public UnknownManagerException() {
		
	}
	
	public UnknownManagerException(String errorMessage) {
		super(errorMessage);
	}
	
	public UnknownManagerException(Throwable err) {
		super(err);
	}
	
	public UnknownManagerException(String errorMessage, Throwable err) {
		super(errorMessage,err);
	}

}
