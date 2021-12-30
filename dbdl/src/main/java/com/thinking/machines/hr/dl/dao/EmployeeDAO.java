package com.thinking.machines.hr.dl.dao;
import java.util.*;

import javax.xml.parsers.ParserConfigurationException;

import java.math.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.text.*;
import java.io.*;
import java.sql.*;
import com.thinking.machines.hr.dl.dto.*;
import com.thinking.machines.hr.dl.interfaces.dto.*;
import com.thinking.machines.hr.dl.interfaces.dao.*;
import com.thinking.machines.hr.dl.exceptions.*;
import com.thinking.machines.enums.*;
public class EmployeeDAO implements EmployeeDAOInterface{
	private static final String FILE_NAME = "employee.data";
	private static final String TEMP_NAME = "tmp.data";
	
	//ADD EMPLOYEE DATA
	public void add(EmployeeDTOInterface edtoi) throws DAOException{
		if(edtoi == null) throw new DAOException("Employee object is null.");
		if(edtoi.getName() == null || edtoi.getName().length() == 0) throw new DAOException("Please Enter Title.");
		if(edtoi.getDateOfBirth() == null) throw new DAOException("Please Fill All the slots.");
		if(edtoi.getBasicSalary() == null) throw new DAOException("Please Fill All the slots.");
		if(edtoi.getBasicSalary().signum() <0) throw new DAOException("Please fill slots properly.");
		if(edtoi.getPANNumber() == null || edtoi.getPANNumber().length()==0) throw new DAOException("Please Fill All the slots.");
		if(edtoi.getPANNumber().length()==0) throw new DAOException("Please fill PAN Number poperly.");
		if(edtoi.getAadharCardNumber() == null || edtoi.getAadharCardNumber().length()==0) throw new DAOException("Please Fill All the slots.");
		if(edtoi.getAadharCardNumber().length()==0 ) throw new DAOException("Please fill Aadhar Card Number Properly.");
		Connection connection = null;
		PreparedStatement preparedStatement;
		ResultSet resultSet;
		try {
			connection = ConnectionDAO.getConnection();
			preparedStatement = connection.prepareStatement("select code from designation where code=?;");
			preparedStatement.setInt(1,edtoi.getDesignationCode());
			resultSet = preparedStatement.executeQuery();
			if(resultSet.next()==false){
				resultSet.close();
				preparedStatement.close();
				connection.close();
				throw new DAOException("Invalid designation code :"+edtoi.getDesignationCode());
			}
			resultSet.close();
			preparedStatement.close();
		} catch (SQLException e) {
			throw new DAOException(e.getMessage());
		}
		int designationCode = edtoi.getDesignationCode();
		char gender = edtoi.getGender();
		boolean isIndian = edtoi.getIsIndian();
		String name = edtoi.getName();
		java.util.Date dateOfBirth = edtoi.getDateOfBirth();
		DesignationDAOInterface ddaoi = new DesignationDAO();
		BigDecimal basicSalary = edtoi.getBasicSalary();
		String panNumber = edtoi.getPANNumber();
		String aadharCardNumber = edtoi.getAadharCardNumber();
		try{
			connection = ConnectionDAO.getConnection();
			boolean panNumberExists=false, aadharCardNumberExists=false;
			preparedStatement = connection.prepareStatement("select gender from employee where pan_number=?;");
			preparedStatement.setString(1,panNumber);
			resultSet = preparedStatement.executeQuery();
			panNumberExists = resultSet.next();
			resultSet.close();
			preparedStatement.close();
			preparedStatement = connection.prepareStatement("select gender from employee where aadhar_card_number=?;");
			preparedStatement.setString(1,aadharCardNumber);
			resultSet = preparedStatement.executeQuery();
			aadharCardNumberExists = resultSet.next();
			resultSet.close();
			preparedStatement.close();
			if(panNumberExists && aadharCardNumberExists){
				try {
					connection.close();
				} catch (SQLException e) {
					throw new DAOException(e.getMessage());
				}
				throw new DAOException("PAN Card number "+panNumber+" and Aadhar Card Number "+aadharCardNumber+" already exists.");
			}
			if(panNumberExists){
				try {
					connection.close();
				} catch (SQLException e) {
					throw new DAOException(e.getMessage());
				}
				throw new DAOException("PAN Number "+panNumber+" already exists.");
			}
			if(aadharCardNumberExists){
				try {
					connection.close();
				} catch (SQLException e) {
					throw new DAOException(e.getMessage());
				}
				throw new DAOException("Aadhar card Number "+aadharCardNumber+" already exists.");
			}
			preparedStatement = connection.prepareStatement("insert into employee (name,designation_code,gender,date_of_birth,is_indian,basic_salary,pan_number,aadhar_card_number) values (?,?,?,?,?,?,?,?);",Statement.RETURN_GENERATED_KEYS);
			preparedStatement.setString(1, name);
			preparedStatement.setInt(2,designationCode);
			preparedStatement.setString(3,String.valueOf(gender));
			java.sql.Date sqlDateOfBirth  = new java.sql.Date(dateOfBirth.getYear(),dateOfBirth.getMonth(),dateOfBirth.getDate());
			preparedStatement.setDate(4, sqlDateOfBirth);
			preparedStatement.setBoolean(5,isIndian);
			preparedStatement.setBigDecimal(6,basicSalary);
			preparedStatement.setString(7, panNumber);
			preparedStatement.setString(8,aadharCardNumber);
			preparedStatement.executeUpdate();
			resultSet = preparedStatement.getGeneratedKeys();
			resultSet.next();
			int generatedEmployeeId  = resultSet.getInt(1);
			resultSet.close();
			preparedStatement.close();
			connection.close();
			edtoi.setEmployeeId("A"+(10000000+generatedEmployeeId));
		}catch(SQLException daoe){
			throw new DAOException(daoe.getMessage());
		}
	}
	
