package com.thinking.machines.hr.dl.dao;
import java.util.*;
import java.sql.*;
import com.thinking.machines.hr.dl.dto.*;
import com.thinking.machines.hr.dl.interfaces.dto.*;
import com.thinking.machines.hr.dl.interfaces.dao.*;
import com.thinking.machines.hr.dl.exceptions.*;
public class DesignationDAO implements DesignationDAOInterface{
	public void add(DesignationDTOInterface ddtoi) throws DAOException{
		int code = 0;
		if(ddtoi == null) throw new DAOException("Designation is null.");
		String title = ddtoi.getTitle();
		if(title ==null) throw new DAOException("Designation is null");
		title = title.trim();
		if(title.length()==0) throw new DAOException("Designation length is zero");
		try {
			Connection connection = ConnectionDAO.getConnection();
			PreparedStatement preparedStatement;
			preparedStatement = connection.prepareStatement("select code from designation where title=?;");
			preparedStatement.setString(1,title);
			ResultSet resultSet = preparedStatement.executeQuery();
			if(resultSet.next()){
				resultSet.close();
				preparedStatement.close();
				connection.close();
				throw new DAOException("Designation, "+title+" already exists.");
			}
			resultSet.close();
			preparedStatement.close();
			preparedStatement = connection.prepareStatement("insert into designation (title) values (?);",Statement.RETURN_GENERATED_KEYS);	
			preparedStatement.setString(1,title);
			preparedStatement.executeUpdate();//returns number of rows affected by this statement.
			resultSet = preparedStatement.getGeneratedKeys();
			resultSet.next();
			code = resultSet.getInt(1);
			resultSet.close();
			preparedStatement.close();
			connection.close();
			ddtoi.setCode(code);
		} catch (Exception e) {
			throw new DAOException(e.getMessage());
		}
	}
	
	//UPDATE DESIGNATION
	public void update(DesignationDTOInterface ddtoi) throws DAOException{
		if(ddtoi == null) throw new DAOException("Designation is null.");
		String title = ddtoi.getTitle();
		if(title ==null) throw new DAOException("Designation is null");
		title = title.trim();
		if(title.length()==0) throw new DAOException("Designation length is zero");
		int code = ddtoi.getCode();
		try {
			Connection connection = ConnectionDAO.getConnection();
			PreparedStatement preparedStatement;
			preparedStatement = connection.prepareStatement("select title from designation where title=?;");
			preparedStatement.setString(1,title);
			ResultSet resultSet = preparedStatement.executeQuery();
			if(resultSet.next()){
				resultSet.close();
				preparedStatement.close();
				connection.close();
				throw new DAOException("Designation, "+title+" already exists.");
			}
			resultSet.close();
			preparedStatement.close();
			preparedStatement = connection.prepareStatement("update designation set title=? where code=?;",Statement.RETURN_GENERATED_KEYS);	
			preparedStatement.setString(1,title);
			preparedStatement.setInt(2,code);
			preparedStatement.executeUpdate();//returns number of rows affected by this statement.
			preparedStatement.close();
			connection.close();
			ddtoi.setTitle(title);
		} catch (Exception e) {
			throw new DAOException(e.getMessage());
		}
	}
	
	//DELETE DESIGNATION
	public void delete(int code) throws DAOException{
		try {
			Connection connection = ConnectionDAO.getConnection();
			PreparedStatement preparedStatement;
			preparedStatement = connection.prepareStatement("select * from designation where code=?;");
			preparedStatement.setInt(1, code);
			ResultSet resultSet= preparedStatement.executeQuery();
			if(!resultSet.next()){
				resultSet.close();
				preparedStatement.close();
				connection.close();
				throw new DAOException("Code "+code+"does not exists.");
			}
			resultSet.close();
			preparedStatement.close();
			preparedStatement = connection.prepareStatement("select gender from employee where designation_code=?;");
			preparedStatement.setInt(1,code);
			resultSet = preparedStatement.executeQuery();
			if(resultSet.next()){
				resultSet.close();
				preparedStatement.close();
				connection.close();
				throw new DAOException("Cannot delete Designation as it is already assigned to employee(s).");
			}
			preparedStatement = connection.prepareStatement("delete from designation where code=?;");
			preparedStatement.setInt(1,code);
			preparedStatement.executeUpdate();
			preparedStatement.close();
			connection.close();
		} catch (Exception e) {
			throw new DAOException(e.getMessage());
		}
	}
	
