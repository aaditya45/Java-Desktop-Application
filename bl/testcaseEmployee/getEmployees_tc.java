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
class EmployeeGetEmployeesTestCase{
	public static void main(String gg[]){
		try{
			Set<EmployeeInterface> employees = new TreeSet<>();
			EmployeeManagerInterface emi = EmployeeManager.getEmployeeManager();
			employees = emi.getEmployees();
			for(EmployeeInterface employee : employees){
				System.out.println("ID : "+employee.getEmployeeId()+"\nName :"+employee.getName());
			}
			System.out.println("*********");
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