	//UPDATE EMPLOYEE DATA
	public void update(EmployeeDTOInterface edtoi) throws DAOException{
		if(edtoi == null) throw new DAOException("Employee object is null.");
		if(edtoi.getEmployeeId().trim().length()<=0 || edtoi.getEmployeeId() == null) throw new DAOException("Invalid Employee ID.");
		if(edtoi.getName() == null || edtoi.getName().length() == 0) throw new DAOException("Please Enter Title.");
		if(edtoi.getDateOfBirth() == null) throw new DAOException("Please Fill All the slots.");
		if(edtoi.getBasicSalary() == null) throw new DAOException("Please Fill All the slots.");
		if(edtoi.getBasicSalary().signum() <0) throw new DAOException("Please fill slots properly.");
		if(edtoi.getPANNumber() == null || edtoi.getPANNumber().length()==0) throw new DAOException("Please Fill All the slots.");
		if(edtoi.getPANNumber().length()==0) throw new DAOException("Please fill PAN Number poperly.");
		if(edtoi.getAadharCardNumber() == null || edtoi.getAadharCardNumber().length()==0) throw new DAOException("Please Fill All the slots.");
		if(edtoi.getAadharCardNumber().length()==0 ) throw new DAOException("Please fill Aadhar Card Number Properly.");
		int actualEmployeeId = Integer.parseInt(edtoi.getEmployeeId().substring(1))-10000000;
		Connection connection = null;
		PreparedStatement preparedStatement;
		ResultSet resultSet;
		try {
			connection = ConnectionDAO.getConnection();
			preparedStatement = connection.prepareStatement("select gender from employee where employee_id=?;");
			preparedStatement.setInt(1,actualEmployeeId);
			resultSet = preparedStatement.executeQuery();
			if(resultSet.next()==false){
				resultSet.close();
				preparedStatement.close();
				connection.close();
				throw new DAOException("Invalid Employee ID :"+edtoi.getEmployeeId().trim());
			}
			resultSet.close();
			preparedStatement.close();
		} catch (Exception e) {
			//TODO: handle exception
		}
		try {
			preparedStatement = connection.prepareStatement("select code from designation where code=?;");
			preparedStatement.setInt(1,edtoi.getDesignationCode());
			resultSet = preparedStatement.executeQuery();
			if(resultSet.next()==false){
				resultSet.close();
				preparedStatement.close();
				connection.close();
				throw new DAOException("Invalid designation code :"+edtoi.getDesignationCode());
			}
			resultSet.close();
			preparedStatement.close();
		} catch (SQLException e) {
			throw new DAOException(e.getMessage());
		}
		int designationCode = edtoi.getDesignationCode();
		char gender = edtoi.getGender();
		boolean isIndian = edtoi.getIsIndian();
		String name = edtoi.getName();
		java.util.Date dateOfBirth = edtoi.getDateOfBirth();
		DesignationDAOInterface ddaoi = new DesignationDAO();
		BigDecimal basicSalary = edtoi.getBasicSalary();
		String panNumber = edtoi.getPANNumber();
		String aadharCardNumber = edtoi.getAadharCardNumber();
		try{
			connection = ConnectionDAO.getConnection();
			boolean panNumberExists=false, aadharCardNumberExists=false;
			preparedStatement = connection.prepareStatement("select gender from employee where pan_number=? and employee_id<>?;");
			preparedStatement.setString(1,panNumber);
			preparedStatement.setInt(2,actualEmployeeId);
			resultSet = preparedStatement.executeQuery();
			panNumberExists = resultSet.next();
			resultSet.close();
			preparedStatement.close();
			preparedStatement = connection.prepareStatement("select gender from employee where aadhar_card_number=? and employee_id<>?;");
			preparedStatement.setString(1,aadharCardNumber);
			preparedStatement.setInt(2,actualEmployeeId);
			resultSet = preparedStatement.executeQuery();
			aadharCardNumberExists = resultSet.next();
			resultSet.close();
			preparedStatement.close();
			if(panNumberExists && aadharCardNumberExists){
				try {
					connection.close();
				} catch (SQLException e) {
					throw new DAOException(e.getMessage());
				}
				throw new DAOException("PAN Card number "+panNumber+" and Aadhar Card Number "+aadharCardNumber+" already exists.");
			}
			if(panNumberExists){
				try {
					connection.close();
				} catch (SQLException e) {
					throw new DAOException(e.getMessage());
				}
				throw new DAOException("PAN Number "+panNumber+" already exists.");
			}
			if(aadharCardNumberExists){
				try {
					connection.close();
				} catch (SQLException e) {
					throw new DAOException(e.getMessage());
				}
				throw new DAOException("Aadhar card Number "+aadharCardNumber+" already exists.");
			}
			preparedStatement = connection.prepareStatement("update employee set name=?, designation_code=?,gender=?,date_of_birth=?,is_indian=?,basic_salary=?,pan_number=?,aadhar_card_number=? where employee_id=?;");
			preparedStatement.setString(1, name);
			preparedStatement.setInt(2,designationCode);
			preparedStatement.setString(3,String.valueOf(gender));
			java.sql.Date sqlDateOfBirth  = new java.sql.Date(dateOfBirth.getYear(),dateOfBirth.getMonth(),dateOfBirth.getDate());
			preparedStatement.setDate(4, sqlDateOfBirth);
			preparedStatement.setBoolean(5,isIndian);
			preparedStatement.setBigDecimal(6,basicSalary);
			preparedStatement.setString(7, panNumber);
			preparedStatement.setString(8,aadharCardNumber);
			preparedStatement.setInt(9,actualEmployeeId);
			preparedStatement.executeUpdate();
			resultSet.close();
			preparedStatement.close();
			connection.close();
		}catch(SQLException daoe){
			throw new DAOException(daoe.getMessage());
		}
	}
	