	//GET ALL DESIGNATIONS
	public Set<DesignationDTOInterface> getAll() throws DAOException{
		Set<DesignationDTOInterface> designations;
		designations = new TreeSet<DesignationDTOInterface>();
		try{
			Connection connection = ConnectionDAO.getConnection();
			PreparedStatement preparedStatement;
			preparedStatement = connection.prepareStatement("select * from designation;");
			ResultSet resultSet = preparedStatement.executeQuery();
			DesignationDTOInterface designation;
			while(resultSet.next()){
				designation = new DesignationDTO();
				designation.setCode(resultSet.getInt("code"));
				designation.setTitle(resultSet.getString("title"));
				designations.add(designation);
			}
			resultSet.close();
			preparedStatement.close();
			connection.close();
		}catch(Exception e){
			throw new DAOException(e.getMessage());
		}
		return designations;
	}
	public DesignationDTOInterface getByCode(int code) throws DAOException{
		try{
			Connection connection = ConnectionDAO.getConnection();
			PreparedStatement preparedStatement;
			preparedStatement = connection.prepareStatement("select title from designation where code=?;");
			preparedStatement.setString(1, String.valueOf(code));
			ResultSet resultSet = preparedStatement.executeQuery();
			DesignationDTOInterface designation = null;
			if(resultSet.next()){
				designation = new DesignationDTO();
				designation.setCode(code);
				designation.setTitle(resultSet.getString("title"));
			}else{
				resultSet.close();
				preparedStatement.close();
				connection.close();
				throw new DAOException("Designation with code: "+code+" does not exists.");
			}
			resultSet.close();
			preparedStatement.close();
			connection.close();
			return designation;
		}catch(Exception e){
			throw new DAOException(e.getMessage());
		}
	}
	
	public DesignationDTOInterface getByTitle(String title) throws DAOException{
		if(title.length()==0 || title==null) throw new DAOException("Invalid Title.");
		title = title.trim();
		try{
			Connection connection = ConnectionDAO.getConnection();
			PreparedStatement preparedStatement;
			preparedStatement = connection.prepareStatement("select code from designation where title=?;");
			preparedStatement.setString(1, title);
			ResultSet resultSet = preparedStatement.executeQuery();
			DesignationDTOInterface designation = null;
			if(resultSet.next()){
				designation = new DesignationDTO();
				designation.setCode(resultSet.getInt("code"));
				designation.setTitle(title);
			}else{
				resultSet.close();
				preparedStatement.close();
				connection.close();
				throw new DAOException("Designation "+title+" does not exists.");
			}
			resultSet.close();
			preparedStatement.close();
			connection.close();
			return designation;
		}catch(Exception e){
			throw new DAOException(e.getMessage());
		}
	}
	
	public boolean codeExists(int code) throws DAOException{
		this.getByCode(code);
		return true;
	}
	
	public boolean titleExists(String title) throws DAOException{
		this.getByTitle(title);
		return true;
	}
	
	public int getCount() throws DAOException{
		try{
			Connection connection = ConnectionDAO.getConnection();
			PreparedStatement preparedStatement;
			preparedStatement = connection.prepareStatement("select count(*) as cnt from designation;");
			ResultSet resultSet = preparedStatement.executeQuery();
			resultSet.next();
			int count = 0;
			count = resultSet.getInt("cnt");
			resultSet.close();
			preparedStatement.close();
			connection.close();
			return count;
		}catch (Exception e) {
			throw new DAOException(e.getMessage());
		}
	}
}	