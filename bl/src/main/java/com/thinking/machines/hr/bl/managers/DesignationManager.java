package com.thinking.machines.hr.bl.managers;
import com.thinking.machines.hr.bl.exceptions.*;
import com.thinking.machines.hr.bl.interfaces.pojo.*;
import com.thinking.machines.hr.bl.interfaces.managers.*;
import com.thinking.machines.hr.bl.pojo.*;
import java.util.*;
import com.thinking.machines.hr.dl.dao.*;
import com.thinking.machines.hr.dl.dto.*;
import com.thinking.machines.hr.dl.interfaces.dao.*;
import com.thinking.machines.hr.dl.interfaces.dto.*;
import com.thinking.machines.hr.dl.exceptions.*;
public class DesignationManager implements DesignationManagerInterface{
	private Map<Integer,DesignationInterface> codeWiseDesignationManager; 
	private Map<String,DesignationInterface> titleWiseDesignationManager; 
	private Set<DesignationInterface> designationsSet; 
	private static DesignationManager designationManager = null;
	//NOTE : - Keeping constructor private inorder to make This class object an singleton.
	private DesignationManager() throws BLException{
		populateDataStructure();
	}
	public void populateDataStructure() throws BLException{
		this.codeWiseDesignationManager = new HashMap<>();
		this.titleWiseDesignationManager = new HashMap<>();
		this.designationsSet = new TreeSet<>();
		try{
			DesignationDAOInterface ddaoi = new DesignationDAO();
			Set<DesignationDTOInterface> designationDTOs;
			designationDTOs = ddaoi.getAll();
			DesignationInterface designation;
			for(DesignationDTOInterface d : designationDTOs){
				designation = new Designation();
				designation.setCode(d.getCode());
				designation.setTitle(d.getTitle());
				this.designationsSet.add(designation);
				this.codeWiseDesignationManager.put(new Integer(designation.getCode()),designation);
				this.titleWiseDesignationManager.put(designation.getTitle().toUpperCase(),designation);
			}
		}catch(DAOException daoexception){
			BLException ble = new BLException();
			ble.setGenericException(daoexception.getMessage());
			throw ble;
		}
	}
	// this method is for 
	public static DesignationManagerInterface getDesignationManager() throws BLException{
		if(designationManager == null) designationManager = new DesignationManager();
		return designationManager;
	}
	public void addDesignation(DesignationInterface designation) throws BLException{
		BLException ble = new BLException();
		String title = "";
		int code= 0;
		if(designation == null){
			ble.setGenericException("Invalid Input");
			throw ble;
		}
		if(designation.getCode()!=0){
			ble.addException("code","Invalid Code");
			code = 0;
		}
		if(designation.getTitle() == null){
			ble.addException("title","Invalid Title");
			title = "";
		}else{
			title = designation.getTitle();
			title = title.trim();
			if(title.length()==0){
				ble.addException("title","Invalid Title");
			}
		}
		if(ble.hasExceptions()){
			throw ble;
		}
		try{
			DesignationDTOInterface ddtoi = new DesignationDTO();
			DesignationDAOInterface ddaoi = new DesignationDAO();
			ddtoi.setCode(code);
			ddtoi.setTitle(title);
			ddaoi.add(ddtoi);
			DesignationInterface clone = new Designation();
			clone.setCode(ddtoi.getCode());
			clone.setTitle(ddtoi.getTitle());
			designation.setCode(ddtoi.getCode());//in order to recive it back at testcase
			this.designationsSet.add(clone);
			this.codeWiseDesignationManager.put(ddtoi.getCode(),clone);
			this.titleWiseDesignationManager.put(ddtoi.getTitle(),clone);
		}catch(DAOException daoe){
			BLException blexception = new BLException();
			blexception.setGenericException(daoe.getMessage());
			throw blexception;
		}
	}
	public void updateDesignation(DesignationInterface designation) throws BLException{
		BLException ble = new BLException();
		String title;
		int code;
		code = designation.getCode();
		title = designation.getTitle();
		if(designation == null){
			ble.setGenericException("Invalid Input");
			throw ble;
		}
		if(code<=0){
			ble.addException("code","Invalid Code");
			code = 0;
			throw ble;
		}
		if(this.codeWiseDesignationManager.containsKey(new Integer(code))==false){
			ble.addException("code","Code "+code+" does not Exists.");
			throw ble;
		}
		if(title == null){
				ble.addException("title","Invalid Title");
			title = "";
		}else{
			title = title.trim();
			if(title.length()==0){
				ble.addException("title","Invalid Title");
			}
		}
		if(ble.hasExceptions()){
			throw ble;
		}
		DesignationInterface di;
		try{
			DesignationDTOInterface ddtoi = new DesignationDTO();
			DesignationDAOInterface ddaoi = new DesignationDAO();
			ddaoi.getByCode(code);
			ddtoi.setCode(code);
			ddtoi.setTitle(title);
			ddaoi.update(ddtoi);
			di = this.codeWiseDesignationManager.get(code);
			//remove old data from data structures
			this.codeWiseDesignationManager.remove(code);
			this.titleWiseDesignationManager.remove(di.getTitle().toUpperCase());
			this.designationsSet.remove(di);
			//update the data
			di.setTitle(title);
			//add new data into ds
			this.codeWiseDesignationManager.put(code,di);
			this.titleWiseDesignationManager.put(title.toUpperCase(),di);
			this.designationsSet.add(di);
		}catch(DAOException daoe){
			BLException blexception = new BLException();
			blexception.setGenericException(daoe.getMessage());
			throw blexception;
		}
	}
	public void removeDesignation(int code) throws BLException{
		BLException ble =new BLException();
		if(code<=0){
			ble.addException("code","Invalid code, code can not be "+code);
			throw ble;
		}
		if(this.codeWiseDesignationManager.containsKey(new Integer(code))==false){
			ble.addException("code","Invalid code, code can not be "+code);
			throw ble;
		}
		DesignationInterface di;
		try{
			DesignationDTOInterface ddtoi = new DesignationDTO();
			DesignationDAOInterface ddaoi = new DesignationDAO();
			di = this.codeWiseDesignationManager.get(code);
			ddaoi.delete(code);//error
			//remove data from data structures
			this.codeWiseDesignationManager.remove(code);
			this.titleWiseDesignationManager.remove(di.getTitle().toUpperCase());
			this.designationsSet.remove(di);
		}catch(DAOException daoe){
			BLException blexception = new BLException();
			blexception.setGenericException(daoe.getMessage());
			throw blexception;
		}
	}
	public DesignationInterface getDesignationByCode(int code) throws BLException{
		BLException ble = new BLException();
		if(code<=0){
			ble.addException("code","Code is Invalid.");
			throw ble;
		}
		if(this.codeWiseDesignationManager.containsKey(code)==false){
			ble.addException("code","Code Does not exists.");
			throw ble;
		}
		DesignationInterface di = new Designation();
		di = this.codeWiseDesignationManager.get(code);
		return di;
	}
	public DesignationInterface getDesignationByTitle(String title) throws BLException{
		BLException ble = new BLException();
		if(this.titleWiseDesignationManager.containsKey(title.toUpperCase())==false){
			ble.addException("title","Title Does not exists.");
			throw ble;
		}
		DesignationInterface di = new Designation();
		di = this.titleWiseDesignationManager.get(title.toUpperCase());
		return di;
	}
	public int getDesignationCount() throws BLException{
		BLException blexception = new BLException();
		blexception.setGenericException("not implemented yet.");
		throw blexception;
	}
	public boolean designationCodeExists(int code) throws BLException{
		getDesignationByCode(code);
		return true;
	}
	public boolean designationTitleExists(String title) throws BLException{
		BLException blexception = new BLException();
		blexception.setGenericException("not implemented yet.");
		throw blexception;
	}
	public Set<DesignationInterface> getDesignations() throws BLException{
		DesignationInterface di;
		Set<DesignationInterface> designations = new TreeSet<>();
		for(DesignationInterface designation : this.designationsSet){
			di = designation;
			designations.add(di);
		}
		return designations;
	}
}
