package com.thinking.machines.hr.dl.dao;
import java.io.*;
import java.util.*;
import com.thinking.machines.hr.dl.dto.*;
import com.thinking.machines.hr.dl.interfaces.dto.*;
import com.thinking.machines.hr.dl.interfaces.dao.*;
import com.thinking.machines.hr.dl.exceptions.*;
public class DesignationDAO implements DesignationDAOInterface{
	private final static String FILE_NAME = "designation.data";
	private final static String TEMP_FILE = "temp.data";
	public void add(DesignationDTOInterface ddtoi) throws DAOException{
		if(ddtoi == null) throw new DAOException("Designation is null.");
		String title = ddtoi.getTitle();
		if(title ==null) throw new DAOException("Designation is null");
		title = title.trim();
		if(title.length()==0) throw new DAOException("Designation length is zero");
		try{
			File file = new File(FILE_NAME);
			RandomAccessFile raf = new RandomAccessFile(file,"rw");
			int lastGeneratedCode = 0;
			int numberOfRecords = 0;
			String lastGeneratedCodeString = "";
			String numberOfRecordsString = "";
			if(raf.length()==0){
				lastGeneratedCodeString = "0";
				while (lastGeneratedCodeString.length()<10) lastGeneratedCodeString +=" ";
				numberOfRecordsString = "0";
				while(numberOfRecordsString.length()<10) numberOfRecordsString +=" ";
				raf.writeBytes(lastGeneratedCodeString);
				raf.writeBytes("\n");
				raf.writeBytes(numberOfRecordsString);
				raf.writeBytes("\n");
			}else{
				lastGeneratedCodeString = raf.readLine().trim();
				numberOfRecordsString = raf.readLine().trim();
				lastGeneratedCode = Integer.parseInt(lastGeneratedCodeString);
				numberOfRecords = Integer.parseInt(numberOfRecordsString);
			}
			int fCode;
			String fTitle;
			while(raf.getFilePointer()<raf.length()){
				fCode = Integer.parseInt(raf.readLine());
				fTitle = raf.readLine();
				if(fTitle.equalsIgnoreCase(title)){
					raf.close();
					throw new DAOException(fTitle+" is already present.");
				}
			}
			int code;
			code = lastGeneratedCode + 1;
			raf.writeBytes(String.valueOf(code));
			raf.writeBytes("\n");
			raf.writeBytes(title);
			raf.writeBytes("\n");
			ddtoi.setCode(code);
			lastGeneratedCode++;
			numberOfRecords++;
			lastGeneratedCodeString =String.valueOf(lastGeneratedCode);
			while(lastGeneratedCodeString.length()<10) lastGeneratedCodeString+=" ";
			numberOfRecordsString =String.valueOf(numberOfRecords);
			while(numberOfRecordsString.length()<10) numberOfRecordsString+=" ";
			raf.seek(0);
			raf.writeBytes(lastGeneratedCodeString);
			raf.writeBytes("\n");
			raf.writeBytes(numberOfRecordsString);
			raf.writeBytes("\n");
			raf.close();
		}catch(IOException ioe){
			throw new DAOException(ioe.getMessage());
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
		try{
			File file = new File(FILE_NAME);
			if(!file.exists()) throw new DAOException("File not found.");
			RandomAccessFile raf = new RandomAccessFile(file,"rw");
			if(raf.length()==0) throw new DAOException("File is empty.");
			raf.readLine();
			raf.readLine();
			boolean found = false;
			int fCode;
			String fTitle;
			while(raf.getFilePointer()<raf.length()){
				fCode = Integer.parseInt(raf.readLine());
				fTitle = raf.readLine();
				if(fCode == code){
					found = true;
					break;
				}
			}
			if(!found){
				raf.close();
				throw new DAOException("Code not found.");
			}
			raf.seek(0);
			raf.readLine();
			raf.readLine();
			while(raf.getFilePointer()<raf.length()){
				fCode = Integer.parseInt(raf.readLine());
				fTitle = raf.readLine();
				if(fCode!= code && fTitle.equalsIgnoreCase(title)){
					raf.close();
					throw new DAOException(fTitle+" already exists.");
				}
			}
			File temp = new File(TEMP_FILE);
			RandomAccessFile tempraf = new RandomAccessFile(temp,"rw");
			raf.seek(0);
			tempraf.writeBytes(raf.readLine());
			tempraf.writeBytes("\n");
			tempraf.writeBytes(raf.readLine());
			tempraf.writeBytes("\n");
			while(raf.length()>raf.getFilePointer()){
				fCode = Integer.parseInt(raf.readLine());
				fTitle = raf.readLine();
				if(fCode!= code){
					tempraf.writeBytes(String.valueOf(fCode));
					tempraf.writeBytes("\n");
					tempraf.writeBytes(fTitle);
					tempraf.writeBytes("\n");
				}else{
					tempraf.writeBytes(String.valueOf(fCode));
					tempraf.writeBytes("\n");
					tempraf.writeBytes(title);
					tempraf.writeBytes("\n");
				}
			}
			raf.seek(0);
			tempraf.seek(0);
			while(tempraf.length()>tempraf.getFilePointer()){
				raf.writeBytes(tempraf.readLine());
				raf.writeBytes("\n");
			}
			raf.setLength(tempraf.length());
			tempraf.setLength(0);
			raf.close();
			tempraf.close();
		}catch(IOException ioe){
			throw new DAOException(ioe.getMessage());
		}
	}
	
	//DELETE DESIGNATION
	public void delete(int code) throws DAOException{
		try{
			File file = new File(FILE_NAME);
			if(!file.exists()) throw new DAOException("File not found.");
			RandomAccessFile raf = new RandomAccessFile(file,"rw");
			if(raf.length()==0) throw new DAOException("File is empty.");
			if(raf.length()==0) throw new DAOException("File is empty.");
			raf.readLine();
			raf.readLine();
			boolean found = false;
			int fCode;
			String fTitle = "";
			while(raf.getFilePointer()<raf.length()){
				fCode = Integer.parseInt(raf.readLine());
				fTitle = raf.readLine();
				if(fCode == code){
					found = true;
					break;
				}
			}
			if(!found){
				raf.close();
				throw new DAOException("Code not found.");
			}
			/*if(new EmployeeDAO().isDesignationAlloted(code)){
				raf.close();
				throw new DAOException("Designation "+fTitle+" is alloted.");
			}*/
			File temp = new File(TEMP_FILE);
			RandomAccessFile tempraf = new RandomAccessFile(temp,"rw");
			raf.seek(0);
			tempraf.writeBytes(raf.readLine());
			tempraf.writeBytes("\n");
			String fNumberOfRecordsString = raf.readLine().trim();
			int fNumberOfRecords = Integer.parseInt(fNumberOfRecordsString);
			fNumberOfRecords -=1;
			fNumberOfRecordsString = String.valueOf(fNumberOfRecords);
			while (fNumberOfRecordsString.length()<10) fNumberOfRecordsString +=" ";
			tempraf.writeBytes(fNumberOfRecordsString);
			tempraf.writeBytes("\n");
			while(raf.length()>raf.getFilePointer()){
				fCode = Integer.parseInt(raf.readLine());
				fTitle = raf.readLine();
				if(fCode!= code){
					tempraf.writeBytes(String.valueOf(fCode));
					tempraf.writeBytes("\n");
					tempraf.writeBytes(fTitle);
					tempraf.writeBytes("\n");
				}else{
					//don't do anything
				}
			}
			raf.seek(0);
			tempraf.seek(0);
			while(tempraf.length()>tempraf.getFilePointer()){
				raf.writeBytes(tempraf.readLine());
				raf.writeBytes("\n");
			}
			raf.setLength(tempraf.length());
			tempraf.setLength(0);
			raf.close();
			tempraf.close();
		}catch(IOException ioe){
			throw new DAOException(ioe.getMessage());
		}
	}
	
	//GET ALL DESIGNATIONS
	public Set<DesignationDTOInterface> getAll() throws DAOException{
		Set<DesignationDTOInterface> designations;
		designations = new TreeSet<DesignationDTOInterface>();
		try{
			File file = new File(FILE_NAME);
			RandomAccessFile raf = new RandomAccessFile(file,"rw");
			if(raf.length()==0){
				raf.close();
				return designations;
			}
			raf.readLine();
			raf.readLine();
			DesignationDTOInterface ddtoi ;
			while(raf.length()>raf.getFilePointer()){
				ddtoi = new DesignationDTO();
				ddtoi.setCode(Integer.parseInt(raf.readLine()));
				ddtoi.setTitle(raf.readLine());
				designations.add(ddtoi);
			}
			raf.close();
			return designations;
		}catch(IOException ioe){
			throw new DAOException(ioe.getMessage());
		}
	}
	public DesignationDTOInterface getByCode(int code) throws DAOException{
		try{
			File file = new File(FILE_NAME);
			if(file.exists() == false) throw new DAOException("File does not exists.");
			RandomAccessFile raf = new RandomAccessFile(file,"rw");
			if(raf.length()==0){
				raf.close();
				throw new DAOException("File is Empty.");
			}
			raf.readLine();
			int nor;
			nor = Integer.parseInt(raf.readLine().trim());
			if(nor == 0){
				raf.close();
				throw new DAOException("No records are added.");
			}
			String fTitle;
			int fCode;
			while(raf.getFilePointer()<raf.length()){
				fCode = Integer.parseInt(raf.readLine().trim());
				fTitle = raf.readLine();
				if(fCode == code){
					raf.close();
					DesignationDTOInterface ddtoi = new DesignationDTO();
					ddtoi.setCode(code);
					ddtoi.setTitle(fTitle);
					return ddtoi;
				}				
			}
			raf.close();
			throw new DAOException(code +" code Does not exists.");
		}catch(IOException ioe){
			throw new DAOException(ioe.getMessage());
		}
	}
	
	public DesignationDTOInterface getByTitle(String title) throws DAOException{
		if(title.length()==0 || title==null) throw new DAOException("Invalid Title.");
		title = title.trim();
		try{
			File file = new File(FILE_NAME);
			if(file.exists() == false) throw new DAOException("File does not exists.");
			RandomAccessFile raf = new RandomAccessFile(file,"rw");
			if(raf.length()==0){
				raf.close();
				throw new DAOException("File is Empty.");
			}
			raf.readLine();
			int nor;
			nor = Integer.parseInt(raf.readLine().trim());
			if(nor == 0){
				raf.close();
				throw new DAOException("No records are added.");
			}
			String fTitle;
			int fCode;
			while(raf.getFilePointer()<raf.length()){
				fCode = Integer.parseInt(raf.readLine().trim());
				fTitle = raf.readLine();
				if(fTitle.equalsIgnoreCase(title)){
					raf.close();
					DesignationDTOInterface ddtoi = new DesignationDTO();
					ddtoi.setCode(fCode);
					ddtoi.setTitle(fTitle);
					return ddtoi;
				}				
			}
			raf.close();
			throw new DAOException(title +" title Does not exists.");
		}catch(IOException ioe){
			throw new DAOException(ioe.getMessage());
		}
	}
	
	public boolean codeExists(int code) throws DAOException{
		throw new DAOException("daoexception");
		
		
	}
	
	public boolean titleExists(String title) throws DAOException{
		throw new DAOException("daoexception");
		
	}
	
	public int getCount() throws DAOException{
		File file = new File(FILE_NAME);
		if(file.exists() == false) return 0;
		try{
			RandomAccessFile raf = new RandomAccessFile(file,"rw");
			if(raf.length()==0){
				raf.close();
				return 0;
			}
			raf.readLine();
			int recordCount = 0;
			recordCount = Integer.parseInt(raf.readLine().trim());
			raf.close();
			return recordCount;
		}catch(IOException ioe){
			throw new DAOException(ioe.getMessage());
		} 
	}
}	