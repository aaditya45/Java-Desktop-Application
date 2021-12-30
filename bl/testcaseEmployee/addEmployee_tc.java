import java.util.*;
import java.math.*;
import java.text.*;
import java.io.*;
import com.thinking.machines.hr.bl.managers.*;
import com.thinking.machines.hr.bl.pojo.*;
import com.thinking.machines.hr.bl.exceptions.*;
import com.thinking.machines.hr.bl.interfaces.pojo.*;
import com.thinking.machines.hr.bl.interfaces.managers.*;
import com.thinking.machines.hr.dl.dto.*;
import com.thinking.machines.hr.dl.interfaces.dto.*;
import com.thinking.machines.hr.dl.interfaces.dao.*;
import com.thinking.machines.hr.dl.exceptions.*;
import com.thinking.machines.hr.dl.dao.*;
import com.thinking.machines.enums.*;
class EmployeeAddEmployeeTestCase{
	public static void main(String gg[]){
		String name = gg[0];
		int DesignationCode = Integer.parseInt(gg[1]);;
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		Date dateOfBirth = null;
		try{
			dateOfBirth = sdf.parse(gg[2]);
		}catch(ParseException pe){
			System.out.println(pe.getMessage());
			return ;
		}
		char Gender = gg[3].charAt(0);
		boolean IsIndian = Boolean.parseBoolean(gg[4]);
		BigDecimal BasicSalary = new BigDecimal(gg[5]);
		String PANNumber = gg[6];
		String AadharCardNumber = gg[7];
		try{
			EmployeeInterface ei = new Employee();
			EmployeeManagerInterface emi = EmployeeManager.getEmployeeManager();
			DesignationInterface designation = new Designation();
			designation.setCode(DesignationCode);
			ei.setName(name);
			ei.setDesignation(designation);
			ei.setDateOfBirth(dateOfBirth);
				if(Gender == 'M'){
					ei.setGender(GENDER.MALE);
				}else if(Gender == 'F'){
					ei.setGender(GENDER.FEMALE);
				}
			ei.setIsIndian(IsIndian);
			ei.setBasicSalary(BasicSalary);
			ei.setPANNumber(PANNumber);
			ei.setAadharCardNumber(AadharCardNumber);
			emi.addEmployee(ei);
			System.out.println("Emloyee Code is : "+ ei.getEmployeeId());
		}catch(BLException ble){
			List<String> exceptions = new ArrayList<>();
			if(ble.hasGenericException()){
				System.out.println(ble.getGenericException());
			}
			exceptions = ble.getProperties();
			for(String property : exceptions){
				System.out.println(ble.getException(property));
			}
		}
	}	
}