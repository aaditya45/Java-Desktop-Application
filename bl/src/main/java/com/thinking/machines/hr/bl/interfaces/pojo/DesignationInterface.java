package com.thinking.machines.hr.bl.interfaces.pojo;
import com.thinking.machines.hr.bl.exceptions.*;
import java.util.*;
public interface DesignationInterface extends Comparable<DesignationInterface>, java.io.Serializable{
	public void setCode(int code);
	public int getCode();
	public void setTitle(String title);
	public String getTitle();
}