package com.thinking.machines.hr.dl.dao;
import java.util.*;
import java.math.*;
import java.text.*;
import java.io.*;
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
		int designationCode = edtoi.getDesignationCode();
		char gender = edtoi.getGender();
		boolean isIndian = edtoi.getIsIndian();
		String name = edtoi.getName();
		Date dateOfBirth = edtoi.getDateOfBirth();
		DesignationDAOInterface ddaoi = new DesignationDAO();
		BigDecimal basicSalary = edtoi.getBasicSalary();
		String panNumber = edtoi.getPANNumber();
		String aadharCardNumber = edtoi.getAadharCardNumber();
		try{
			if(designationCode<0) throw new DAOException("Desgination code can not be negative number.");
			ddaoi.getByCode(designationCode);//will throw DAOException if code not found.
			if(edtoi.getName() == null || edtoi.getName().length() == 0) throw new DAOException("Please Enter Title.");
			if(edtoi.getDateOfBirth() == null) throw new DAOException("Please Fill All the slots.");
			if(edtoi.getBasicSalary() == null) throw new DAOException("Please Fill All the slots.");
			if(basicSalary.signum() <0) throw new DAOException("Please fill slots properly.");
			if(edtoi.getPANNumber() == null || edtoi.getPANNumber().length()==0) throw new DAOException("Please Fill All the slots.");
			if(panNumber.length()==0) throw new DAOException("Please fill PAN Number poperly.");
			if(edtoi.getAadharCardNumber() == null || edtoi.getAadharCardNumber().length()==0) throw new DAOException("Please Fill All the slots.");
			if(aadharCardNumber.length()==0 ) throw new DAOException("Please fill Aadhar Card Number Properly.");
		}catch(DAOException daoe){
			throw new DAOException(daoe.getMessage());
		}
		try{
			File file= new File(FILE_NAME);
			RandomAccessFile raf = new RandomAccessFile(file,"rw");
			int lastGeneratedCode = 10000000;
			int numberOfRecords = 0;
			String lastGeneratedCodeString = "";
			String numberOfRecordsString = "";
			if(raf.length()==0){
				lastGeneratedCodeString = String.format("%-10s",10000000);
				numberOfRecordsString = String.format("%-10s",0);
				raf.writeBytes(lastGeneratedCodeString);
				raf.writeBytes("\n");
				raf.writeBytes(numberOfRecordsString);
				raf.writeBytes("\n");
			}else{
				lastGeneratedCodeString = raf.readLine().trim();
				lastGeneratedCode = Integer.parseInt(lastGeneratedCodeString);
				numberOfRecordsString = raf.readLine().trim();
				numberOfRecords = Integer.parseInt(numberOfRecordsString);
			}
			String fpanNumber,faadharNumber;
			boolean panNumberExists=false, aadharCardNumberExists=false;
			while(raf.length()>raf.getFilePointer()){
				for(int i=0 ; i<7;i++) raf.readLine();
				fpanNumber = raf.readLine();
				faadharNumber = raf.readLine();
				if(panNumberExists == false && fpanNumber.equalsIgnoreCase(panNumber)){
					panNumberExists=true;
				}
				if(aadharCardNumberExists == false && faadharNumber.equalsIgnoreCase(aadharCardNumber)){
					aadharCardNumberExists=true;
				}
				if(panNumberExists == true && aadharCardNumberExists ==true) break;
			}
			if(panNumberExists && aadharCardNumberExists){
				raf.close();
				throw new DAOException("PAN Card number "+panNumber+" and Aadhar Card Number "+aadharCardNumber+" already exists.");
			}
			if(panNumberExists){
				raf.close();
				throw new DAOException("PAN Number "+panNumber+" already exists.");
			}
			if(aadharCardNumberExists){
				raf.close();
				throw new DAOException("Aadhar card Number "+aadharCardNumber+" already exists.");
			}
			lastGeneratedCode++;
			numberOfRecords++;
			String EmpID = "A" + String.format("%-10s",lastGeneratedCode);
			raf.writeBytes(EmpID+"\n");
			raf.writeBytes(name+"\n");
			raf.writeBytes(String.valueOf(designationCode)+"\n");
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
			raf.writeBytes(sdf.format(dateOfBirth)+"\n");
			raf.writeBytes(gender+"\n");
			raf.writeBytes(isIndian+"\n");
			raf.writeBytes(basicSalary.toPlainString()+"\n");
			raf.writeBytes(panNumber+"\n");
			raf.writeBytes(aadharCardNumber+"\n");
			raf.seek(0);
			lastGeneratedCodeString = String.format("%-10s",lastGeneratedCode);
			numberOfRecordsString = String.format("%-10s",numberOfRecords);
			raf.writeBytes(lastGeneratedCodeString+"\n");
			raf.writeBytes(numberOfRecordsString+"\n");
			raf.close();
			edtoi.setEmployeeId(EmpID);
		}catch(IOException ioe){
			throw new DAOException(ioe.getMessage());
		}
	}
	
	//UPDATE EMPLOYEE DATA
	public void update(EmployeeDTOInterface edtoi) throws DAOException{
		String EmpID = edtoi.getEmployeeId();
		int designationCode = edtoi.getDesignationCode();
		char gender = edtoi.getGender();
		boolean isIndian = edtoi.getIsIndian();
		String name = edtoi.getName();
		Date dateOfBirth = edtoi.getDateOfBirth();
		DesignationDAOInterface ddaoi = new DesignationDAO();
		BigDecimal basicSalary = edtoi.getBasicSalary();
		String panNumber = edtoi.getPANNumber();
		String aadharCardNumber = edtoi.getAadharCardNumber();
		try{
			if(EmpID.length()==0 || EmpID ==null) throw new DAOException("Employee ID is Null");
			if(designationCode<0) throw new DAOException("Desgination code can not be negative number.");
			ddaoi.getByCode(designationCode);//will throw DAOException if code not found.
			if(edtoi.getName() == null || edtoi.getName().length() == 0) throw new DAOException("Please Enter Title.");
			if(edtoi.getDateOfBirth() == null) throw new DAOException("Please Fill All the slots.");
			if(gender == ' ') throw new DAOException("Gender is not Male/Female type");
			if(edtoi.getBasicSalary() == null) throw new DAOException("Please Fill All the slots.");
			if(basicSalary.signum() <0) throw new DAOException("Please fill slots properly.");
			if(edtoi.getPANNumber() == null || edtoi.getPANNumber().length()==0) throw new DAOException("Please Fill All the slots.");
			if(panNumber.length()==0) throw new DAOException("Please fill PAN Number poperly.");
			if(edtoi.getAadharCardNumber() == null || edtoi.getAadharCardNumber().length()==0) throw new DAOException("Please Fill All the slots.");
			if(aadharCardNumber.length()==0 ) throw new DAOException("Please fill Aadhar Card Number Properly.");
		}catch(DAOException daoe){
			throw new DAOException(daoe.getMessage());
		}
		try{
			File file = new File(FILE_NAME);
			if(!file.exists()) throw new DAOException("File does not exists.");
			RandomAccessFile raf = new RandomAccessFile(file,"rw");
			if(raf.length()==0){
				raf.close();
				throw new DAOException("File is empty");
			} 
			raf.readLine();
			raf.readLine();
			String fEmployeeId;
			String fpanNumber, faadharNumber;
			boolean employeeIdExists=false;
			boolean panCardNumberExists=false,aadharCardNumberExists=false;
			String panCardNumberFoundAgainstEmployeeId=null, aadharCardNumberFoundAgainstEmployeeId=null;
			long foundAt=0;
			while(raf.length()>raf.getFilePointer()){
				if(employeeIdExists == false) foundAt = raf.getFilePointer();
				fEmployeeId = raf.readLine().trim();
				for(int i=0;i<6;i++) raf.readLine();
				fpanNumber = raf.readLine();
				faadharNumber = raf.readLine();
				if(employeeIdExists ==false && fEmployeeId.equalsIgnoreCase(EmpID)){
					employeeIdExists =true;
				}
				if(panCardNumberExists ==false && fpanNumber.equalsIgnoreCase(panNumber)){
					panCardNumberExists = true;
					panCardNumberFoundAgainstEmployeeId = fEmployeeId;
				}
				if(aadharCardNumberExists ==false && faadharNumber.equalsIgnoreCase(aadharCardNumber)){
					aadharCardNumberExists = true;
					aadharCardNumberFoundAgainstEmployeeId =fEmployeeId;
				}
				if(aadharCardNumberExists && panCardNumberExists && employeeIdExists) break;
			}
			if(!employeeIdExists){
				raf.close();
				throw new DAOException("No such employee id registered.");
			}
			if(panCardNumberExists && panCardNumberFoundAgainstEmployeeId.equalsIgnoreCase(EmpID) ==false){
				raf.close();
				throw new DAOException("PAN Number "+panNumber+" already exists for some other employee.");
			}
			if(aadharCardNumberExists && aadharCardNumberFoundAgainstEmployeeId.equalsIgnoreCase(EmpID) == false){
				raf.close();
				throw new DAOException("Aadhar card Number "+aadharCardNumber+" already exists for some other employee.");
			}
			raf.seek(foundAt);
			for(int i=0;i<9;i++) raf.readLine();
			File tempFile = new File (TEMP_NAME);
			if(tempFile.exists()) tempFile.delete();
			RandomAccessFile temp = new RandomAccessFile(tempFile,"rw");
			while(raf.getFilePointer()<raf.length()){
				temp.writeBytes(raf.readLine()+"\n");//writing data present after updating data into temporary file
			}
			raf.seek(foundAt);
			temp.seek(0);
			raf.writeBytes(EmpID+"\n");
			raf.writeBytes(name+"\n");
			raf.writeBytes(String.valueOf(designationCode)+"\n");
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
			raf.writeBytes(sdf.format(dateOfBirth)+"\n");
			raf.writeBytes(gender+"\n");
			raf.writeBytes(isIndian+"\n");
			raf.writeBytes(basicSalary.toPlainString()+"\n");
			raf.writeBytes(panNumber+"\n");
			raf.writeBytes(aadharCardNumber+"\n");
			while(temp.length()>temp.getFilePointer()){
				raf.writeBytes(temp.readLine()+"\n");// writing temporary file content back into employee.data
			}
			raf.setLength(raf.getFilePointer());
			temp.setLength(0);
			raf.close();
			temp.close();
		}catch(IOException ioe){
			throw new DAOException(ioe.getMessage());
		}		
	}
	
	//DELETE EMPLOYEE DATA
	public void delete(String empID) throws DAOException{
		try{
			File file = new File(FILE_NAME);
			if(!file.exists()) throw new DAOException("File does not exists.");
			RandomAccessFile raf = new RandomAccessFile(file,"rw");
			if(raf.length()==0){
				raf.close();
				throw new DAOException("File is empty");
			} 
			raf.readLine();
			raf.readLine();
			String fEmployeeId;
			String fpanNumber, faadharNumber;
			boolean employeeIdExists=false;
			long foundAt=0;
			while(raf.length()>raf.getFilePointer()){
				if(employeeIdExists == false) foundAt = raf.getFilePointer();
				fEmployeeId = raf.readLine().trim();
				for(int i=0;i<8;i++) raf.readLine();
				if(employeeIdExists ==false && fEmployeeId.equalsIgnoreCase(empID)){
					employeeIdExists =true;
					break;
				}
			}
			if(!employeeIdExists){
				raf.close();
				throw new DAOException("No such employee id registered.");
			}
			raf.seek(foundAt);
			for(int i=0;i<9;i++) raf.readLine();
			File tempFile = new File (TEMP_NAME);
			if(tempFile.exists()) tempFile.delete();
			RandomAccessFile temp = new RandomAccessFile(tempFile,"rw");
			while(raf.getFilePointer()<raf.length()){
				temp.writeBytes(raf.readLine()+"\n");//writing data present after data to update into temporary file
			}
			raf.seek(foundAt);
			temp.seek(0);
			while(temp.length()>temp.getFilePointer()){
				raf.writeBytes(temp.readLine()+"\n");// writing temporary file content back into employee.data
			}
			raf.setLength(raf.getFilePointer());
			temp.setLength(0);
			raf.close();
			temp.close();
		}catch(IOException ioe){
			throw new DAOException(ioe.getMessage());
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
		try{
			File file = new File(FILE_NAME);
			if(file.exists() == false) return employees;
			RandomAccessFile raf = new RandomAccessFile(file,"rw");
			if(raf.length() == 0 ){
				raf.close();
				throw new DAOException("No records added.");
			}
			raf.readLine();
			raf.readLine();
			char fGender;
			while(raf.getFilePointer() < raf.length()){
				EmployeeDTOInterface edtoi = new EmployeeDTO();
				edtoi.setEmployeeId(raf.readLine());
				edtoi.setName(raf.readLine());
				edtoi.setDesignationCode(Integer.parseInt(raf.readLine()));
				try {
					SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
					edtoi.setDateOfBirth(sdf.parse(raf.readLine()));
				}catch(ParseException pe){
					//raf.close();
					//throw new DAOException(pe.getMessage());
				}
				fGender = raf.readLine().charAt(0);
				if(fGender == 'M'){
					edtoi.setGender(GENDER.MALE);
				}else if(fGender == 'F'){
					edtoi.setGender(GENDER.FEMALE);
				}
				edtoi.setIsIndian(Boolean.parseBoolean(raf.readLine()));
				edtoi.setBasicSalary(new BigDecimal(raf.readLine()));
				edtoi.setPANNumber(raf.readLine());
				edtoi.setAadharCardNumber(raf.readLine());
				employees.add(edtoi);
			}
			raf.close();
			return employees;
		}catch(IOException ioe){
			throw new DAOException(ioe.getMessage());
		}
	}
	
	//GET EMPLOYEE DATA BY DESIGNATION
	public Set<EmployeeDTOInterface> getByDesignationCode(int designationCode) throws DAOException{
		Set<EmployeeDTOInterface> employees = new TreeSet<>();
		try{
			DesignationDAOInterface ddaoi = new DesignationDAO();
			ddaoi.getByCode(designationCode);
		}catch(DAOException daoe){
			throw new DAOException(daoe.getMessage());
		}
		try{
			File file = new File(FILE_NAME);
			if(file.exists() == false) return employees;
			RandomAccessFile raf = new RandomAccessFile(file,"rw");
			if(raf.length() == 0 ){
				raf.close();
				throw new DAOException("No records added.");
			}
			raf.readLine();
			raf.readLine();
			char fGender;
			while(raf.getFilePointer() < raf.length()){
				EmployeeDTOInterface edtoi = new EmployeeDTO();
				edtoi.setEmployeeId(raf.readLine());
				edtoi.setName(raf.readLine());
				edtoi.setDesignationCode(Integer.parseInt(raf.readLine()));
				try {
					SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
					edtoi.setDateOfBirth(sdf.parse(raf.readLine()));
				}catch(ParseException pe){
					System.out.println("error in parsing");
					//raf.close();
					//throw new DAOException(pe.getMessage());
				}
				fGender = raf.readLine().charAt(0);
				if(fGender == 'M'){
					edtoi.setGender(GENDER.MALE);
				}else if(fGender == 'F'){
					edtoi.setGender(GENDER.FEMALE);
				}edtoi.setIsIndian(Boolean.parseBoolean(raf.readLine()));
				edtoi.setBasicSalary(new BigDecimal(raf.readLine()));
				edtoi.setPANNumber(raf.readLine());
				edtoi.setAadharCardNumber(raf.readLine());
				if(edtoi.getDesignationCode() == designationCode){
					employees.add(edtoi);
				}
			}
			raf.close();
			return employees;
		}catch(IOException ioe){
			throw new DAOException(ioe.getMessage());
		}	
	}
	
	//GET EMPLOYEE DATA BY ID
	public EmployeeDTOInterface getByEmployeeId(String empID) throws DAOException{
		if(empID == null || empID.length()==0) throw new DAOException("Please Enter Input Properly.");
		try{
			File file = new File(FILE_NAME);
			if(!file.exists()) throw new DAOException("File not Found");
			RandomAccessFile raf = new RandomAccessFile(file ,"rw");
			if(raf.length()==0){
				raf.close();
				throw new DAOException("File is empty.");
			}
			raf.readLine();
			raf.readLine();
			String fEmployeeId;
			char fGender;
			while(raf.length()>raf.getFilePointer()){
				fEmployeeId = raf.readLine().trim();
				if(fEmployeeId.equals(empID)){
					EmployeeDTOInterface edtoi = new EmployeeDTO();
					edtoi.setEmployeeId(fEmployeeId);
					edtoi.setName(raf.readLine());
					edtoi.setDesignationCode(Integer.parseInt(raf.readLine()));
					try {
						SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
						edtoi.setDateOfBirth(sdf.parse(raf.readLine()));
					}catch(ParseException pe){
						//raf.close();
						//throw new DAOException(pe.getMessage());
					}
					fGender = raf.readLine().charAt(0);
					if(fGender == 'M'){
						edtoi.setGender(GENDER.MALE);
					}else if(fGender == 'F'){
						edtoi.setGender(GENDER.FEMALE);
					}edtoi.setIsIndian(Boolean.parseBoolean(raf.readLine()));
					edtoi.setBasicSalary(new BigDecimal(raf.readLine()));
					edtoi.setPANNumber(raf.readLine());
					edtoi.setAadharCardNumber(raf.readLine());
					raf.close();
					return edtoi;
				}
				for(int i=0;i<8;i++) raf.readLine();
			}
			raf.close();
			throw new DAOException("No such roll Number found.");
		}catch(IOException ioe){
			throw new DAOException(ioe.getMessage());
		}
	}
	public EmployeeDTOInterface getByPANNumber(String name) throws DAOException{
		throw new DAOException("some error");
	}
	public EmployeeDTOInterface getByAadharCardNumber(String title) throws DAOException{
		throw new DAOException("some error");
	}
	public boolean employeeIdExists(String empID) throws DAOException{
		throw new DAOException("some error");
	}
	public boolean panCardNumberExists(String panCardNumber) throws DAOException{
		throw new DAOException("some error");
	}
	public boolean aadharCardNumberExists(String aadharCardNumber) throws DAOException{
		throw new DAOException("some error");
	}
	public int getCount() throws DAOException{
		throw new DAOException("some error");
	}
	public int getCountByDesignation(int designationCode) throws DAOException{
		throw new DAOException("some error");
	}
}