package com.thinking.machines.hr.dl.interfaces.dao;
import java.util.*;
import com.thinking.machines.hr.dl.interfaces.dto.*;
import com.thinking.machines.hr.dl.exceptions.*;
public interface EmployeeDAOInterface{
	public void add(EmployeeDTOInterface edtoi) throws DAOException;
	public void update(EmployeeDTOInterface edtoi) throws DAOException;
	public void delete(String empID) throws DAOException;
	public boolean isDesignationAlloted(int designationCode) throws DAOException;
	public Set<EmployeeDTOInterface> getAll() throws DAOException;
	public Set<EmployeeDTOInterface> getByDesignationCode(int designationCode) throws DAOException;
	public EmployeeDTOInterface getByEmployeeId(String empID) throws DAOException;
	public EmployeeDTOInterface getByPANNumber(String name) throws DAOException;
	public EmployeeDTOInterface getByAadharCardNumber(String title) throws DAOException;
	public boolean employeeIdExists(String empID) throws DAOException;
	public boolean panCardNumberExists(String panCardNumber) throws DAOException;
	public boolean aadharCardNumberExists(String aadharCardNumber) throws DAOException;
	public int getCount() throws DAOException;
	public int getCountByDesignation(int designationCode) throws DAOException;
}