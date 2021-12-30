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
class EmployeeDesignationAllotedTestCase{
	public static void main(String gg[]){
		int designation = Integer.valueOf(gg[0]);
		try{
			EmployeeManagerInterface emi = EmployeeManager.getEmployeeManager();
			System.out.println(emi.DesignationAlloted(designation));
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