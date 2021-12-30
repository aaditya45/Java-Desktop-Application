import java.util.*;
import java.math.*;
import java.text.*;
import java.io.*;
import com.thinking.machines.hr.dl.dto.*;
import com.thinking.machines.hr.dl.interfaces.dto.*;
import com.thinking.machines.hr.dl.interfaces.dao.*;
import com.thinking.machines.hr.dl.exceptions.*;
import com.thinking.machines.hr.dl.dao.*;
class EmployeeGetAllEmployeeTestCase{
	public static void main(String gg[]){
		try{
			Set<EmployeeDTOInterface> employees = new TreeSet<>();
			EmployeeDAOInterface edaoi = new EmployeeDAO();
			employees = edaoi.getAll();
			employees.forEach(x -> System.out.print("Name : " +x.getName() + "\n" +"Date of Birth : "+x.getDateOfBirth() + "\n" +"ID : "+x.getEmployeeId() + "\n" +"Is Employee Indian : "+x.getIsIndian() + "\n" +"Gender : "+x.getGender() + "\n" +"Salary : "+x.getBasicSalary() + "\n" +"PAN Card Number : "+x.getPANNumber() + "\n" +"Aadhar Card Number : "+x.getAadharCardNumber() + "\n"+"******************************\n" ));
		}catch(DAOException daoe){
			System.out.println(daoe.getMessage());
		}
		
		
		
	}	
}