package me.alientation.doomboheadplugin.customcommand.exceptions;

/**
 * Plugin was not initialized or correct
 */
public class PluginNotFoundException extends RuntimeException{
	private static final long serialVersionUID = -1511850485557876180L;
	
	public PluginNotFoundException(String errorMessage) {
		super(errorMessage);
	}
	
	public PluginNotFoundException(Throwable err) {
		super(err);
	}
	
	public PluginNotFoundException(String errorMessage, Throwable err) {
		super(errorMessage, err);
	}
}
