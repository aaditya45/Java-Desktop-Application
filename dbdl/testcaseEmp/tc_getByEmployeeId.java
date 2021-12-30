import java.util.*;
import java.math.*;
import java.text.*;
import java.io.*;
import com.thinking.machines.hr.dl.dto.*;
import com.thinking.machines.hr.dl.interfaces.dto.*;
import com.thinking.machines.hr.dl.interfaces.dao.*;
import com.thinking.machines.hr.dl.exceptions.*;
import com.thinking.machines.hr.dl.dao.*;
import com.thinking.machines.enums.*;
class EmployeeGetByEmployeeIdTestCase{
	public static void main(String gg[]){
		String EmpID = gg[0];
		try{
			EmployeeDAOInterface edaoi = new EmployeeDAO();
			EmployeeDTOInterface x = new EmployeeDTO();
			x = edaoi.getByEmployeeId(EmpID);
			System.out.print("Name : " +x.getName() + "\n" +"Date of Birth : "+ x.getDateOfBirth() + "\n" +"ID : "+x.getEmployeeId() + "\n" +"Is Employee Indian : "+x.getIsIndian() + "\n" +"Gender : "+x.getGender() + "\n" +"Salary : "+x.getBasicSalary().toPlainString() + "\n" +"PAN Card Number : "+x.getPANNumber() + "\n" +"Aadhar Card Number : "+x.getAadharCardNumber() + "\n"+"******************************\n" );
		}catch(DAOException daoe){
			System.out.println(daoe.getMessage());
		}
		
		
		
	}	
}