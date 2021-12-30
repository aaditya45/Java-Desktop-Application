/*
What to do:-
Exchanging getDesignationCode to getDesignation
*/
package com.thinking.machines.hr.bl.managers;
import com.thinking.machines.hr.bl.exceptions.*;
import com.thinking.machines.hr.bl.interfaces.pojo.*;
import com.thinking.machines.hr.bl.interfaces.managers.*;
import com.thinking.machines.hr.bl.pojo.*;
import java.util.*;
import java.text.*;
import java.math.*;
import com.thinking.machines.enums.*;
import com.thinking.machines.hr.dl.dao.*;
import com.thinking.machines.hr.dl.dto.*;
import com.thinking.machines.hr.dl.interfaces.dao.*;
import com.thinking.machines.hr.dl.interfaces.dto.*;
import com.thinking.machines.hr.dl.exceptions.*;
public class EmployeeManager implements EmployeeManagerInterface{
	private Map<String,EmployeeInterface> employeeIdWiseEmployeeMap; 
	private Map<String,EmployeeInterface> panNumberWiseEmployeeMap; 
	private Map<String,EmployeeInterface> aadharCardNumberWiseEmployeeMap; 
	private Map<Integer,Set<EmployeeInterface>> designationCodeWiseEmployeeMap; 
	private Set<EmployeeInterface> employeesSet; 
	private static EmployeeManager employeeManager = null;
	private EmployeeManager() throws BLException{
		populateDataStructure();
	}
	public void populateDataStructure() throws BLException{
		this.employeeIdWiseEmployeeMap = new HashMap<>();
		this.panNumberWiseEmployeeMap = new HashMap<>();
		this.aadharCardNumberWiseEmployeeMap = new HashMap<>();
		this.designationCodeWiseEmployeeMap = new HashMap<>();
		this.employeesSet = new TreeSet<>();//contains all the employee ids
		try{
			EmployeeDAOInterface edaoi = new EmployeeDAO();
			Set<EmployeeDTOInterface> employeeDTOs;
			employeeDTOs = edaoi.getAll();
			DesignationInterface designation;
			EmployeeInterface ei;
			DesignationManagerInterface dmi;
			Set<EmployeeInterface> ets;//(EmployeeTreeSet)
			for(EmployeeDTOInterface e : employeeDTOs){
				ei = new Employee();
				ei.setEmployeeId(e.getEmployeeId());
				ei.setName(e.getName());
				designation = new Designation();
				dmi = DesignationManager.getDesignationManager();
				designation = dmi.getDesignationByCode(e.getDesignationCode());
				ei.setDesignation(designation);
				ei.setDateOfBirth((Date)e.getDateOfBirth().clone());
				if(e.getGender()=='M'){
					ei.setGender(GENDER.MALE);
				}else{
					ei.setGender(GENDER.FEMALE);
				}
				ei.setIsIndian(e.getIsIndian());
				ei.setBasicSalary(e.getBasicSalary());
				ei.setPANNumber(e.getPANNumber());
				ei.setAadharCardNumber(e.getAadharCardNumber());
				this.employeeIdWiseEmployeeMap.put(ei.getEmployeeId().toUpperCase().trim(),ei);
				this.panNumberWiseEmployeeMap.put(ei.getPANNumber().toUpperCase(),ei);
				this.aadharCardNumberWiseEmployeeMap.put(ei.getAadharCardNumber().toUpperCase(),ei);
				this.employeesSet.add(ei);//contains all the employee ids
				ets = this.designationCodeWiseEmployeeMap.get(designation.getCode());
				if(ets ==null){
					ets = new TreeSet<>();
					ets.add(ei);
					designationCodeWiseEmployeeMap.put(new Integer(designation.getCode()),ets);
				}else{
					ets.add(ei);
				}				
			}
		}catch(DAOException daoexception){
			BLException ble = new BLException();
			ble.setGenericException(daoexception.getMessage());
			throw ble;
		}
	}
	// this method is for 
	public static EmployeeManagerInterface getEmployeeManager() throws BLException{
		if(employeeManager == null) employeeManager = new EmployeeManager();
		return employeeManager;
	}
	public void addEmployee(EmployeeInterface employee) throws BLException{
		BLException ble = new BLException();
		String employeeId= employee.getEmployeeId();
		String name = employee.getName();
		DesignationInterface designation = employee.getDesignation();
		Date dateOfBirth = employee.getDateOfBirth();
		char gender = employee.getGender();
		boolean isIndian = employee.getIsIndian();
		BigDecimal basicSalary = employee.getBasicSalary(); 
		String panCardNumber = employee.getPANNumber();
		String aadharCardNumber = employee.getAadharCardNumber();
		if(employeeId==null){
			employeeId="";
		}else{
			employeeId =  employeeId.trim();
			if(employeeId.length()>0){
				ble.addException("employeeId","Employee Id should be nill");
			}
			employeeId = "";
		}
		if(name==null){
			ble.addException("name","Invalid name");
			name = "";
		}else {
			name = name.trim();
			if(name.length()==0){
				ble.addException("name","Invalid name");
			}
		}
		if(designation == null){
			ble.addException("designation","Invalid designation");
		}else{
			DesignationManagerInterface dmi = DesignationManager.getDesignationManager();
			if(dmi.designationCodeExists(designation.getCode())==false){
				ble.addException("designation","Invalid Designation");
			}
		}
		if(dateOfBirth ==null){
			ble.addException("dateOfBirth","Invalid date of birth");
		}
		if(gender ==' '){
			ble.addException("gender","Invalid gender");
		}
		if(basicSalary == null){
			ble.addException("salary","Invalid salary");
		}else{
			if(basicSalary.signum()==-1){
				ble.addException("salary","Salary can never be negative number.");
			}
		}
		if(panCardNumber==null){
			ble.addException("panCardNumber","Invalid Pan Number");
			panCardNumber="";
		}else{
			panCardNumber =  panCardNumber.trim();
			if(panCardNumber.length()==0){
				ble.addException("panCardNumber","Invalid Pan Number");
			}
		}
		if(aadharCardNumber==null){
			ble.addException("aadharCardNumber","Invalid aadhar card number");
			aadharCardNumber="";
		}else{
			aadharCardNumber =  aadharCardNumber.trim();
			if(aadharCardNumber.length()==0){
				ble.addException("aadharCardNumber","Invalid aadhar Card number");
			}
		}
		if(panCardNumber!=null && panCardNumber.length()>0){
			if(this.panNumberWiseEmployeeMap.containsKey(panCardNumber.toUpperCase())){
				ble.addException("panCardNumber",panCardNumber + " PAN Number already exists.");
			}
		}
		if(aadharCardNumber!=null && aadharCardNumber.length()>0){
			if(this.aadharCardNumberWiseEmployeeMap.containsKey(aadharCardNumber.toUpperCase())){
				ble.addException("aadharCardNumber",aadharCardNumber + " Aadhar card Number already exists.");
			}
		}
		if(ble.hasExceptions()){
			throw ble;
		}
		try{
			EmployeeDAOInterface edaoi = new EmployeeDAO();
			EmployeeDTOInterface edtoi = new EmployeeDTO();
			edtoi.setName(name);
			edtoi.setDesignationCode(designation.getCode());
			edtoi.setDateOfBirth((Date)dateOfBirth.clone());
			edtoi.setGender((gender=='M')?GENDER.MALE:GENDER.FEMALE);
			edtoi.setIsIndian(isIndian);
			edtoi.setBasicSalary(basicSalary);
			edtoi.setPANNumber(panCardNumber);
			edtoi.setAadharCardNumber(aadharCardNumber);
			edaoi.add(edtoi);
			
			EmployeeInterface ei = new Employee();
			ei.setEmployeeId(edtoi.getEmployeeId());//getting employee ID that this data set got from data layer
			ei.setName(name);
			ei.setDesignation(designation);
			ei.setDateOfBirth((Date)dateOfBirth.clone());
			ei.setGender((gender=='M')?GENDER.MALE:GENDER.FEMALE);
			ei.setIsIndian(isIndian);
			ei.setBasicSalary(basicSalary);
			ei.setPANNumber(panCardNumber);
			ei.setAadharCardNumber(aadharCardNumber);
			this.employeesSet.add(ei);//contains all the employee ids
			this.employeeIdWiseEmployeeMap.put(edtoi.getEmployeeId().toUpperCase().trim(),ei);
			this.panNumberWiseEmployeeMap.put(panCardNumber.toUpperCase(),ei);
			this.aadharCardNumberWiseEmployeeMap.put(aadharCardNumber.toUpperCase(),ei);
			employee.setEmployeeId(ei.getEmployeeId().toUpperCase());//to return original employee id to testcase coder
			Set<EmployeeInterface> ets;//(Employee Tree Set)
			ets = this.designationCodeWiseEmployeeMap.get(ei.getDesignation().getCode());
			if(ets ==null){
				ets = new TreeSet<>();
				ets.add(ei);
				designationCodeWiseEmployeeMap.put(new Integer(ei.getDesignation().getCode()),ets);
			}else{
				ets.add(ei);
			}
		}catch(DAOException daoe){
			BLException blexception = new BLException();
			blexception.setGenericException(daoe.getMessage());
			throw blexception;
		}
	}
	public void updateEmployee(EmployeeInterface employee) throws BLException{
		BLException ble = new BLException();
		String employeeId= employee.getEmployeeId();
		String name = employee.getName();
		DesignationInterface designation = employee.getDesignation();
		Date dateOfBirth = employee.getDateOfBirth();
		char gender = employee.getGender();
		boolean isIndian = employee.getIsIndian();
		BigDecimal basicSalary = employee.getBasicSalary(); 
		String panCardNumber = employee.getPANNumber();
		String aadharCardNumber = employee.getAadharCardNumber();
		if(employeeId==null){
			ble.addException("employeeId","Invalid Employee ID");
		}else{
			employeeId =  employeeId.toUpperCase();
			if(employeeId.length()==0){
				ble.addException("employeeId","Invalid Employee ID");
			}else{
				if(this.employeeIdWiseEmployeeMap.containsKey(employeeId.toUpperCase())==false){
					ble.addException("employeeId",employeeId + ", Employee ID doesn't exists.");
					throw ble;
				}
			}
		}
		if(name==null){
			ble.addException("name","Invalid name");
			name = "";
		}else {
			name = name.trim();
			if(name.length()==0){
				ble.addException("name","Invalid name");
			}
		}
		if(designation == null){
			ble.addException("designation","Invalid designation");
		}else{
			DesignationManagerInterface dmi = DesignationManager.getDesignationManager();
			if(dmi.designationCodeExists(designation.getCode())==false){
				ble.addException("designation","Invalid Designation");
			}
		}
		if(dateOfBirth ==null){
			ble.addException("dateOfBirth","Invalid date of birth");
		}
		if(gender ==' '){
			ble.addException("gender","Invalid gender");
		}
		if(basicSalary == null){
			ble.addException("salary","Invalid salary");
		}else{
			if(basicSalary.signum()==-1){
				ble.addException("salary","Salary can never be negative number.");
			}
		}
		if(panCardNumber==null){
			ble.addException("panCardNumber","Invalid Pan Number");
			panCardNumber="";
		}else{
			panCardNumber =  panCardNumber.trim();
			if(panCardNumber.length()==0){
				ble.addException("panCardNumber","Invalid Pan Number");
			}
		}
		if(aadharCardNumber==null){
			ble.addException("aadharCardNumber","Invalid aadhar card number");
			aadharCardNumber="";
		}else{
			aadharCardNumber =  aadharCardNumber.trim();
			if(aadharCardNumber.length()==0){
				ble.addException("aadharCardNumber","Invalid aadhar Card number");
			}
		}
		
		if(ble.hasExceptions()){
			throw ble;
		}
		try{
			EmployeeDAOInterface edaoi = new EmployeeDAO();
			EmployeeDTOInterface edtoi = new EmployeeDTO();
			edtoi.setEmployeeId(employeeId);
			edtoi.setName(name);
			edtoi.setDesignationCode(designation.getCode());
			edtoi.setDateOfBirth((Date)dateOfBirth.clone());
			if(gender=='M'){
				edtoi.setGender(GENDER.MALE);
			}else{
				edtoi.setGender(GENDER.FEMALE);
			}
			edtoi.setIsIndian(isIndian);
			edtoi.setBasicSalary(basicSalary);
			edtoi.setPANNumber(panCardNumber);
			edtoi.setAadharCardNumber(aadharCardNumber);
			edaoi.update(edtoi);//update method from data layer object
			EmployeeInterface ei = new Employee();//clone variable in order to make our data structures safe
			System.out.println("Employee id registered : " + edtoi.getEmployeeId());
			ei.setEmployeeId(employeeId);
			ei.setName(name);
			ei.setDesignation(designation);
			ei.setDateOfBirth((Date)dateOfBirth.clone());
			if(gender=='M'){
				ei.setGender(GENDER.MALE);
			}else{
				ei.setGender(GENDER.FEMALE);
			}
			ei.setIsIndian(isIndian);
			ei.setBasicSalary(basicSalary);
			ei.setPANNumber(panCardNumber);
			ei.setAadharCardNumber(aadharCardNumber);
			//Extracting old Data From DataStructure, in order to remove it from every DataStructures
			EmployeeInterface oldData = new Employee();
			oldData = this.employeeIdWiseEmployeeMap.get(employeeId.toUpperCase().trim());
			int oldDesignationCode = oldData.getDesignation().getCode();
			
			//Removing old Data from DataStructures
			this.employeeIdWiseEmployeeMap.remove(employeeId.toUpperCase());
			this.panNumberWiseEmployeeMap.remove(oldData.getPANNumber().toUpperCase());
			this.aadharCardNumberWiseEmployeeMap.remove(oldData.getAadharCardNumber().toUpperCase());
			this.employeesSet.remove(oldData);//contains all the employee ids
			
			//Adding Updated Data to DataStructures as New Data
			this.employeeIdWiseEmployeeMap.put(employeeId.toUpperCase(),ei);
			this.panNumberWiseEmployeeMap.put(panCardNumber.toUpperCase(),ei);
			this.aadharCardNumberWiseEmployeeMap.put(aadharCardNumber.toUpperCase(),ei);
			this.employeesSet.add(ei);//contains all the employee ids
			
			//Updating Data in dataStructure designationCodeWiseEmployeeMap
			if(oldDesignationCode!=ei.getDesignation().getCode()){
				Set<EmployeeInterface> ets;
				ets = this.designationCodeWiseEmployeeMap.get(oldDesignationCode);
				ets.remove(ei);
				ets = this.designationCodeWiseEmployeeMap.get(ei.getDesignation().getCode());
				if(ets == null){
					ets = new TreeSet<>();
					ets.add(ei);
					this.designationCodeWiseEmployeeMap.put(new Integer(ei.getDesignation().getCode()),ets);
				}else{
					ets.add(ei);
				}	
			}
		}catch(DAOException daoe){
			BLException blexception = new BLException();
			blexception.setGenericException(daoe.getMessage());
			throw blexception;
		}
	}
	public void removeEmployee(String EmployeeId) throws BLException{
		String employeeId = EmployeeId;
		BLException ble = new BLException();
		if(employeeId==null){
			ble.addException("employeeId","Invalid Employee id.");
			employeeId= "";
		}else{
			employeeId = employeeId.trim();
			if(employeeId.length()==0){
				ble.addException("employeeId","Invalid Employee id");
			}
		}
		if(employeeId!=null && employeeId.length()>0){
			if(this.employeeIdWiseEmployeeMap.containsKey(employeeId.toUpperCase())==false){
				ble.addException("employeeId",employeeId + " Employee ID doesn't exists");
			}
		}
		if(ble.hasExceptions()){
			throw ble;
		}
		try{
			EmployeeDAOInterface edaoi = new EmployeeDAO();
			edaoi.delete(employeeId);//delete method from data layer object
			//Extracting Data From DataStructure, in order to remove it from every DataStructures
			EmployeeInterface oldData = new Employee();
			oldData = this.employeeIdWiseEmployeeMap.get(employeeId.toUpperCase().trim());
			int oldDesignationCode = oldData.getDesignation().getCode();
			//Removing Data from DataStructures
			this.employeeIdWiseEmployeeMap.remove(employeeId.toUpperCase());
			this.panNumberWiseEmployeeMap.remove(oldData.getPANNumber().toUpperCase());
			this.aadharCardNumberWiseEmployeeMap.remove(oldData.getAadharCardNumber().toUpperCase());
			this.employeesSet.remove(oldData);
			Set<EmployeeInterface> ets;
			ets = this.designationCodeWiseEmployeeMap.get(oldDesignationCode);
			ets.remove(oldData);
		}catch(DAOException daoe){
			BLException blexception = new BLException();
			blexception.setGenericException(daoe.getMessage());
			throw blexception;
		}
	}
	public EmployeeInterface getEmployeeById(String EmployeeId) throws BLException{
		String employeeId = EmployeeId;
		BLException ble = new BLException();
		if(employeeId==null){
			ble.addException("employeeId","Invalid Employee id.");
			employeeId= "";
		}else{
			employeeId = employeeId.trim();
			if(employeeId.length()==0){
				ble.addException("employeeId","Invalid Employee id");
			}
		}
		if(employeeId!=null && employeeId.length()>0){
			if(this.employeeIdWiseEmployeeMap.containsKey(employeeId.toUpperCase())==false){
				ble.addException("employeeId",employeeId + " Employee ID doesn't exists");
			}
		}
		if(ble.hasExceptions()){
			throw ble;
		}
		EmployeeInterface ei;
		ei = this.employeeIdWiseEmployeeMap.get(employeeId.toUpperCase());
		return ei;
	}
	public EmployeeInterface getEmployeeByAadharCardNumber(String aadharCardNumber) throws BLException{
		BLException ble = new BLException();
		if(aadharCardNumber==null){
			ble.addException("aadharCardNumber","Invalid Aadhar Card Number.");
			aadharCardNumber= "";
		}else{
			aadharCardNumber = aadharCardNumber.trim();
			if(aadharCardNumber.length()==0){
				ble.addException("aadharCardNumber","Invalid Aadhar Card Number");
			}
		}
		if(aadharCardNumber!=null && aadharCardNumber.length()>0){
			if(this.aadharCardNumberWiseEmployeeMap.containsKey(aadharCardNumber.toUpperCase())==false){
				ble.addException("aadharCardNumber",aadharCardNumber + " Aadhar Card Number doesn't exists");
			}
		}
		if(ble.hasExceptions()){
			throw ble;
		}
		EmployeeInterface ei;
		ei = this.aadharCardNumberWiseEmployeeMap.get(aadharCardNumber.toUpperCase());
		return ei;
	}
	public EmployeeInterface getEmployeeByPANNumber(String panNumber) throws BLException{
		BLException ble = new BLException();
		if(panNumber==null){
			ble.addException("panNumber","Invalid pan Number.");
			panNumber= "";
		}else{
			panNumber = panNumber.trim();
			if(panNumber.length()==0){
				ble.addException("panNumber","Invalid pan Number");
			}
		}
		if(panNumber!=null && panNumber.length()>0){
			if(this.panNumberWiseEmployeeMap.containsKey(panNumber.toUpperCase())==false){
				ble.addException("panNumber",panNumber + " pan Number Number doesn't exists");
			}
		}
		if(ble.hasExceptions()){
			throw ble;
		}
		EmployeeInterface ei;
		ei = this.panNumberWiseEmployeeMap.get(panNumber.toUpperCase());
		return ei;
	}
	public int getEmployeeCount() throws BLException{
		int count;
		count = this.employeeIdWiseEmployeeMap.size();
		return count;
	}
	public boolean employeeIdExists(String employeeId) throws BLException{
		boolean found;
		found =false;
		if(this.employeeIdWiseEmployeeMap.containsKey(employeeId.toUpperCase())) found =true;
		return found;
	}
	public boolean employeePANNumberExists(String panNumber) throws BLException{
		boolean found;
		found = false;
		if(this.panNumberWiseEmployeeMap.containsKey(panNumber.toUpperCase())) found = true;
		return found;
	}
	public boolean employeeAadharCardNumberExists(String aadharCardNumber) throws BLException{
		boolean found;
		found = false;
		if(this.aadharCardNumberWiseEmployeeMap.containsKey(aadharCardNumber.toUpperCase())) found = true;
		return found;
	}
	public Set<EmployeeInterface> getEmployees() throws BLException{
		Set<EmployeeInterface> employees = new TreeSet<>();
		for(EmployeeInterface employee : this.employeesSet){
			employees.add(employee);
		}
		return employees;
	}
	public Set<EmployeeInterface> getEmployeesByDesignationCode(int designationCode) throws BLException{
		DesignationManagerInterface designationManager;
		designationManager=DesignationManager.getDesignationManager();
		if(designationManager.designationCodeExists(designationCode)==false){
			BLException blException=new BLException();
			blException.setGenericException("Invalid designation code "+designationCode);
			throw blException;
		}
		Set<EmployeeInterface> employees=new TreeSet<>();
		Set<EmployeeInterface> ets;
		ets=this.designationCodeWiseEmployeeMap.get(designationCode);
		if(ets==null){
			return employees; 
		}
		EmployeeInterface employee;
		DesignationInterface designation;
		for(EmployeeInterface dsEmployee:ets){
			employee=new Employee();
			employee.setEmployeeId(dsEmployee.getEmployeeId());
			employee.setName(dsEmployee.getName());
			designation=new Designation(); 
			designation.setCode(dsEmployee.getDesignation().getCode());
			designation.setTitle(dsEmployee.getDesignation().getTitle());
			employee.setDesignation(designation);
			employee.setDateOfBirth((Date)dsEmployee.getDateOfBirth().clone());
			employee.setGender((dsEmployee.getGender()=='M')?GENDER.MALE:GENDER.FEMALE);
			employee.setIsIndian(dsEmployee.getIsIndian());
			employee.setBasicSalary(dsEmployee.getBasicSalary());
			employee.setPANNumber(dsEmployee.getPANNumber());
			employee.setAadharCardNumber(dsEmployee.getAadharCardNumber());
			employees.add(employee);
		}
		return employees;
		/*Set<EmployeeInterface> ets = new TreeSet<>();
		if(this.designationCodeWiseEmployeeMap.containsKey(designationCode)==false){
			return ets;
		}else{
			ets = this.designationCodeWiseEmployeeMap.get(designationCode);
			return ets;
		}*/
	}
	public boolean DesignationAlloted(int designationCode) throws BLException{
		boolean allocated = false;
		if(this.designationCodeWiseEmployeeMap.containsKey(designationCode)){
			allocated = true;
		}
		return allocated;
	}
}
