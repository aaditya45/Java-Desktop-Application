package com.thinking.machines.hr.dl.interfaces.dto;
public interface DesignationDTOInterface extends Comparable<DesignationDTOInterface>, java.io.Serializable{
	void setCode(int code);
	int getCode();
	void setTitle(String title);
	String getTitle();
}