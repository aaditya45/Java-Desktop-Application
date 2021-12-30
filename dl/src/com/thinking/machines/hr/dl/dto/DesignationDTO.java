package com.thinking.machines.hr.dl.dto;
import com.thinking.machines.hr.dl.interfaces.dto.*;
public class DesignationDTO implements DesignationDTOInterface{
	private String title;
	private int code;
	public void setCode(int code){
		this.code = code;
	}
	public int getCode(){
		return this.code;
	}	
	public void setTitle(String title){
		this.title = title;
	}
	public String getTitle(){
		return this.title;
	}
	public boolean equals(Object other){
		if(!(other instanceof DesignationDTO)) return false;
		DesignationDTO ddto = (DesignationDTO) other;
		return this.code == ddto.code;
	}
	public int compareTo(DesignationDTOInterface ddto){
		return this.code - ddto.getCode();
	}
	public int hashCode(){
		return this.code;
	}
}