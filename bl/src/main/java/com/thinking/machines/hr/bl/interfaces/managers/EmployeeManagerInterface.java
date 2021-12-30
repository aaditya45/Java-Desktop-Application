package com.thinking.machines.hr.bl.interfaces.managers;
import com.thinking.machines.hr.bl.exceptions.*;
import com.thinking.machines.hr.bl.interfaces.pojo.*;
import java.util.*;
public interface EmployeeManagerInterface{
	public void addEmployee(EmployeeInterface employee) throws BLException;
	public void updateEmployee(EmployeeInterface employee) throws BLException;
	public void removeEmployee(String EmployeeId) throws BLException;
	public EmployeeInterface getEmployeeById(String EmployeeId) throws BLException;
	public EmployeeInterface getEmployeeByAadharCardNumber(String aadharCardNumber) throws BLException;
	public EmployeeInterface getEmployeeByPANNumber(String panNumber) throws BLException;
	public int getEmployeeCount() throws BLException;
	public boolean employeeIdExists(String employeeId) throws BLException;
	public boolean employeePANNumberExists(String panNumber) throws BLException;
	public boolean employeeAadharCardNumberExists(String aadharCardNumber) throws BLException;
	public Set<EmployeeInterface> getEmployees() throws BLException;
	public Set<EmployeeInterface> getEmployeesByDesignationCode(int code) throws BLException;
	public boolean DesignationAlloted(int designationCode) throws BLException;
}