	//DELETE METHOD 
	public void delete(String empID) throws DAOException{
		if(empID ==null) throw new DAOException("Invalid ID");
		if(empID.trim().length()==0) throw new DAOException("Please Enter the Employee ID.");
		Connection connection;
		PreparedStatement preparedStatement;
		ResultSet resultSet;
		int actualEmployeeId = Integer.parseInt(empID.trim().substring(1))-10000000;
		if(actualEmployeeId<=0){
			throw new DAOException("Invalid Employee ID");
		}
		try {
			connection = ConnectionDAO.getConnection();
			preparedStatement = connection.prepareStatement("select gender from employee where employee_id=?;");
			preparedStatement.setInt(1, actualEmployeeId);
			resultSet = preparedStatement.executeQuery();
			if(!resultSet.next()){
				resultSet.close();
				preparedStatement.close();
				connection.close();
				throw new DAOException("Employye id does not exists.");
			}
			resultSet.close();
			preparedStatement.close();
			preparedStatement = connection.prepareStatement("delete from employee where employee_id=?;");
			preparedStatement.setInt(1,actualEmployeeId);
			preparedStatement.executeUpdate();
			resultSet.close();
			preparedStatement.close();
			connection.close();
		} catch (SQLException e) {
			//TODO: handle exception
		}
	}

