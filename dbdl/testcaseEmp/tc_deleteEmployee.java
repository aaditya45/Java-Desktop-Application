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
class EmployeeDeleteEmployeeIdTestCase{
	public static void main(String gg[]){
		String EmpID = gg[0];
		try{
			EmployeeDAOInterface edaoi = new EmployeeDAO();
			edaoi.delete(EmpID);
			System.out.println("Employee Data got deleted.");
		}catch(DAOException daoe){
			System.out.println(daoe.getMessage());
		}
		
		
		
	}	
}