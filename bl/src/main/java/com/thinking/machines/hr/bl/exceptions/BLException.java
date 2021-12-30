package com.thinking.machines.hr.bl.exceptions;
import java.util.*;
public class BLException extends Exception{
	private Map<String, String> exceptions;
	private String genericException;
	public BLException(){
		genericException=null;
		exceptions = new HashMap<>();
	}
	public void setGenericException(String genericException){
		this.genericException = genericException;
	}
	public String getGenericException(){
		if(this.genericException==null) return null;
		return genericException;
	}
	public void addException(String property,String exception){
		this.exceptions.put(property,exception);
	}
	public String getException(String property){
		 return this.exceptions.get(property);
	}
	public void removeException(String property){
		this.exceptions.remove(property);
	}
	public int getExceptionCount(){
		return this.exceptions.size();
	}
	public boolean hasGenericException(){
		return this.genericException!=null;
	}
	public boolean hasExceptions(){
		 return getExceptionCount() > 0;
	}
	public List<String> getProperties(){
		List<String> properties = new ArrayList<>();
		this.exceptions.forEach((k,v)->{
			properties.add(k);
		});
		return properties;
	}
	public String getMessage(){
		if(this.genericException== null) return "";
		else return this.genericException;
	}
	
	
	
}