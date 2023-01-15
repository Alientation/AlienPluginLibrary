package me.alientation.doomboheadplugin.customgui.exceptions;

public class UnflaggedParameterException extends RuntimeException {
	private static final long serialVersionUID = -1990638435662955623L;

	public UnflaggedParameterException() {
		
	}
	
	public UnflaggedParameterException(String errorMessage) {
		super(errorMessage);
	}
	
	public UnflaggedParameterException(Throwable err) {
		super(err);
	}
	
	public UnflaggedParameterException(String errorMessage, Throwable err) {
		super(errorMessage, err);
	}
}
