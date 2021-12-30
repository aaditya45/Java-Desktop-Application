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
class EmployeeGetEmployeeByIdTestCase{
	public static void main(String gg[]){
		String employeeId = gg[0];
		try{
			EmployeeManagerInterface emi = EmployeeManager.getEmployeeManager();
			EmployeeInterface x = new Employee();
			x = emi.getEmployeeById(employeeId);
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
			System.out.print("Name : " +x.getName() + "\n" +"Date of Birth : "+ sdf.format(x.getDateOfBirth()) + "\n" +"ID : "+x.getEmployeeId() + "\n" +"Is Employee Indian : "+x.getIsIndian() + "\n" +"Gender : "+x.getGender() + "\n" +"Salary : "+x.getBasicSalary().toPlainString() + "\n" +"PAN Card Number : "+x.getPANNumber() + "\n" +"Aadhar Card Number : "+x.getAadharCardNumber() + "\n"+"******************************\n" );
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