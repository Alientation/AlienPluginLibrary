package me.alientation.doomboheadplugin.customgui.exceptions;

public class UnknownGUIException extends RuntimeException {
	private static final long serialVersionUID = 3076161386644643289L;
	
	public UnknownGUIException() {
		
	}
	
	public UnknownGUIException(String errorMessage) {
		super(errorMessage);
	}
	
	public UnknownGUIException(Throwable err) {
		super(err);
	}
	
	public UnknownGUIException(String errorMessage, Throwable err) {
		super(errorMessage, err);
	}
}