	//IS DESIGNATION ALLOTED
	public boolean isDesignationAlloted(int designationCode) throws DAOException{
		if(getByDesignationCode(designationCode)==null){
			return false;
		}
		return true;
	}
	
	//RETURN ALL THE EMPLOYEE DATA
	public Set<EmployeeDTOInterface> getAll() throws DAOException{
		Set<EmployeeDTOInterface> employees = new TreeSet<>();
		try {
			Connection connection = ConnectionDAO.getConnection();
			PreparedStatement preparedStatement = connection.prepareStatement("select * from employee;");
			ResultSet resultSet = preparedStatement.executeQuery();
			EmployeeDTOInterface edtoi;
			java.util.Date normalDate;
			java.sql.Date sqlDate;
			String fGender;
			while(resultSet.next()){
				edtoi = new EmployeeDTO();
				edtoi.setEmployeeId("A"+String.valueOf(10000000+resultSet.getInt("employee_id")));
				edtoi.setName(resultSet.getString("name"));
				edtoi.setDesignationCode(resultSet.getInt("designation_code"));
				sqlDate = resultSet.getDate("date_of_birth");
				normalDate = new java.util.Date(sqlDate.getYear(),sqlDate.getMonth(),sqlDate.getDate());
				fGender = resultSet.getString("gender");
				if(fGender == "M"){
					edtoi.setGender(GENDER.MALE);
				}else if(fGender == "F"){
					edtoi.setGender(GENDER.FEMALE);
				}
				edtoi.setIsIndian(resultSet.getBoolean("is_indian"));
				edtoi.setBasicSalary(resultSet.getBigDecimal("basic_salary"));
				edtoi.setPANNumber(resultSet.getString("pan_number"));
				edtoi.setAadharCardNumber(resultSet.getString("aadhar_card_number"));
				employees.add(edtoi);
			}
			resultSet.close();
			preparedStatement.close();
			connection.close();
		} catch (SQLException e) {
			throw new DAOException(e.getMessage());
		}
		return employees;
	}
	
	//GET EMPLOYEE DATA BY DESIGNATION
	public Set<EmployeeDTOInterface> getByDesignationCode(int designationCode) throws DAOException{
		Set<EmployeeDTOInterface> employees = new TreeSet<>();
		try {
			Connection connection = ConnectionDAO.getConnection();
			PreparedStatement preparedStatement = connection.prepareStatement("select * from employee where designation_code=?;");
			preparedStatement.setInt(1,designationCode);
			ResultSet resultSet = preparedStatement.executeQuery();
			EmployeeDTOInterface edtoi;
			java.util.Date normalDate;
			java.sql.Date sqlDate;
			String fGender;
			while(resultSet.next()){
				edtoi = new EmployeeDTO();
				edtoi.setEmployeeId("A"+String.valueOf(10000000+resultSet.getInt("employee_id")));
				edtoi.setName(resultSet.getString("name"));
				edtoi.setDesignationCode(resultSet.getInt("designation_code"));
				sqlDate = resultSet.getDate("date_of_birth");
				normalDate = new java.util.Date(sqlDate.getYear(),sqlDate.getMonth(),sqlDate.getDate());
				fGender = resultSet.getString("gender");
				if(fGender == "M"){
					edtoi.setGender(GENDER.MALE);
				}else if(fGender == "F"){
					edtoi.setGender(GENDER.FEMALE);
				}
				edtoi.setIsIndian(resultSet.getBoolean("is_indian"));
				edtoi.setBasicSalary(resultSet.getBigDecimal("basic_salary"));
				edtoi.setPANNumber(resultSet.getString("pan_number"));
				edtoi.setAadharCardNumber(resultSet.getString("aadhar_card_number"));
				employees.add(edtoi);
			}
			resultSet.close();
			preparedStatement.close();
			connection.close();
		} catch (SQLException e) {
			throw new DAOException(e.getMessage());
		}
		return employees;
	}
	
