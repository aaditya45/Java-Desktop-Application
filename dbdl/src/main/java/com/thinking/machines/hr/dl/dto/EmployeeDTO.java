package com.thinking.machines.hr.dl.dto;
import com.thinking.machines.enums.*;//enum GENDER
import com.thinking.machines.hr.dl.interfaces.dto.*;
import java.util.*;
import java.math.*;
public class EmployeeDTO implements EmployeeDTOInterface{
	private String EmployeeId;
	private String name;
	private int DesignationCode;
	private Date DateOfBirth;
	private char Gender;
	private boolean IsIndian;
	private BigDecimal BasicSalary;
	private String PANNumber;
	private String AadharCardNumber;
	public EmployeeDTO(){
		this.name = "";
		this.DesignationCode = 0;
		this.DateOfBirth = null;
		this.Gender = ' ';
		this.IsIndian = true;
		this.BasicSalary = null;
		this.PANNumber = "";
		this.AadharCardNumber = "";
	}
	//setter getters
	public void setEmployeeId(String id){
		this.EmployeeId = id;
	}
	public String getEmployeeId(){
		return this.EmployeeId;
	}
	public void setName(String name){
		this.name =name;
	}
	public String getName(){
		return this.name;
	}
	public void setDesignationCode(int DesignationCode){
		this.DesignationCode = DesignationCode;
	}
	public int getDesignationCode(){
		return this.DesignationCode;
	}
	public void setDateOfBirth(Date DOB){
		this.DateOfBirth = DOB;
	}
	public Date getDateOfBirth(){
		return this.DateOfBirth;
	}
	public void setGender(GENDER gender){
		if(gender == GENDER.MALE) this.Gender = 'M';
		else this.Gender = 'F';
	}
	public char getGender(){
		return this.Gender;
	}
	public void setIsIndian(boolean isIndian){
		this.IsIndian = isIndian;
	}
	public boolean getIsIndian(){
		return this.IsIndian;
	}
	public void setBasicSalary(BigDecimal salary){
		this.BasicSalary = salary;
	}
	public BigDecimal getBasicSalary(){
		return this.BasicSalary;
	}
	public void setPANNumber(String panNumber){
		this.PANNumber = panNumber;
	}
	public String getPANNumber(){
		return this.PANNumber;
	}
	public void setAadharCardNumber(String aadharCardNumber){
		this.AadharCardNumber = aadharCardNumber;
	}
	public String getAadharCardNumber(){
		return this.AadharCardNumber;
	}
	public boolean equals(Object other){
		if(!(other instanceof EmployeeDTOInterface)) return false;
		EmployeeDTOInterface edtoi = (EmployeeDTOInterface) other;
		return this.EmployeeId.equalsIgnoreCase(edtoi.getEmployeeId());
	}
	public int compareTo(EmployeeDTOInterface edtoi){
		return this.EmployeeId.compareToIgnoreCase(edtoi.getEmployeeId());
	}
	public int hashCode(){
		return this.EmployeeId.toUpperCase().hashCode();
	}
	
}