	//GET EMPLOYEE DATA BY ID
	public EmployeeDTOInterface getByEmployeeId(String empID) throws DAOException{
		if(empID == null || empID.trim().length() <=0) throw new DAOException("Invalid Input.");
		EmployeeDTOInterface edtoi = null;
		int actualEmployeeId = Integer.parseInt(empID.trim().substring(1))-10000000;
		try {
			Connection connection = ConnectionDAO.getConnection();
			PreparedStatement preparedStatement = connection.prepareStatement("select gender from employee where employee_id=?;");
			preparedStatement.setInt(1,actualEmployeeId);
			ResultSet resultSet = preparedStatement.executeQuery();
			if(resultSet.next()==false){
				resultSet.close();
				preparedStatement.close();
				connection.close();
				throw new DAOException("Employee Id does not exist.");
			}
			resultSet.close();
			preparedStatement.close();
			preparedStatement = connection.prepareStatement("select * from employee where employee_id=?;");
			preparedStatement.setInt(1,actualEmployeeId);
			resultSet = preparedStatement.executeQuery();
			java.util.Date normalDate;
			java.sql.Date sqlDate;
			String fGender;
			while(resultSet.next()){
				edtoi = new EmployeeDTO();
				edtoi.setEmployeeId("A"+String.valueOf(10000000+resultSet.getInt("employee_id")));
				edtoi.setName(resultSet.getString("name"));
				edtoi.setDesignationCode(resultSet.getInt("designation_code"));
				sqlDate = resultSet.getDate("date_of_birth");
				normalDate = new java.util.Date(sqlDate.getYear(),sqlDate.getMonth(),sqlDate.getDate());
				fGender = resultSet.getString("gender");
				if(fGender == "M"){
					edtoi.setGender(GENDER.MALE);
				}else if(fGender == "F"){
					edtoi.setGender(GENDER.FEMALE);
				}
				edtoi.setIsIndian(resultSet.getBoolean("is_indian"));
				edtoi.setBasicSalary(resultSet.getBigDecimal("basic_salary"));
				edtoi.setPANNumber(resultSet.getString("pan_number"));
				edtoi.setAadharCardNumber(resultSet.getString("aadhar_card_number"));
			}
			resultSet.close();
			preparedStatement.close();
			connection.close();
		} catch (SQLException e) {
			throw new DAOException(e.getMessage());
		}
		return edtoi;
	}
	public EmployeeDTOInterface getByPANNumber(String panNumber) throws DAOException{
		if(panNumber==null || panNumber.trim().length() <=0) throw new DAOException("Invalid PAN Card Number.");
		EmployeeDTOInterface edtoi = null;
		try {
			Connection connection = ConnectionDAO.getConnection();
			PreparedStatement preparedStatement = connection.prepareStatement("select gender from employee where pan_number=?;");
			preparedStatement.setString(1,panNumber.trim());
			ResultSet resultSet = preparedStatement.executeQuery();
			if(resultSet.next()==false){
				resultSet.close();
				preparedStatement.close();
				connection.close();
				throw new DAOException("Pan number does not exist.");
			}
			resultSet.close();
			preparedStatement.close();
			preparedStatement = connection.prepareStatement("select * from employee where pan_number=?;");
			preparedStatement.setString(1,panNumber.trim());
			resultSet = preparedStatement.executeQuery();
			java.util.Date normalDate;
			java.sql.Date sqlDate;
			String fGender;
			while(resultSet.next()){
				edtoi = new EmployeeDTO();
				edtoi.setEmployeeId("A"+String.valueOf(10000000+resultSet.getInt("employee_id")));
				edtoi.setName(resultSet.getString("name"));
				edtoi.setDesignationCode(resultSet.getInt("designation_code"));
				sqlDate = resultSet.getDate("date_of_birth");
				normalDate = new java.util.Date(sqlDate.getYear(),sqlDate.getMonth(),sqlDate.getDate());
				fGender = resultSet.getString("gender");
				if(fGender == "M"){
					edtoi.setGender(GENDER.MALE);
				}else if(fGender == "F"){
					edtoi.setGender(GENDER.FEMALE);
				}
				edtoi.setIsIndian(resultSet.getBoolean("is_indian"));
				edtoi.setBasicSalary(resultSet.getBigDecimal("basic_salary"));
				edtoi.setPANNumber(resultSet.getString("pan_number"));
				edtoi.setAadharCardNumber(resultSet.getString("aadhar_card_number"));
			}
			resultSet.close();
			preparedStatement.close();
			connection.close();
		} catch (SQLException e) {
			throw new DAOException(e.getMessage());
		}
		return edtoi;
	}
	public EmployeeDTOInterface getByAadharCardNumber(String aadharCardNumber) throws DAOException{
		if(aadharCardNumber==null || aadharCardNumber.trim().length() <=0) throw new DAOException("Invalid PAN Card Number.");
		EmployeeDTOInterface edtoi = null;
		try {
			Connection connection = ConnectionDAO.getConnection();
			PreparedStatement preparedStatement = connection.prepareStatement("select gender from employee where aadhar_card_number=?;");
			preparedStatement.setString(1,aadharCardNumber.trim());
			ResultSet resultSet = preparedStatement.executeQuery();
			if(resultSet.next()==false){
				resultSet.close();
				preparedStatement.close();
				connection.close();
				throw new DAOException("Aadhar Card Number does not exist.");
			}
			resultSet.close();
			preparedStatement.close();
			preparedStatement = connection.prepareStatement("select * from employee where aadhar_card_number=?;");
			preparedStatement.setString(1,aadharCardNumber.trim());
			resultSet = preparedStatement.executeQuery();
			java.util.Date normalDate;
			java.sql.Date sqlDate;
			String fGender;
			while(resultSet.next()){
				edtoi = new EmployeeDTO();
				edtoi.setEmployeeId("A"+String.valueOf(10000000+resultSet.getInt("employee_id")));
				edtoi.setName(resultSet.getString("name"));
				edtoi.setDesignationCode(resultSet.getInt("designation_code"));
				sqlDate = resultSet.getDate("date_of_birth");
				normalDate = new java.util.Date(sqlDate.getYear(),sqlDate.getMonth(),sqlDate.getDate());
				fGender = resultSet.getString("gender");
				if(fGender == "M"){
					edtoi.setGender(GENDER.MALE);
				}else if(fGender == "F"){
					edtoi.setGender(GENDER.FEMALE);
				}
				edtoi.setIsIndian(resultSet.getBoolean("is_indian"));
				edtoi.setBasicSalary(resultSet.getBigDecimal("basic_salary"));
				edtoi.setPANNumber(resultSet.getString("pan_number"));
				edtoi.setAadharCardNumber(resultSet.getString("aadhar_card_number"));
			}
			resultSet.close();
			preparedStatement.close();
			connection.close();
		} catch (SQLException e) {
			throw new DAOException(e.getMessage());
		}
		return edtoi;
	}
	public boolean employeeIdExists(String empID) throws DAOException{
		try {
			this.getByEmployeeId(empID);
		} catch (DAOException e) {
			return false;
		}
		return true;
	}
	public boolean panCardNumberExists(String panCardNumber) throws DAOException{
		try {
			this.getByPANNumber(panCardNumber);
		} catch (DAOException e) {
			return false;
		}
		return true;
	}
	public boolean aadharCardNumberExists(String aadharCardNumber) throws DAOException{
		try {
			this.getByAadharCardNumber(aadharCardNumber);
		} catch (DAOException e) {
			return false;
		}
		return true;
	}
	public int getCount() throws DAOException{
		try {
			Connection connection = ConnectionDAO.getConnection();
			PreparedStatement preparedStatement = connection.prepareStatement("select count(*) as cnt from employee;");
			ResultSet resultSet = preparedStatement.executeQuery();
			resultSet.next();
			int count ;
			count = resultSet.getInt("cnt");
			resultSet.close();
			preparedStatement.close();
			connection.close();
			return count;
		} catch (SQLException e) {
			throw new DAOException(e.getMessage());
		}
	}
	public int getCountByDesignation(int designationCode) throws DAOException{
		try {
			Connection connection = ConnectionDAO.getConnection();
			PreparedStatement preparedStatement = connection.prepareStatement("select code from designation where code=?;");
			preparedStatement.setInt(1,designationCode);
			ResultSet resultSet = preparedStatement.executeQuery();
			if(resultSet.next()==false){
				resultSet.close();
				preparedStatement.close();
				connection.close();
				throw new DAOException("Code Does not exists.");
			}
			resultSet.close();
			preparedStatement.close();
			preparedStatement = connection.prepareStatement("select count(*) as cnt from employee where designation_code=?;");
			preparedStatement.setInt(1, designationCode);
			resultSet = preparedStatement.executeQuery();
			resultSet.next();
			int count;
			count = resultSet.getInt("cnt");
			resultSet.close();
			preparedStatement.close();
			connection.close();
			return count;
		} catch (SQLException e) {
			throw new DAOException(e.getMessage());
		}
	}
}